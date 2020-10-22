package com.slang;

public class PrintStatement extends Statement {
    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean Execute(RuntimeContext context) {
        double a = expression.Evaluate(context);
        System.out.println(String.valueOf(a));
        return true;
    }
}
