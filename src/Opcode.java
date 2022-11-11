public enum Opcode {
    DATA, ___,            // se memoria nesta posicao tem um dado, usa DATA, se não usada é NULO ___
    JMP, JMPI, JMPIG, JMPIL, JMPIE, JMPIM, JMPIGM, JMPILM, JMPIEM, STOP,   // desvios e parada
    ADDI, SUBI, ADD, SUB, MULT,             // matemáticos
    LDI, LDD, STD, LDX, STX, SWAP,          // movimentação
    TRAP;                                   //
}
