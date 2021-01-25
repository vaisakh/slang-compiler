package com.slang;

import java.util.ArrayList;

public class Module extends AbstractCompilationUnit {
    private String name;
    private ArrayList procedures = null;

    public Module(ArrayList procedures) {
        this.procedures = procedures;
    }

    @Override
    public Symbol Execute(RuntimeContext context, ArrayList actualParameters) throws Exception {
        Procedure p = findProcedure("Main");

        if (p != null) {
            return p.Execute(context, actualParameters);
        }

        return null;
    }

    public Procedure findProcedure(String str) {
        for (Object p : procedures) {
            String pname = ((Procedure) p).getName();
            if (pname.toUpperCase().compareTo(str.toUpperCase()) == 0) {
                return ((Procedure) p);
            }
        }

        return null;
    }
}
