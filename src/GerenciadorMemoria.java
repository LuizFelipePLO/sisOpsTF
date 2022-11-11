public class GerenciadorMemoria {

    public Word[] mem;
    private int tamPagina;
    private int tamFrame;
    private int nroFrames;
    private boolean[] tabelaPaginas;
    //private int quantidadeDePaginasUsadas; // só para debug
    public int [] framesAlocados;

    public GerenciadorMemoria(Word[] mem, int tamPagina) {
        this.mem = mem;
        this.tamPagina = tamPagina;
        tamFrame = tamPagina;
        nroFrames = mem.length / tamPagina;
        tabelaPaginas = initFrames();
        //quantidadeDePaginasUsadas = 0;
    }

    public int getTamPagina(){
        return tamPagina;
    }

    private boolean[] initFrames() {
        boolean[] free = new boolean[nroFrames];
        for (int i = 0; i < nroFrames; i++) {
            free[i] = true;
        }

        //mockando frames ocupados
        free[0]=false;
        free[2]=false;

        return free;
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

    public void dumpMem(Word[] m, int ini, int fim) {
        for (int i = ini; i < fim; i++) {
            System.out.print(i);
            System.out.print(":  ");
            dump(m[i]);
        }
    }

    public int getQuantidadePaginasUsadas(){
        int quantidade = 0;
        for (int i=0; i<tabelaPaginas.length; i++){
            if (tabelaPaginas[i]==false) quantidade++;
        }
        return quantidade;
    }

    public void dumpMemoriaUsada(Word[] m) {
        int fim = getQuantidadePaginasUsadas() * tamPagina;
        for (int i = 0; i < fim; i++) {
            System.out.print(i);
            System.out.print(":  ");
            dump(m[i]);
        }
    }

    public void dumpPagina (Word[]m, int pagina){
        int ini = tamPagina * pagina;
        int fim = ini + tamPagina;
        for (int i = ini; i < fim; i++) {
            System.out.print(i);
            System.out.print(":  ");
            dump(m[i]);
        }
    }


    // retorna -1 se não conseguir alocar, ou um array com os frames alocadas
    public int[] aloca(Word[] programa) {
        int quantidadePaginas = programa.length / tamPagina;
        if (programa.length % tamPagina > 0) quantidadePaginas++; // vê se ainda tem código além da divisão inteira
        framesAlocados = new int[quantidadePaginas];
        int indiceAlocado = 0;

        // testa se tem espaço para alocar o programa
        int framesLivres =0;
        for (int i = 0; i < nroFrames; i++) {
            if (tabelaPaginas[i]) //vê se o frame está vazio e conta 1
                framesLivres++;
        }

        // se não existe memória suficiente retorna um array com -1
        if (framesLivres <= quantidadePaginas){
            framesAlocados [0] = -1;
            return framesAlocados;
        }

        // aloca os frames
        for (int i = 0; i < nroFrames; i++) {
            if (quantidadePaginas == 0) break;
            if (tabelaPaginas[i]) { //vê se o frame está vazio e aloca o programa ali
                tabelaPaginas[i] = false; // marca o frame como ocupado
                framesAlocados[indiceAlocado] = i;
                indiceAlocado++;
                quantidadePaginas--;
            }
        }
        return framesAlocados;
    }

    public int[] getFramesAlocados(){
        return framesAlocados;
    }

    public boolean[] getTabelaDePaginas(){
        return tabelaPaginas;
    }

    public void desaloca(PCB processo){
        int[] paginas = processo.getPaginasAlocadas();

        System.out.println(" ");
        System.out.println("------ Estado da memória onde estava o programa que foi desalocado ---------");
        System.out.println("------ Só para mostrar que memória ficou limpa --------");
        for(int i = 0; i < paginas.length; i ++) {
            dumpPagina(mem, paginas[i]);
        }

        // libera os frames
        for(int i = 0; i < paginas.length; i ++) {
            tabelaPaginas[paginas[i]] = true;
        }
    }
}

