package com.slang;

public class ExpressionBuilder extends Builder {
    public String expressionString;

    public ExpressionBuilder(String expressionString) {
        this.expressionString = expressionString;
    }

    public Expression getExpression()
    {
        try {
            RDParser p = new RDParser(expressionString);
            return p.callExpression();
        } catch (Exception e) {
            return null;
        }
    }
}
