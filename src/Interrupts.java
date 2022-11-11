public enum Interrupts {
    INT_NONE,
    INT_INVALID_INSTRUCTION,    // Nunca será usada, pois o Java não deixará compilar
    INT_INVALID_ADDRESS,        // Nossa memória tem 1024 posições
    INT_OVERFLOW,               // Nossa memória só trabalha com inteiros, ou seja de -2,147,483,648 até 2,147,483,647
    INT_SYSTEM_CALL,            // Ativa chamada de I/O pelo comando TRAP
    INT_SCHEDULER,              // Aciona o Escalonador
    INT_STOP;                   // Usada com o escalonador
}
