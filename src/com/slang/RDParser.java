package com.slang;

import java.util.ArrayList;

public class RDParser extends Lexer {

    ModuleBuilder prog = null;

    public RDParser(String expression) {
        super(expression);
        prog = new ModuleBuilder();
    }

    public Expression Expression(ProcedureBuilder pBuilder) throws Exception {
        Token operatorToken;
        Expression expression = Term(pBuilder);
        while (currentToken == Token.TOK_PLUS || currentToken == Token.TOK_SUB) {
            operatorToken = currentToken;
            currentToken = getToken();
            Expression exp = Expression(pBuilder);

            if (operatorToken == Token.TOK_PLUS) {
                expression = new BinaryPlus(expression, exp);
            } else {
                expression = new BinaryMinus(expression, exp);
            }
        }
        return expression;
    }

    public Expression BExpr(ProcedureBuilder pBuilder) throws Exception {
        Token operatorToken;
        Expression expression = LExpr(pBuilder);
        while (currentToken == Token.TOK_AND || currentToken == Token.TOK_OR) {
            operatorToken = currentToken;
            currentToken = getNext();
            Expression exp = LExpr(pBuilder);
            expression = new LogicalExpression(expression, exp, operatorToken);
        }
        return expression;
    }

    public Expression LExpr(ProcedureBuilder pBuilder) throws Exception {
        Token operatorToken;
        Expression expression = Expression(pBuilder);
        while (currentToken == Token.TOK_GT
                || currentToken == Token.TOK_LT
                || currentToken == Token.TOK_GTE
                || currentToken == Token.TOK_LTE
                || currentToken == Token.TOK_NEQ
                || currentToken == Token.TOK_EQ) {
            operatorToken = currentToken;
            currentToken = getNext();
            Expression exp = Expression(pBuilder);
            RelationalOperators relop = getRelationalOperator(operatorToken);
            expression = new RelationalExpression(expression, exp, relop);
        }
        return expression;
    }

    public Expression Expr(ProcedureBuilder context) throws Exception {
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

    public Expression Term(ProcedureBuilder context) throws Exception {
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

    public Expression Factor(ProcedureBuilder context) throws Exception {
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
            ///
            ///  Variables
            ///

            String str = super.lastStr;

            if (!prog.isFunction(str)) {
                //
                // if it is not a function..it ought to
                // be a variable...
                Symbol symbol = context.getSymbol(str);

                if (symbol == null) {
                    throw new Exception("Undefined symbol");
                }

                getNext();
                return new VariableExpression(symbol);
            }
            //
            // P can be null , if we are parsing a
            // recursive function call
            //
            Procedure p = prog.getProcedure(str);
            // It is a Function Call
            // Parse the function invocation
            //
            Expression ptr = parseProcedureCallExpression(context, p);
            getNext();

            return ptr;
        }
        else
        {
            System.out.println("Illegal Token");
            throw new Exception();
        }

        return returnValue;

    }

    public Expression parseProcedureCallExpression(ProcedureBuilder pBuilder, Procedure p) throws Exception {
        getNext();

        if (currentToken != Token.TOK_OPAREN) {
            throw new Exception("Opening Parenthesis expected");
        }

        getNext();

        ArrayList actualParameterExpressions = new ArrayList();

        while (true) {
            // Evaluate Each Expression in the
            // parameter list and populate actualParameterExpressions
            // list
            Expression expression = BExpr(pBuilder);
            // do type analysis
            expression.TypeCheck(pBuilder.getContext());
            // if , there are more parameters
            if (currentToken == Token.TOK_COMMA) {
                actualParameterExpressions.add(expression);
                getNext();
                continue;
            }

            if (currentToken != Token.TOK_CPAREN) {
                throw new Exception("Expected paranthesis");
            } else {
                // Add the last parameters
                actualParameterExpressions.add(expression);
                break;
            }
        }

        // if p is null , that means it is a
        // recursive call. Being a one pass
        // compiler , we need to wait till
        // the parse process to be over to
        // resolve the Procedure.
        //
        //
        if (p != null) {
            return new ProcedureCallExpression(p, actualParameterExpressions);
        } else {
            return new ProcedureCallExpression(pBuilder.getProcedureName(),
                    true, // recurse !
                    actualParameterExpressions,
                    pBuilder.getProcedure().type);
        }
    }

//    TODO: need this?
    public ArrayList Parse(ProcedureBuilder context) throws Exception {
        getNext();  // Get the Next Token
        //
        // Parse all the statements
        //

        return StatementList(context);
    }


    // Entry point to the parser
    public Module doParse() throws Exception {
//        ProcedureBuilder p = new ProcedureBuilder("MAIN", new CompilationContext());
//        ArrayList stmts = Parse(p);
//
//        for (Object s : stmts) {
//            p.addStatement((Statement) s);
//        }
//
//        Procedure pc = p.getProcedure();
//
//        prog.addProcedure(pc);
//        return prog.getProgram();

        getNext();   // Get The First Valid Token

        return parseFunctions();
    }

    public Module parseFunctions() throws Exception {
        while (currentToken == Token.TOK_FUNCTION) {
            ProcedureBuilder pb = parseFunction();
            Procedure p = pb.getProcedure();

            if (p == null) {
                System.out.println("Error While Parsing Functions");
                return null;
            }

            prog.addProcedure(p);
            getNext();
        }
        //
        //  Convert the builder into a program
        //
        return prog.getProgram();
    }

    private ArrayList StatementList(ProcedureBuilder context) throws Exception {
        ArrayList arr = new ArrayList();
        while ((currentToken != Token.TOK_END)
                && (currentToken != Token.TOK_ELSE)
                && (currentToken != Token.TOK_ENDIF)
                && (currentToken != Token.TOK_WEND)) {
            Statement temp = Statement(context);
            if (temp != null)
            {
                arr.add(temp);
            }
        }
        return arr;
    }

    private Statement Statement(ProcedureBuilder context) throws Exception {
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
            case TOK_IF:
                retval = ParseIfStatement(context);
                getNext();
                return retval;
            case TOK_WHILE:
                retval = ParseWhileStatement(context);
                getNext();
                return retval;
            case TOK_RETURN:
                retval = parseReturnStatement(context);
                getNext();
                return retval;
            default:
                throw new Exception("Invalid statement");
        }
        return retval;
    }

    private Statement ParsePrintStatement(ProcedureBuilder context) throws Exception {
        getNext();
        Expression e = BExpr(context);
        e.TypeCheck(context.getContext());
        if (currentToken != Token.TOK_SEMI)
        {
            throw new Exception("; is expected");
        }
        return new PrintStatement(e);
    }

    private Statement ParsePrintLNStatement(ProcedureBuilder context) throws Exception {
        getNext();
        Expression a = Expr(context);

        if (currentToken != Token.TOK_SEMI)
        {
            throw new Exception("; is expected");
        }
        return new PrintLineStatement(a);
    }

    public Statement ParseVariableDeclStatement(ProcedureBuilder context) throws Exception {
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
            CSyntaxErrorLog.addLine("invalid variable declaration");
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, ", or ; expected", saveIndex());
        }
    }

    public Statement ParseAssignmentStatement(ProcedureBuilder context) throws Exception {
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

        Expression expression = BExpr(context);

        if (expression.TypeCheck(context.getContext()) != symbol.type) {
            throw new Exception("Type mismatch in assignment");
        }

        if (currentToken != Token.TOK_SEMI) {
            CSyntaxErrorLog.addLine("; expected");
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, " ; expected", -1);
        }

        return new AssignmentStatement(symbol, expression);
    }

    public Statement ParseIfStatement(ProcedureBuilder pBuilder) throws Exception {
        getNext();
        ArrayList truePartStatements = null;
        ArrayList falsePartStatements = null;

        Expression exp = BExpr(pBuilder);  // Evaluate Expression

        if (pBuilder.TypeCheck(exp) != Type.BOOLEAN) {
            throw new Exception("Expects a boolean expression");
        }

        if (currentToken != Token.TOK_THEN) {
            CSyntaxErrorLog.addLine(" Then Expected");
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, "Then Expected", saveIndex());
        }

        getNext();

        truePartStatements = StatementList(pBuilder);

        if (currentToken == Token.TOK_ENDIF) {
            return new IfStatement(exp, truePartStatements, falsePartStatements);
        }

        if (currentToken != Token.TOK_ELSE) {
            throw new Exception("ELSE expected");
        }

        getNext();
        falsePartStatements = StatementList(pBuilder);

        if (currentToken != Token.TOK_ENDIF) {
            throw new Exception("END IF EXPECTED");
        }

        return new IfStatement(exp, truePartStatements, falsePartStatements);
    }

    public Statement ParseWhileStatement(ProcedureBuilder pBuilder) throws Exception {
        getNext();

        Expression expression = BExpr(pBuilder);
        if (pBuilder.TypeCheck(expression) != Type.BOOLEAN) {
            throw new Exception("Expects a boolean expression");
        }

        ArrayList body = StatementList(pBuilder);
        if ((currentToken != Token.TOK_WEND)) {
            CSyntaxErrorLog.addLine("Wend Expected");
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, "Wend Expected", saveIndex());
        }

        return new WhileStatement(expression, body);
    }

    public Statement parseReturnStatement(ProcedureBuilder pBuilder) throws Exception {
        getNext();
        Expression expression = BExpr(pBuilder);
        if (currentToken != Token.TOK_SEMI) {
            CSyntaxErrorLog.addLine("; expected");
            CSyntaxErrorLog.addLine(getCurrentLine(saveIndex()));
            throw new CParserException(-100, " ; expected", -1);
        }
        pBuilder.TypeCheck(expression);
        return new ReturnStatement(expression);
    }

    private RelationalOperators getRelationalOperator(Token tok) {
        if (tok == Token.TOK_EQ) {
            return RelationalOperators.TOK_EQ;
        } else if (tok == Token.TOK_NEQ) {
            return RelationalOperators.TOK_NEQ;
        } else if (tok == Token.TOK_GT) {
            return RelationalOperators.TOK_GT;
        } else if (tok == Token.TOK_GTE) {
            return RelationalOperators.TOK_GTE;
        } else if (tok == Token.TOK_LT) {
            return RelationalOperators.TOK_LT;
        } else {
            return RelationalOperators.TOK_LTE;
        }
    }

    ProcedureBuilder parseFunction() throws Exception {
        //
        // Create a Procedure builder Object
        //
        ProcedureBuilder p = new ProcedureBuilder("", new CompilationContext());
        if (currentToken != Token.TOK_FUNCTION) {
            return null;
        }

        getNext();
        // return type of the Procedure ought to be
        // Boolean , Numeric or String
        if (!(currentToken == Token.TOK_VAR_BOOL
                || currentToken == Token.TOK_VAR_NUMBER
                || currentToken == Token.TOK_VAR_STRING)) {
            return null;
        }


        //-------- Assign the return type
        p.setTypeInfo((currentToken == Token.TOK_VAR_BOOL)
                ? Type.BOOLEAN : (currentToken == Token.TOK_VAR_NUMBER)
                ? Type.NUMERIC : Type.STRING);

        // Parse the name of the Function call
        getNext();
        if (currentToken != Token.TOK_UNQUOTED_STRING) {
            return null;
        }
        p.setProcedureName(this.lastStr); // assign the name


        // ---------- Opening parenthesis for
        // the start of <paramlist>
        getNext();
        if (currentToken != Token.TOK_OPAREN) {
            return null;
        }

        //---- Parse the Formal Parameter list
        formalParameters(p);

        if (currentToken != Token.TOK_CPAREN) {
            return null;
        }

        getNext();

        // --------- Parse the Function code
        ArrayList lst = StatementList(p);

        if (currentToken != Token.TOK_END) {
            throw new Exception("END expected");
        }

        // Accumulate all statements to
        // Procedure builder
        //
        for (Object o : lst) {
            Statement s = (Statement) o;
            p.addStatement(s);
        }
        return p;
    }

    void formalParameters(ProcedureBuilder pBuilder) throws Exception {

        if (currentToken != Token.TOK_OPAREN) {
            throw new Exception("Opening Parenthesis expected");
        }

        getNext();

        ArrayList lst_types = new ArrayList();

        while (currentToken == Token.TOK_VAR_BOOL
                || currentToken == Token.TOK_VAR_NUMBER
                || currentToken == Token.TOK_VAR_STRING) {

            Symbol symbol = new Symbol();

            symbol.type = (currentToken == Token.TOK_VAR_BOOL)
                    ? Type.BOOLEAN : (currentToken == Token.TOK_VAR_NUMBER)
                    ? Type.NUMERIC : Type.STRING;

            getNext();

            if (currentToken != Token.TOK_UNQUOTED_STRING) {
                throw new Exception("Variable Name expected");
            }

            symbol.name = this.lastStr;
            lst_types.add(symbol.type);
            pBuilder.addFormals(symbol);
            pBuilder.addLocal(symbol);
            getNext();

            if (currentToken != Token.TOK_COMMA) {
                break;
            }
            getNext();
        }

        prog.addFunctionProtoType(pBuilder.getProcedureName(), pBuilder.getTypeInfo(), lst_types);
        return;
    }
}