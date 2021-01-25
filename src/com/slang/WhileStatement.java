package com.slang;

import java.util.ArrayList;

public class WhileStatement extends Statement {
    private Expression conditionExpression;
    private ArrayList<Statement> statements;

    public WhileStatement(Expression condExpr, ArrayList<Statement> statements) {
        this.conditionExpression = condExpr;
        this.statements = statements;
    }

    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        while (true) {
            Symbol condition = this.conditionExpression.Evaluate(context);

            if (condition == null || condition.type != Type.BOOLEAN) {
                return null;
            }

            if (condition.booleanValue != true) {
                return null;
            }

            Symbol tsp = null;
            for (Object o : statements) {
                Statement rst = (Statement) o;
                tsp = rst.Execute(context);
                if (tsp != null) {
                    return tsp;
                }
            }
        }
    }
}
