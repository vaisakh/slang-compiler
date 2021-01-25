package com.slang;

public class LogicalNot extends Expression {
    private Expression expression;
    private Type type;

    public LogicalNot(Expression expression) {
        this.expression = expression;
    }
    @Override
    public Symbol Evaluate(RuntimeContext context) throws Exception {
        Symbol evalExpression = this.expression.Evaluate(context);
        if (evalExpression.type == Type.BOOLEAN) {
            Symbol retval = new Symbol();
            retval.type = Type.BOOLEAN;
            retval.name = "";
            retval.booleanValue = (!evalExpression.booleanValue);
            return retval;
        }

        return null;
    }

    @Override
    public Type TypeCheck(CompilationContext context) throws Exception {
        Type evalExpression = this.expression.TypeCheck(context);

        if (evalExpression == Type.BOOLEAN) {
            this.type = Type.BOOLEAN;
            return type;
        } else {
            throw new Exception("Wrong Type in expression");
        }
    }

    @Override
    public Type getType() {
        return type;
    }
}
