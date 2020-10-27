package com.slang;

public class CParserException extends Exception {
    private int errorCode;
    private String errorString;
    private int lexicalOffset;

    public CParserException(int pErrorCode, String pErrorString, int pLexicalOffset) {
        errorCode = pErrorCode;
        errorString = pErrorString;
        lexicalOffset = pLexicalOffset;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public int getLexicalOffset() {
        return lexicalOffset;
    }

    public void setLexicalOffset(int lexicalOffset) {
        this.lexicalOffset = lexicalOffset;
    }
}
