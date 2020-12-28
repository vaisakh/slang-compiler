package com.slang;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        System.out.println("Comilation");
        //Abstract Syntax Tree for 5*5
//        Expression expression = new BinaryExpression(
//                new NumericConstantExpression(5),
//                new NumericConstantExpression(5),
//                Operator.MUL
//        );
//        Expression e = new UnaryExpression(new NumericConstantExpression(2), Operator.MINUS);



        //AST for -(10 + (30+50))
//        Expression e = new UnaryExpression(
//                new BinaryExpression( new NumericConstantExpression(10),
//                        new BinaryExpression(new NumericConstantExpression(30),
//                                new NumericConstantExpression(50),
//                        Token.TOK_PLUS),
//                        Token.TOK_PLUS),
//                Token.TOK_SUB);
//
//
//        System.out.println(e.Evaluate(null));

//        ExpressionBuilder b = new ExpressionBuilder("-2*(-3+3)");
//        Expression e = b.getExpression();
//        System.out.println(e.Evaluate(null));
//        TestFirstScript();
//        TestSecondScript();

//        try {
//            if (args == null || args.length != 1) {
//                System.out.println("CallSlang <scriptname>\n");
//                return;
//            }
//            TestFileScript(args[0]);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//        System.in.read();
    }

   /* static void TestFirstScript() throws Exception {
        String a = "PRINTLINE 2*10;" + "\r\n" + "PRINTLINE 10;\r\n PRINT 2*10;\r\n";
//        String a = "PRINTLINE 2*10;";

        RDParser p = new RDParser(a);
        ArrayList arr = p.Parse();
        for (Object obj:  arr)
        {
            Statement s = (Statement) obj;
            s.Execute(null);
        }
    }

    static void TestSecondScript() throws Exception {
        String a = "PRINTLINE -2*10;" + "\r\n" + "PRINTLINE -10*-1;\r\n PRINT 2*10;\r\n";
        RDParser p = new RDParser(a);
        ArrayList arr = p.Parse();
        for (Object obj: arr)
        {
            Statement s = (Statement) obj;
            s.Execute(null);
        }
    } */

    static void TestFileScript(String filename) throws Exception {
        if (filename == null) {
            return;
        }
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner = new Scanner(new FileInputStream(filename));
        try {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + NL);
            }
        } finally {
            scanner.close();
        }


        RDParser parse = null;
        parse = new RDParser(text.toString());
        CompilationContext ctx = new CompilationContext();
        ArrayList stmts = parse.Parse(ctx);

        RuntimeContext f = new RuntimeContext();
        for (Object obj: stmts)
        {
            Statement s = (Statement) obj;
            s.Execute(f);
        }

    }
}
