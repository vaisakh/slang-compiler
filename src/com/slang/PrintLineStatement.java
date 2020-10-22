package com.slang;

public class PrintLineStatement extends Statement {
    private Expression expression;

    public PrintLineStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public boolean Execute(RuntimeContext context) {
        double a = expression.Evaluate(context);
        System.out.println(String.valueOf(a));
        return true;
    }
}
