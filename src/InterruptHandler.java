import java.util.Scanner;

public class InterruptHandler {
    private GerenciadorMemoria gerMem;
    private GerenciadorProcessos gerProc;
    private Escalonador escalonador;

    public InterruptHandler(){
    }

    public void setAttributes(GerenciadorMemoria gerMem, GerenciadorProcessos gerProc, Escalonador escalonador){
        this.gerMem = gerMem;
        this.gerProc = gerProc;
        this.escalonador = escalonador;
    }

    public void dump(Word w) {
        System.out.print("[ ");
        System.out.print(w.opc);
        System.out.print(", ");
        System.out.print(w.r1);
        System.out.print(", ");
        System.out.print(w.r2);
        System.out.print(", ");
        System.out.print(w.p);
        System.out.println("  ] ");
    }

    public int traduzEndereco (int endereco){
        int [] paginasAlocadas = gerProc.running.paginasAlocadas;

        /*
        System.out.println("Fazendo tradução do endereço para o trap");
        System.out.print("Páginas alocadas: ");
        for (int i =0; i<paginasAlocadas.length; i++){
            System.out.print(paginasAlocadas[i] + " ");
        }
        System.out.println(" ");
         */

        try {
            return (paginasAlocadas[(endereco / 16)] * 16) + (endereco % 16);
        } catch(ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public boolean handleInterrupt(int[] registers, Word instructionRegister, Word[] memory, int programCounter, Interrupts interrupts) {
        switch (interrupts) {
            case INT_SCHEDULER:
                System.out.println("Escalonador acionado - irá executar agora o processo: " + gerProc.running.id + " - " + gerProc.running.nomeDoPrograma);
                escalonador.runEscalonador(programCounter, registers, instructionRegister, interrupts, gerProc.running.getPaginasAlocadas());
                return true;

            case INT_STOP:
                System.out.println("Ocorreu STOP no código, logo vamos remover o running");
                //gp.removeFromProntos(gp.running);  // se usar essa linha o programa permanece na memória e dá rpa ver o resultado
                gerProc.desalocaProcesso(gerProc.running); // assim o programa sai da memória, mas o dump é limpo

                // vê se ainda tem algum processo na lista e deixa esse como sendo o running
                if (gerProc.prontos.size()>0){
                    if (escalonador.posicaoEscalonador>0) escalonador.posicaoEscalonador = escalonador.posicaoEscalonador - 1;
                    gerProc.running = gerProc.prontos.get(escalonador.posicaoEscalonador);
                    gerProc.setCPUforRunningProcess();
                    return true;
                }
                else{
                    return false;
                }

            case INT_INVALID_ADDRESS:
                System.out.println("Endereço inválido, na linha: " + programCounter);
                dump(memory[programCounter]);
                return false;

            // Consideramos, além de uma instrução inválida, o uso de um registrador inválido também
            case INT_INVALID_INSTRUCTION:
                System.out.println("Comando desconhecido ou registrador inválido, na linha: " + programCounter);
                dump(memory[programCounter]);
                return false;

            case INT_OVERFLOW:
                programCounter--;
                System.out.println("Deu overflow, na linha: " + programCounter);
                dump(memory[programCounter]);
                return false;

            case INT_SYSTEM_CALL:
                // Entrada (in) (reg[8]=1): o programa lê um inteiro do teclado.
                // O parâmetro para IN, em reg[9], é o endereço de memória a armazenar a leitura
                // Saída (out) (reg[8]=2): o programa escreve um inteiro na tela.
                // O parâmetro para OUT, em reg[9], é o endereço de memória cujo valor deve-se escrever na tela

                Scanner in = new Scanner(System.in);

                if (registers[8] == 1) {
                    int address_destiny = traduzEndereco(registers[9]);
                    System.out.println("Insira um número:");
                    int value_to_be_written = in.nextInt();
                    System.out.println("Endereço de destino = " + address_destiny);
                    memory[address_destiny].p = value_to_be_written;
                    return true;
                }

                if (registers[8] == 2) {
                    int source_adress = traduzEndereco(registers[9]);
                    System.out.println("Output: " + memory[source_adress].p);
                    return true;
                }
        }
        return true;
    }
}
