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
    TOK_SEMI,
    TOK_VAR_NUMBER, // NUMBER data type
    TOK_VAR_STRING, // STRING data type
    TOK_VAR_BOOL, // Bool data type
    TOK_NUMERIC, // [0-9]+
    TOK_COMMENT, // Comment Token ( presently not used )
    TOK_BOOL_TRUE, // Boolean TRUE
    TOK_BOOL_FALSE, // Boolean FALSE
    TOK_STRING, // String Literal
    TOK_ASSIGN, // Assignment Symbol =
}
