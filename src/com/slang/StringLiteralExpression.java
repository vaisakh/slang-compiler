package com.slang;

public class StringLiteralExpression extends Expression {
    private Symbol symbol;

    public StringLiteralExpression(String value) {
        this.symbol = new Symbol();
        this.symbol.name = null;
        this.symbol.name = value;
        this.symbol.type = Type.STRING;
    }
    @Override
    public Symbol Evaluate(RuntimeContext context) {
        return this.symbol;
    }

    @Override
    public Type TypeCheck(CompilationContext context) {
        return this.symbol.type;
    }

    @Override
    public Type getType() {
        return this.symbol.type;
    }
}
