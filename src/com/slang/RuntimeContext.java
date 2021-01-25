package com.slang;

public class RuntimeContext {
    private SymbolTable symbolTable;
    private Module prog = null;

    public RuntimeContext(Module program) {
        this.symbolTable = new SymbolTable();
        this.prog = program;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Module getProgram() {
        return prog;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
}
