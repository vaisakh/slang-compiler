package com.slang;

import java.util.ArrayList;

public class ProcedureCallExpression extends Expression {
    Procedure procedure;
    ArrayList actualParameterExpressions;
    String procedureName;
    boolean isRecurse;
    Type type;

    public ProcedureCallExpression(Procedure procedure, ArrayList actualParameterExpressions) {
        this.procedure = procedure;
        this.actualParameterExpressions = actualParameterExpressions;
    }

    // Constructor for recursive function call
    public ProcedureCallExpression(String name, boolean recurse, ArrayList actualParameterExpressions, Type type) {
        this.procedureName = name;
        if (recurse) { isRecurse = true; }
        this.type = type;
        this.actualParameterExpressions = actualParameterExpressions;
    }

    @Override
    public Symbol Evaluate(RuntimeContext context) throws Exception {
        if (procedure != null) {
            //
            // This is a Ordinary Function Call
            //
            //
            RuntimeContext ctx = new RuntimeContext(context.getProgram());

            ArrayList lst = new ArrayList();

            for (Object o : actualParameterExpressions) {
                Expression exp = (Expression) o;
                lst.add(exp.Evaluate(context));
            }

            return procedure.Execute(ctx, lst);

        } else {
            // Recursive function call...by the time we
            // reach here..whole program has already been
            // parsed. Lookup the Function name table and
            // resolve the Address
            //
            //
            procedure = context.getProgram().findProcedure(procedureName);
            RuntimeContext ctx = new RuntimeContext(context.getProgram());
            ArrayList lst = new ArrayList();


            for (Object o : actualParameterExpressions) {
                Expression exp = (Expression) o;
                lst.add(exp.Evaluate(context));
            }

            return procedure.Execute(ctx, lst);

        }
    }

    @Override
    public Type TypeCheck(CompilationContext context) throws Exception {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }
}
