package com.slang;


public class Lexer {
    private String expression;
    int cursor;
    int length;
    double number;
    private int index;
    private ValueTable[] keywords = null;
    public String lastStr;
    private Token currentToken;
    private Token lastToken;


    public Lexer(String expression) {
        this.expression = expression;
        this.length = expression.length();
        this.cursor = 0;
        this.index = 0;

        this.keywords = new ValueTable[7];
        keywords[0] = new ValueTable(Token.TOK_BOOL_FALSE, "FALSE");
        keywords[1] = new ValueTable(Token.TOK_BOOL_TRUE, "TRUE");
        keywords[2] = new ValueTable(Token.TOK_VAR_STRING, "STRING");
        keywords[3] = new ValueTable(Token.TOK_VAR_BOOL, "BOOLEAN");
        keywords[4] = new ValueTable(Token.TOK_VAR_NUMBER, "NUMERIC");
        keywords[5] = new ValueTable(Token.TOK_PRINT, "PRINT");
        keywords[6] = new ValueTable(Token.TOK_PRINTLN, "PRINTLINE");
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
                case '/':
                    token = Token.TOK_DIV;
                    cursor++;
                    break;
                case '*':
                    token = Token.TOK_MUL;
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
                    if (Character.isLetter(expression.charAt(cursor))) {

                        String tempString = String.valueOf(expression.charAt(cursor));
                        cursor++;

                        while (cursor < length && (Character.isLetterOrDigit(expression.charAt(cursor)) ||
                                expression.charAt(cursor) == '_')) {
                            tempString += expression.charAt(cursor);
                            cursor++;
                        }

                        tempString = tempString.toUpperCase();

                        for (int i = 0; i < keywords.length; ++i) {
                            if (keywords[i].value.compareTo(tempString) == 0)
                                return keywords[i].token;
                        }

                        this.lastStr = tempString;
                        return Token.TOK_UNQUOTED_STRING;
                    }
                    else
                    {
                        token = Token.ILLEGAL_TOKEN;
//                        System.out.println("Error While Analyzing Tokens");
//                        throw new Exception();
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
        return index;
    }
}
