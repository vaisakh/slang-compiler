package com.slang;

public class BooleanConstantExpression extends Expression {
    private Symbol symbol;

    public BooleanConstantExpression(boolean value) {
        this.symbol = new Symbol();
        this.symbol.name = null;
        this.symbol.booleanValue = value;
        this.symbol.type = Type.BOOLEAN;
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
