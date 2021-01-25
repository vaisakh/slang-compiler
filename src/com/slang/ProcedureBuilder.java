package com.slang;

import java.util.ArrayList;

public class ProcedureBuilder extends AbstractBuilder {
    private String procedureName = "";
    CompilationContext context = null;
    ArrayList formalParameters = null;
    ArrayList statements = new ArrayList();
    Type type = Type.ILLEGAL;

    public ProcedureBuilder(String name, CompilationContext context) {
        this.context = context;
        this.procedureName = name;
    }

    public boolean addLocal(Symbol symbol) {
        context.getSymbolTable().addSymbol(symbol);
        return true;
    }

    public boolean addFormals(Symbol symbol) {
        formalParameters.add(symbol);
        return true;
    }

    public Type TypeCheck(Expression e) throws Exception {
        return e.TypeCheck(context);
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    public Symbol getSymbol(String name) {
        return context.getSymbolTable().getSymbol(name);
    }

    public boolean checkProto(String name) {
        return true;
    }

    public Type getTypeInfo() {
        return type;
    }

    public void setTypeInfo(Type type) {
        this.type = type;
    }

    public SymbolTable getSymbolTable() {
        return context.getSymbolTable();
    }

    public CompilationContext getContext() {
        return context;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public Procedure getProcedure() {
        Procedure p = new Procedure(procedureName,formalParameters,
                statements, context.getSymbolTable(), type);
        return p;
    }

}
