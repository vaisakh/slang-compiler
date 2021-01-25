package com.slang;

public class LogicalExpression extends Expression {
    private Token operator;
    private Type type;
    private Expression lExpression, rExpression;

    public LogicalExpression(Expression left, Expression right, Token op) {
        lExpression = left;
        rExpression = right;
        operator = op;
    }
    @Override
    public Symbol Evaluate(RuntimeContext context) throws Exception {
        Symbol evalLeft = this.lExpression.Evaluate(context);
        Symbol evalRight = this.rExpression.Evaluate(context);

        if (evalLeft.type == Type.BOOLEAN && evalRight.type == Type.BOOLEAN) {
            Symbol retval = new Symbol();
            retval.type = Type.BOOLEAN;
            retval.name = "";

            if (this.operator == Token.TOK_AND) {
                retval.booleanValue = (evalLeft.booleanValue && evalRight.booleanValue);
            } else if (this.operator == Token.TOK_OR) {
                retval.booleanValue = (evalLeft.booleanValue || evalRight.booleanValue);
            } else {
                return null;
            }
            return retval;
        }

        return null;
    }

    @Override
    public Type TypeCheck(CompilationContext context) throws Exception {
        Type lType = this.lExpression.TypeCheck(context);
        Type rType = this.rExpression.TypeCheck(context);

        if ((lType == rType) && (lType == Type.BOOLEAN)) {
            this.type = lType;
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
