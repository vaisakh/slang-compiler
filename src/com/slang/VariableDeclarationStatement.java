package com.slang;

public class VariableDeclarationStatement extends Statement {
    private Symbol symbol = null;
    private VariableExpression variable = null;

    public VariableDeclarationStatement(Symbol symbol) {
       this.symbol = symbol;
    }

    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        context.getSymbolTable().addSymbol(symbol);
        variable = new VariableExpression(symbol);
        return null;
    }
}
