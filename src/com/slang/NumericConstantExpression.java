package com.slang;

public class NumericConstantExpression extends Expression {
    private Symbol symbol;

    public NumericConstantExpression(double value) {
        this.symbol = new Symbol();
        this.symbol.name = null;
        this.symbol.doubleValue = value;
        this.symbol.type = Type.NUMERIC;
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
