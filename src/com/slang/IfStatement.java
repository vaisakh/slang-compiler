package com.slang;

import java.util.ArrayList;

public class IfStatement extends Statement {
    private Expression conditionExpression;
    private ArrayList<Statement> truePartStatements;
    private ArrayList<Statement> falsePartStatements;

    public IfStatement(Expression expression,
                       ArrayList<Statement> truePartStatements,
                       ArrayList<Statement> falsePartStatements) {
        this.conditionExpression = expression;
        this.truePartStatements = truePartStatements;
        this.falsePartStatements = falsePartStatements;
    }

    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        Symbol condition = conditionExpression.Evaluate(context);
        if ((condition == null) || (condition.type != Type.BOOLEAN)) {
            return null;
        }
        Symbol retval = null;
        if (condition.booleanValue == true) {
            for (Object s : truePartStatements) {
                Statement statement = (Statement) s;
                retval = statement.Execute(context);
                if(retval != null){
                    return retval;
                }
            }
        }

        if((condition.booleanValue == false)&& (falsePartStatements != null)){
            for (Object s : falsePartStatements) {
                Statement statement = (Statement) s;
                retval = statement.Execute(context);
                if(retval != null){
                    return retval;
                }
            }
        }

        return null;
    }
}
