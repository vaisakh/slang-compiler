package com.slang;

import java.util.ArrayList;

public class ModuleBuilder extends AbstractBuilder {
    private ArrayList procedures;
    private ArrayList protos = null;

    public ModuleBuilder() {
        procedures = new ArrayList();
        protos = new ArrayList();
    }

    public boolean isFunction(String name) {
        for (Object o : protos) {
            FunctionInfo fInfo = (FunctionInfo) o;
            if (fInfo.name.equals(name)) {
                return true;
            }
        }
        return false;

    }

    public void addFunctionProtoType(String name, Type retType, ArrayList typeInfo) {
        FunctionInfo info = new FunctionInfo(name, retType, typeInfo);
        protos.add(info);
    }

    public boolean addProcedure(Procedure p) {
        procedures.add(p);
        return true;
    }

    public Procedure getProcedure(String name) {
        for (Object p : procedures) {
            if (((Procedure) p).getName().equals(name)) {
                return ((Procedure) p);
            }
        }
        return null;
    }

    public Module getProgram() {
        return new Module(procedures);
    }
}
