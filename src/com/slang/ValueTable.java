package com.slang;

// Value table used for keyword defining
public class ValueTable {
    public Token token;
    public String value;
    public ValueTable(Token token, String value) {
        this.token = token;
        this.value = value;
    }
}