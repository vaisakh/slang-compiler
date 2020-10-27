package com.slang;

public class RuntimeContext {
    private SymbolTable symbolTable;

    public RuntimeContext() {
        this.symbolTable = new SymbolTable();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
}
