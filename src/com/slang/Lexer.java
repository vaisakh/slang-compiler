package com.slang;


public class Lexer {
    private String expression;
    int cursor;
    int length;
    double number;
//    private int index;
    protected ValueTable[] keywords = null;
    public String lastStr;
    protected Token currentToken;
    protected Token lastToken;


    public Lexer(String expression) {
        this.expression = expression;
        this.length = expression.length();
        this.cursor = 0;
//        this.index = 0;

        this.keywords = new ValueTable[16];
        keywords[0] = new ValueTable(Token.TOK_BOOL_FALSE, "FALSE");
        keywords[1] = new ValueTable(Token.TOK_BOOL_TRUE, "TRUE");
        keywords[2] = new ValueTable(Token.TOK_VAR_STRING, "STRING");
        keywords[3] = new ValueTable(Token.TOK_VAR_BOOL, "BOOLEAN");
        keywords[4] = new ValueTable(Token.TOK_VAR_NUMBER, "NUMERIC");
        keywords[5] = new ValueTable(Token.TOK_PRINT, "PRINT");
        keywords[6] = new ValueTable(Token.TOK_PRINTLN, "PRINTLINE");
        keywords[7] = new ValueTable(Token.TOK_IF, "IF");
        keywords[8] = new ValueTable(Token.TOK_THEN, "THEN");
        keywords[9] = new ValueTable(Token.TOK_ELSE, "ELSE");
        keywords[10] = new ValueTable(Token.TOK_ENDIF, "ENDIF");
        keywords[11] = new ValueTable(Token.TOK_WHILE, "WHILE");
        keywords[12] = new ValueTable(Token.TOK_WEND, "WEND");
        keywords[13] = new ValueTable(Token.TOK_FUNCTION, "FUNCTION");
        keywords[14] = new ValueTable(Token.TOK_END, "END");
        keywords[15] = new ValueTable(Token.TOK_RETURN, "RETURN");
    }

    protected Token getNext() throws Exception {
        lastToken = currentToken;
        currentToken = getToken();
        return currentToken;
    }

    public Token getToken() throws Exception {
        Token token;
        boolean restart = false;

        do {
            token = Token.ILLEGAL_TOKEN;

            //Skip white spaces
            while(cursor < length && (expression.charAt(cursor) == ' ' || expression.charAt(cursor) == '\t')) {
                cursor++;
            }

            //Return null if end of string
            if(cursor == length) {
                return Token.TOK_NULL;
            }

            switch (expression.charAt(cursor)) {
                case '\r':
                case '\n':
                    cursor++;
                    restart = true;
                case '+':
                    token = Token.TOK_PLUS;
                    cursor++;
                    break;
                case '-':
                    token = Token.TOK_SUB;
                    cursor++;
                    break;
                case '/': {
                    if (expression.charAt(cursor + 1) == '/') {
                        skipToEoln();
                        restart = true;
                    } else {
                        token = Token.TOK_DIV;
                        cursor++;
                    }
                }
                break;
                case '*':
                    token = Token.TOK_MUL;
                    cursor++;
                    break;
                case ',':
                    token = Token.TOK_COMMA;
                    cursor++;
                    break;
                case '(':
                    token = Token.TOK_OPAREN;
                    cursor++;
                    break;
                case ')':
                    token = Token.TOK_CPAREN;
                    cursor++;
                    break;
                case ';':
                    token = Token.TOK_SEMI;
                    cursor++;
                    break;
                case '!':
                    token = Token.TOK_NOT;
                    cursor++;
                    break;
                case '>':
                    if (expression.charAt(cursor + 1) == '=') {
                        token = Token.TOK_GTE;
                        cursor += 2;
                    } else {
                        token = Token.TOK_GT;
                        cursor++;
                    }
                    break;
                case '<':
                    if (expression.charAt(cursor + 1) == '=') {
                        token = Token.TOK_LTE;
                        cursor += 2;
                    } else if (expression.charAt(cursor + 1) == '>') {
                        token = Token.TOK_NEQ;
                        cursor += 2;
                    } else {
                        token = Token.TOK_LT;
                        cursor++;
                    }
                    break;
                case '=':
                    if(expression.charAt(cursor + 1) == '=') {
                        token = Token.TOK_EQ;
                        cursor += 2;
                    } else {
                        token = Token.TOK_ASSIGN;
                        cursor++;
                    }
                    break;
                case '&':
                    if(expression.charAt(cursor + 1) == '&') {
                        token = Token.TOK_AND;
                        cursor += 2;
                    } else {
                        token = Token.ILLEGAL_TOKEN;
                        cursor++;
                    }
                    break;
                case '|':
                    if(expression.charAt(cursor + 1) == '|') {
                        token = Token.TOK_OR;
                        cursor += 2;
                    } else {
                        token = Token.ILLEGAL_TOKEN;
                        cursor++;
                    }
                    break;
                case '"': {
                    String tempString = "";
                    cursor++;
                    while (cursor < length && expression.charAt(cursor) != '"') {
                        tempString += expression.charAt(cursor);
                        cursor++;
                    }

                    if(cursor == length) {
                        token = Token.ILLEGAL_TOKEN;
                        return token;
                    } else {
                        cursor++;
                        lastStr = tempString;
                        token = Token.TOK_STRING;
                        return token;
                    }
                }
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                {
                    String str = "";
                    while (cursor < length &&
                            (expression.charAt(cursor) == '0' ||
                                    expression.charAt(cursor) == '1' ||
                                    expression.charAt(cursor) == '2' ||
                                    expression.charAt(cursor) == '3' ||
                                    expression.charAt(cursor) == '4' ||
                                    expression.charAt(cursor) == '5' ||
                                    expression.charAt(cursor) == '6' ||
                                    expression.charAt(cursor) == '7' ||
                                    expression.charAt(cursor) == '8' ||
                                    expression.charAt(cursor) == '9'))
                    {
                        str += String.valueOf(expression.charAt(cursor));
                        cursor++;
                    }
                    number = Double.valueOf(str);
                    token = Token.TOK_DOUBLE;
                }
                break;
                default: {
                    if (Character.isDigit(expression.charAt(cursor))) {
                        String tempString = "";

                        while (cursor < length
                                && Character.isDigit(expression.charAt(cursor))) {
                            tempString += expression.charAt(cursor);
                            cursor++;
                        }
                        // Cover for decimal number
                        if (expression.charAt(cursor) == '.') {
                            tempString += '.';
                            cursor++;
                            while (cursor < length
                                    && Character.isDigit(expression.charAt(cursor))) {
                                tempString += expression.charAt(cursor);
                                cursor++;
                            }
                        }
                        number = Double.valueOf(tempString);
                        token = Token.TOK_NUMERIC;
                    } else if (Character.isLetter(expression.charAt(cursor))) {
                        //ie. PRINT or PRINTLINE

                        String tempString = String.valueOf(expression.charAt(cursor));
                        cursor++;

                        while (cursor < length && (Character.isLetterOrDigit(expression.charAt(cursor)) ||
                                expression.charAt(cursor) == '_')) {
                            tempString += expression.charAt(cursor);
                            cursor++;
                        }
                        //PRINT or PRINTLINE

                        tempString = tempString.toUpperCase();


                        for (int i = 0; i < keywords.length; ++i) {
                            if (keywords[i].value.compareTo(tempString) == 0)
                                return keywords[i].token;
                        }

                        this.lastStr = tempString;
                        return Token.TOK_UNQUOTED_STRING;
                    } else {
                        token = Token.ILLEGAL_TOKEN;
                        System.out.println("Error While Analyzing Tokens");
                        throw new Exception();
                    }
                }
            }
        } while(restart);
        return token;
    }

    public double getNumber() { return number; }

    public String getCurrentLine(int index) {
        int tindex = index;
        if (index >= length) {
            tindex = length - 1;
        }
        while (tindex > 0 && expression.toCharArray()[tindex] != '\n')
            tindex--;

        if (expression.toCharArray()[tindex] == '\n')
            tindex++;

        String CurrentLine = "";

        while (tindex < length && (expression.toCharArray()[tindex] != '\n')) {
            CurrentLine = CurrentLine + expression.toCharArray()[tindex];
            tindex++;
        }

        return CurrentLine + "\n";
    }

    public int saveIndex(){
        return cursor;
    }

    public String getPreviousLine(int pindex) {

        int tindex = pindex;
        while ((tindex > 0 )&& (expression.toCharArray()[tindex] != '\n')) {
            tindex--;
        }

        if (expression.toCharArray()[tindex] == '\n') {
            tindex--;
        } else {
            return "";
        }

        while ((tindex > 0) && (expression.toCharArray()[tindex] != '\n')) {
            tindex--;
        }

        if (expression.toCharArray()[tindex] == '\n') {
            tindex--;
        }

        String currentLine = "";

        while (tindex < length && (expression.toCharArray()[tindex] != '\n')) {
            currentLine = currentLine + expression.toCharArray()[tindex];
            tindex++;
        }

        return currentLine + "\n";
    }

    public void restoreIndex(int m_index) {
        cursor = m_index;
    }

    public void skipToEoln() {
        while (cursor < length && (expression.charAt(cursor) != '\n')) {
            cursor++;
        }

        if (cursor == length) {
            return;
        }
    }
}
