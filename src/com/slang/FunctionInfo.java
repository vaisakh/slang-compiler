package com.slang;

import java.util.ArrayList;

public class FunctionInfo {
    public Type retval;
    public String name;
    public ArrayList type;

    public FunctionInfo(String name, Type retval, ArrayList formals) {
        this.retval = retval;
        this.type = formals;
        this.name = name;
    }
}
