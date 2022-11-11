public class PCB {

    public int id;
    public int programCounter;
    public int[] paginasAlocadas;
    public int[] registradores;
    public Word instructionRegister;
    public Interrupts interrupt;
    public String nomeDoPrograma;

    public PCB(int id, int[]paginasAlocadas, int pc, int [] reg, Word ir, Interrupts interrupt) {
        this.id= id;
        this.paginasAlocadas = paginasAlocadas;
        this.programCounter = pc;
        this.registradores = new int[reg.length];
        this.instructionRegister = ir;
        this.interrupt = interrupt;
    }

    public int[] getPaginasAlocadas(){
        return this.paginasAlocadas;
    }

    public void setNomeDoPrograma(String nomeDoPrograma){
        this.nomeDoPrograma = nomeDoPrograma;
    }

    public int getId(){
        return this.id;
    }

    public int getProgramCounter(){
        return programCounter;
    }

    public int[] getRegistradores(){
        return registradores;
    }

    public Word getInstructionRegister(){
        return instructionRegister;
    }

    public Interrupts getInterrupt(){
        return interrupt;
    }

    public void setContext (int programCounter, int[] registradores, Word instructionRegister, Interrupts interrupt){
        this.programCounter = programCounter;
        this.registradores =registradores;
        this.instructionRegister = instructionRegister;
        this.interrupt = interrupt;
    }

    public String toString(){
        String paginasAlocadasString = "[";
        for (int i=0; i<paginasAlocadas.length; i++){
            paginasAlocadasString = paginasAlocadasString + " " + paginasAlocadas[i];
        }
        paginasAlocadasString = paginasAlocadasString + "]";

        String processo = "Process id: " + id + ", Program counter: " + programCounter + ", Páginas alocadas: " + paginasAlocadasString;
        return processo;
    }
}