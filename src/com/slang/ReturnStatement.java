package com.slang;

public class ReturnStatement extends Statement {
    private Expression expression;
    private Symbol symbol = null;

    public ReturnStatement(Expression e) {
        expression = e;
    }

    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        symbol = (expression == null) ? null : expression.Evaluate(context);
        return symbol;
    }
}
