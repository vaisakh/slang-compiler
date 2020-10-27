package com.slang;

public class PrintStatement extends Statement {
    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        Symbol a = expression.Evaluate(context);
        System.out.println(String.valueOf(a));
        return null;
    }
}
