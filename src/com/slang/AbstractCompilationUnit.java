package com.slang;

import java.util.ArrayList;

public abstract class AbstractCompilationUnit {
    public abstract Symbol Execute(RuntimeContext context, ArrayList actualParameters) throws Exception;
}
