package com.slang;

public enum Token {
    ILLEGAL_TOKEN,
    TOK_PLUS, // `+`
    TOK_SUB, // `-`
    TOK_DIV, // `/`
    TOK_MUL, // `*`
    TOK_OPAREN, // `(`
    TOK_CPAREN, // `)`
    TOK_DOUBLE, // [0-9]+
    TOK_NULL, // End of string
    TOK_PRINT, // Print statement
    TOK_PRINTLN, // PrintLine statement
    TOK_UNQUOTED_STRING,
    TOK_SEMI
}
