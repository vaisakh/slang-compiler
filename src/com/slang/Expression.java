package com.slang;

public abstract class Expression {
    public abstract Symbol Evaluate(RuntimeContext context);
    public abstract Symbol Evaluate(RuntimeContext context);
    public abstract Type TypeCheck(CompilationContext context);
    public abstract Type getType();
}
