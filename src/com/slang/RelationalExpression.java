package com.slang;

public class RelationalExpression extends Expression {
    private RelationalOperators operator;
    private Expression lExpression, rExpression;
    private Type type;
    private Type operandType;

    public RelationalExpression(Expression left, Expression right, RelationalOperators op) {
        lExpression = left;
        rExpression = right;
        operator = op;
    }

    @Override
    public Symbol Evaluate(RuntimeContext context) throws Exception {
        Symbol evalLeft = this.lExpression.Evaluate(context);
        Symbol evalRight = this.rExpression.Evaluate(context);

        Symbol retval = new Symbol();

        if(evalLeft.type == Type.NUMERIC && evalRight.type == Type.NUMERIC) {
            retval.type = Type.BOOLEAN;
            retval.name = "";
            
            if (this.operator == RelationalOperators.TOK_EQ) {
                retval.booleanValue = (evalLeft.doubleValue == evalRight.doubleValue) ;
            } else if (this.operator == RelationalOperators.TOK_NEQ) {
                retval.booleanValue = (evalLeft.doubleValue != evalRight.doubleValue);
            } else if (this.operator == RelationalOperators.TOK_GT) {
                retval.booleanValue = (evalLeft.doubleValue > evalRight.doubleValue);
            } else if (this.operator == RelationalOperators.TOK_GTE) {
                retval.booleanValue = (evalLeft.doubleValue >= evalRight.doubleValue);
            } else if (this.operator == RelationalOperators.TOK_LTE) {
                retval.booleanValue = (evalLeft.doubleValue <= evalRight.doubleValue);
            } else if (this.operator == RelationalOperators.TOK_LT) {
                retval.booleanValue = (evalLeft.doubleValue < evalRight.doubleValue);
            }

            return retval;
        } else if (evalLeft.type == Type.STRING && evalRight.type == Type.STRING) {
            retval.type = Type.BOOLEAN;
            retval.name = "";

            if(this.operator == RelationalOperators.TOK_EQ) {
                retval.booleanValue = (evalLeft.stringValue.compareTo(evalRight.stringValue) == 0) ? true : false;
            } else if (this.operator == RelationalOperators.TOK_NEQ) {
                retval.booleanValue = evalLeft.stringValue.compareTo(evalRight.stringValue) != 0;
            } else {
                retval.booleanValue = false;
            }

            return retval;
        }

        if (evalLeft.type == Type.BOOLEAN && evalRight.type == Type.BOOLEAN) {
            retval.type = Type.BOOLEAN;
            retval.name = "";

            if (this.operator == RelationalOperators.TOK_EQ) {
                retval.booleanValue = (evalLeft.booleanValue == evalRight.booleanValue);
            } else if (this.operator == RelationalOperators.TOK_NEQ) {
                retval.booleanValue = (evalLeft.booleanValue != evalRight.booleanValue);
            } else {
                retval.booleanValue = false;
            }
            return retval;
        }

        return null;
    }

    @Override
    public Type TypeCheck(CompilationContext context) throws Exception {
        Type lType = this.lExpression.TypeCheck(context);
        Type rType = this.rExpression.TypeCheck(context);

        if (lType != rType) {
            throw new Exception("Wrong Type in expression");
        }

        if (lType == Type.STRING
                && (!(this.operator == RelationalOperators.TOK_EQ
                || this.operator == RelationalOperators.TOK_NEQ))) {
            throw new Exception("Only == and != supported for string type");
        }

        if (lType == Type.BOOLEAN
                && (!(this.operator == RelationalOperators.TOK_EQ
                || this.operator == RelationalOperators.TOK_NEQ))) {
            throw new Exception("Only == and != supported for boolean type");
        }
        // store the operand type as well
        this.operandType = lType;
        type = Type.BOOLEAN;
        return type;
    }

    @Override
    public Type getType() {
        return type;
    }
}
