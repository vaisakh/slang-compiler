package com.slang;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Symbol> symbolTable = new HashMap<String, Symbol>();

    public void addSymbol(Symbol symbol) {
        symbolTable.put(symbol.name, symbol);
    }

    public void assignSymbol(VariableExpression variable, Symbol symbol) {
        symbolTable.put(variable.getName(), symbol);
    }

    public void assignSymbol(String variable, Symbol symbol) {
        symbolTable.put(variable, symbol);
    }

    public Symbol getSymbol(String name) {
        return symbolTable.get(name);
    }
}
