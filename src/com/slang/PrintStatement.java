package com.slang;

public class PrintStatement extends Statement {
    private Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        Symbol a = expression.Evaluate(context);

        if (a.type == Type.NUMERIC) {
            System.out.print(a.doubleValue);
        } else if (a.type == Type.STRING) {
            System.out.print(a.stringValue);
        } else {
            System.out.print(a.booleanValue);
        }
        return null;
    }
}
