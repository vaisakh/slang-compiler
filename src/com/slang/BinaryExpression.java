//package com.slang;
//
//public class BinaryExpression extends Expression {
//    private Expression leftExpression;
//    private Expression rightExpression;
//    private Token token;
//
//    public BinaryExpression(Expression leftExpression, Expression rightExpression, Token token) {
//        this.leftExpression = leftExpression;
//        this.rightExpression = rightExpression;
//        this.token = token;
//    }
//
//    @Override
//    public Symbol Evaluate(RuntimeContext context) {
//        switch (token) {
//            case TOK_PLUS:
//                return leftExpression.Evaluate(context) + rightExpression.Evaluate(context);
//            case TOK_SUB:
//                return leftExpression.Evaluate(context) - rightExpression.Evaluate(context);
//            case TOK_DIV:
//                return leftExpression.Evaluate(context) / rightExpression.Evaluate(context);
//            case TOK_MUL:
//                return leftExpression.Evaluate(context) * rightExpression.Evaluate(context);
//        }
//        return Double.NaN;
//    }
//}
