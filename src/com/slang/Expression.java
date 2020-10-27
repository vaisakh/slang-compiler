package com.slang;

public abstract class Expression {
    public abstract Symbol Evaluate(RuntimeContext context) throws Exception;
    public abstract Type TypeCheck(CompilationContext context) throws Exception;
    public abstract Type getType();
}
