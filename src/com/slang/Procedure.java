package com.slang;

import java.util.ArrayList;

public class Procedure extends AbstractProcedure {
    public String procedureName;
    public ArrayList fParameters = null;
    public ArrayList statements = null;
    public SymbolTable localsVariables = null;
    public Symbol returnValue = null;
    public Type type = Type.ILLEGAL;

    public Procedure(String procedureName, ArrayList fParameters, ArrayList statements, SymbolTable localsVariables, Type type) {
        this.procedureName = procedureName;
        this.fParameters = fParameters;
        this.statements = statements;
        this.localsVariables = localsVariables;
        this.type = type;
    }
    @Override
    public Symbol Execute(RuntimeContext context, ArrayList actualParameters) throws Exception {
        ArrayList vars = new ArrayList();
        int i = 0;
        if (fParameters != null && actualParameters != null) {
            i = 0;
            for (Object o : fParameters) {
                Symbol b = (Symbol) o;
                Symbol inf = (Symbol) actualParameters.get(i);
                inf.name = b.name;
                context.getSymbolTable().addSymbol(inf);
                i++;
            }
        }
        for (Object o : statements) {
            Statement stmt = (Statement) o;

            returnValue = stmt.Execute(context);
            if (returnValue != null) {
                return returnValue;
            }
        }
        return null;
    }

    public String getName() {
        return procedureName;
    }
}
