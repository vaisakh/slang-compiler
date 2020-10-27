package com.slang;

import java.util.ArrayList;

public class RDParser extends Lexer {
    private Token currentToken;
    private Token lastToken;

    public RDParser(String expression) {
        super(expression);
    }

//    public Expression callExpression() throws Exception {
//        currentToken = getToken();
//        return Expr();
//    }

    protected Token getNext() throws Exception {
        lastToken = currentToken;
        currentToken = getToken();
        return currentToken;
    }

    public Expression Expr(CompilationContext context) throws Exception {
        Token lToken;
        Expression returnValue = Term(context);

        while (currentToken == Token.TOK_PLUS || currentToken == Token.TOK_SUB) {
            lToken = currentToken;
            currentToken = getToken();
            Expression e1 = Expr(context);

            if(lToken == Token.TOK_PLUS) {
                returnValue = new BinaryPlus(returnValue, e1);
            } else {
                returnValue = new BinaryMinus(returnValue, e1);
            }
        }
        return returnValue;

    }

    public Expression Term(CompilationContext context) throws Exception {
        Token lToken;
        Expression returnValue = Factor(context);

        while (currentToken == Token.TOK_MUL || currentToken == Token.TOK_DIV)
        {
            lToken = currentToken;
            currentToken = getToken();

            Expression e1 = Term(context);
            if(lToken == Token.TOK_MUL) {
                returnValue = new Multiply(returnValue, e1);
            } else {
                returnValue = new Division(returnValue, e1);
            }
        }

        return returnValue;
    }

    public Expression Factor(CompilationContext context) throws Exception {
        Token lToken;
        Expression returnValue = null;

        if(currentToken == Token.TOK_NUMERIC) {
            returnValue = new NumericConstantExpression(getNumber());
            currentToken = getToken();
        }
        else if (currentToken == Token.TOK_STRING) {
            returnValue = new StringLiteralExpression(lastStr);
            currentToken = getToken();
        }
        else if (currentToken == Token.TOK_BOOL_FALSE || currentToken == Token.TOK_BOOL_TRUE)
        {
            returnValue = new BooleanConstantExpression(currentToken == Token.TOK_BOOL_TRUE ? true : false);
            currentToken = getToken();
        }
        else if (currentToken == Token.TOK_DOUBLE)
        {
            returnValue = new NumericConstantExpression(getNumber());
            currentToken = getToken();
        }
        else if (currentToken == Token.TOK_OPAREN)
        {
            currentToken = getToken();

            returnValue = Expr(context);  // Recurse

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
            returnValue = Factor(context);

            if(lToken == Token.TOK_PLUS) {
                returnValue = new UnaryPlus(returnValue);
            } else {
                returnValue = new UnaryMinus(returnValue);
            }
        } else if(currentToken == Token.TOK_UNQUOTED_STRING) {
            String str = super.lastStr;
            Symbol symbol = context.getSymbolTable().getSymbol(str);
            if (symbol == null)
                throw new Exception("Undefined symbol");

            getNext();
            returnValue = new VariableExpression(symbol);
        }
        else
        {
            System.out.println("Illegal Token");
            throw new Exception();
        }

        return returnValue;

    }

    public ArrayList Parse(CompilationContext context) throws Exception {
        getNext();  // Get the Next Token
        //
        // Parse all the statements
        //
        return StatementList(context);
    }

    private ArrayList StatementList(CompilationContext context) throws Exception {
        ArrayList arr = new ArrayList();
        while (currentToken != Token.TOK_NULL)
        {
            Statement temp = Statement(context);
            if (temp != null)
            {
                arr.add(temp);
            }
        }
        return arr;
    }

    private Statement Statement(CompilationContext context) throws Exception {
        Statement retval = null;
        switch (currentToken) {
            case TOK_VAR_STRING:
            case TOK_VAR_NUMBER:
            case TOK_VAR_BOOL:
//                retval = ParseVariableStatement(context);
                retval = ParseVariableDeclStatement(context);
                getNext();
                return retval;
            case TOK_PRINT:
                retval = ParsePrintStatement(context);
                getNext();
                break;
            case TOK_PRINTLN:
                retval = ParsePrintLNStatement(context);
                getNext();
                break;
            case TOK_UNQUOTED_STRING:
                retval = ParseAssignmentStatement(context);
                getNext();
                return retval;
            default:
                throw new Exception("Invalid statement");
        }
        return retval;
    }

    private Statement ParsePrintStatement(CompilationContext context) throws Exception {
        getNext();
        Expression a = Expr(context);

        if (currentToken != Token.TOK_SEMI)
        {
            throw new Exception("; is expected");
        }
        return new PrintStatement(a);
    }

    private Statement ParsePrintLNStatement(CompilationContext context) throws Exception {
        getNext();
        Expression a = Expr(context);

        if (currentToken != Token.TOK_SEMI)
        {
            throw new Exception("; is expected");
        }
        return new PrintLineStatement(a);
    }

    public Statement ParseVariableDeclStatement(CompilationContext context) throws Exception {
        Token token = currentToken;

        getNext();

        if(currentToken == Token.TOK_UNQUOTED_STRING) {
            Symbol symbol = new Symbol();
            symbol.name = super.lastStr;
            symbol.type = (token == Token.TOK_VAR_BOOL) ? Type.BOOLEAN : (token == Token.TOK_VAR_NUMBER) ? Type.NUMERIC : Type.STRING;

            getNext();

            if(currentToken == Token.TOK_SEMI) {
                context.getSymbolTable().addSymbol(symbol);
                return new VariableDeclarationStatement(symbol);
            } else {
                CSyntaxErrorLog.addLine("; expected");
                CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
                throw new CParserException(-100, ", or ; expected", saveIndex());
            }
        } else {
            CSyntaxErrorLog.addLine("invalid variable declaration"); CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, ", or ; expected", saveIndex());
        }
    }

    public Statement ParseAssignmentStatement(CompilationContext context) throws Exception {
        String variable = super.lastStr;
        Symbol symbol = context.getSymbolTable().getSymbol(variable);

        if(symbol == null) {
            CSyntaxErrorLog.addLine("Variable not found " + lastStr);
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, "Variable not found", saveIndex());
        }

        getNext();

        if (currentToken != Token.TOK_ASSIGN) {
            CSyntaxErrorLog.addLine("= expected");
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, "= expected", saveIndex());
        }

        getNext();

        Expression expression = Expr(context);
        if (expression.TypeCheck(context) != symbol.type) {
            throw new Exception("Type mismatch in assignment");
        }

        if (currentToken != Token.TOK_SEMI) {
            CSyntaxErrorLog.addLine("; expected");
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, " ; expected", -1);
        }

        return new AssignmentStatement(symbol, expression);
    }
}
