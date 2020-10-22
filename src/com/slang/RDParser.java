package com.slang;

import java.util.ArrayList;

public class RDParser extends Lexer {
    private Token currentToken;
    private Token lastToken;

    public RDParser(String expression) {
        super(expression);
    }

    public Expression callExpression() throws Exception {
        currentToken = getToken();
        return Expr();
    }

    protected Token getNext() throws Exception {
        lastToken = currentToken;
        currentToken = getToken();
        return currentToken;
    }

    public Expression Expr() throws Exception {
        Token lToken;
        Expression returnValue = Term();

        while (currentToken == Token.TOK_PLUS || currentToken == Token.TOK_SUB) {
            lToken = currentToken;
            currentToken = getToken();
            Expression e1 = Expr();
            returnValue = new BinaryExpression(returnValue, e1,
                    lToken == Token.TOK_PLUS ? Token.TOK_PLUS : Token.TOK_SUB);
        }
        return returnValue;

    }

    public Expression Term() throws Exception {
        Token l_token;
        Expression returnValue = Factor();

        while (currentToken == Token.TOK_MUL || currentToken == Token.TOK_DIV)
        {
            l_token = currentToken;
            currentToken = getToken();


            Expression e1 = Term();
            returnValue = new BinaryExpression(returnValue, e1,
                    l_token == Token.TOK_MUL ? Token.TOK_MUL : Token.TOK_DIV);

        }

        return returnValue;
    }

    public Expression Factor() throws Exception {
        Token lToken;
        Expression returnValue = null;
        if (currentToken == Token.TOK_DOUBLE)
        {

            returnValue = new NumericConstantExpression(getNumber());
            currentToken = getToken();

        }
        else if (currentToken == Token.TOK_OPAREN)
        {

            currentToken = getToken();

            returnValue = Expr();  // Recurse

            if (currentToken != Token.TOK_CPAREN)
            {
                System.out.println("Missing Closing Parenthesis\n");
                throw new Exception();

            }
            currentToken = getToken();
        }

        else if (currentToken == Token.TOK_PLUS || currentToken == Token.TOK_SUB)
        {
            lToken = currentToken;
            currentToken = getToken();
            returnValue = Factor();

            returnValue = new UnaryExpression(returnValue,
                    lToken == Token.TOK_PLUS ? Token.TOK_PLUS : Token.TOK_SUB);
        }
        else
        {
            System.out.println("Illegal Token");
            throw new Exception();
        }

        return returnValue;

    }

    public ArrayList Parse() throws Exception {
        getNext();  // Get the Next Token
        //
        // Parse all the statements
        //
        return StatementList();
    }

    private ArrayList StatementList() throws Exception {
        ArrayList arr = new ArrayList();
        while (currentToken != Token.TOK_NULL)
        {
            Statement temp = Statement();
            if (temp != null)
            {
                arr.add(temp);
            }
        }
        return arr;
    }

    private Statement Statement() throws Exception {
        Statement retval = null;
        switch (currentToken) {
            case TOK_PRINT:
                retval = ParsePrintStatement();
                getNext();
                break;
            case TOK_PRINTLN:
                retval = ParsePrintLNStatement();
                getNext();
                break;
            default:
                throw new Exception("Invalid statement");
        }
        return retval;
    }

    private Statement ParsePrintStatement() throws Exception {
        getNext();
        Expression a = Expr();

        if (currentToken != Token.TOK_SEMI)
        {
            throw new Exception("; is expected");
        }
        return new PrintStatement(a);
    }

    private Statement ParsePrintLNStatement() throws Exception {
        getNext();
        Expression a = Expr();

        if (currentToken != Token.TOK_SEMI)
        {
            throw new Exception("; is expected");
        }
        return new PrintLineStatement(a);
    }
}
