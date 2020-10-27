package com.slang;

public class VariableExpression extends Expression {
    private String name;
    private Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VariableExpression(Symbol symbol) {
        this.name = symbol.name;
    }

    public VariableExpression(CompilationContext context, String name, double value) {
        Symbol symbol = new Symbol();
        symbol.name = name;
        symbol.type = Type.NUMERIC;
        symbol.doubleValue = value;
        context.getSymbolTable().addSymbol(symbol);
        this.name = name;
    }

    public VariableExpression(CompilationContext context, String name, boolean value) {
        Symbol symbol = new Symbol();
        symbol.name = name;
        symbol.type = Type.BOOLEAN;
        symbol.booleanValue = value;
        context.getSymbolTable().addSymbol(symbol);
        this.name = name;
    }

    public VariableExpression(CompilationContext context, String name, String value) {
        Symbol symbol = new Symbol();
        symbol.name = name;
        symbol.type = Type.STRING;
        symbol.stringValue = value;
        context.getSymbolTable().addSymbol(symbol);
        this.name = name;
    }
    @Override
    public Symbol Evaluate(RuntimeContext context) {
        if(context.getSymbolTable() == null) {
            return null;
        } else {
            Symbol symbol = context.getSymbolTable().getSymbol(name);
            return symbol;
        }
    }

    @Override
    public Type TypeCheck(CompilationContext context) {
        if(context.getSymbolTable() == null) {
            return Type.ILLEGAL;
        } else {
            Symbol symbol = context.getSymbolTable().getSymbol(name);
            if(symbol != null) {
                this.type = symbol.type;
                return type;
            }
        }
        return Type.ILLEGAL;
    }

    @Override
    public Type getType() {
        return type;
    }
}
