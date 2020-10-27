package com.slang;

public class Division extends Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    private Type type;

    public Division(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
    @Override
    public Symbol Evaluate(RuntimeContext context) throws Exception {
        Symbol evaluateLeft = leftExpression.Evaluate(context);
        Symbol evaluateRight = rightExpression.Evaluate(context);

        if(evaluateLeft.type == Type.NUMERIC && evaluateRight.type == Type.NUMERIC) {
            Symbol retval = new Symbol();
            retval.doubleValue = evaluateLeft.doubleValue / evaluateRight.doubleValue;
            retval.type = Type.NUMERIC;
            retval.name = "";
            return retval;
        } else {
            throw new Exception("Type mismatch");
        }
    }

    @Override
    public Type TypeCheck(CompilationContext context) throws Exception {
        Type evaluateLeftType = leftExpression.TypeCheck(context);
        Type evaluateRightType = rightExpression.TypeCheck(context);

        if(evaluateLeftType == evaluateRightType && evaluateLeftType == Type.NUMERIC) {
            this.type = evaluateLeftType;
            return type;
        } else {
            throw new Exception("Type mismatch failure");
        }
    }

    @Override
    public Type getType() {
        return this.type;
    }
}
