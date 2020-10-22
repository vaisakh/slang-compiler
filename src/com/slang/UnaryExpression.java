package com.slang;

public class UnaryExpression extends Expression {
    private Expression leftExpression;
    private Token token;

    public UnaryExpression(Expression leftExpression, Token token) {
        this.leftExpression = leftExpression;
        this.token = token;
    }

    @Override
    public Symbol Evaluate(RuntimeContext context) {
        switch (token) {
            case TOK_PLUS:
                return leftExpression.Evaluate(context);
            case TOK_SUB:
                return - leftExpression.Evaluate(context);
        }
        return Double.NaN;
    }
}
