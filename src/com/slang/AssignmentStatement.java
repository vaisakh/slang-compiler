package com.slang;

public class AssignmentStatement extends Statement {
    private VariableExpression variable;
    private Expression expression;

    public AssignmentStatement(VariableExpression variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public AssignmentStatement(Symbol variable, Expression expression) {
        this.variable = new VariableExpression(variable);
        this.expression = expression;
    }
    @Override
    public Symbol Execute(RuntimeContext context) throws Exception {
        Symbol value = expression.Evaluate(context);
        context.getSymbolTable().assignSymbol(variable, value);
        return null;
    }
}
