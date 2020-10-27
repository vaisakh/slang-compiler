package com.slang;

public class UnaryPlus extends Expression {
    private Expression leftExpression;
    private Type type;

    public UnaryPlus(Expression leftExpression) {
        this.leftExpression = leftExpression;
    }
    @Override
    public Symbol Evaluate(RuntimeContext context) throws Exception {
        Symbol evaluateLeft = leftExpression.Evaluate(context);
        if(evaluateLeft.type == Type.NUMERIC) {
            Symbol retval = new Symbol();
            retval.doubleValue = evaluateLeft.doubleValue;
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

        if(evaluateLeftType == Type.NUMERIC) {
            this.type = evaluateLeftType;
            return type;
        } else {
            throw new Exception("Type mismatch failure");
        }
    }

    @Override
    public Type getType() {
        return type;
    }
}
