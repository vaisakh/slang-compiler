package com.slang;

public class PrintLineStatement extends Statement {
    private Expression expression;

    public PrintLineStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        Symbol a = expression.Evaluate(context);

        if (a.type == Type.NUMERIC) {
            System.out.println(a.doubleValue);
        } else if (a.type == Type.STRING) {
            System.out.println(a.stringValue);
        } else {
            System.out.println(a.booleanValue);
        }
        return null;
    }
}
