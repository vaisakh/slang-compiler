package com.slang;

public class CompilationContext {
    private SymbolTable symbolTable;

    CompilationContext() {
        this.symbolTable = new SymbolTable();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

}
