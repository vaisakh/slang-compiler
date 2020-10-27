package com.slang;

public abstract class Statement {
    public abstract Symbol Execute(RuntimeContext context) throws Exception;

}
