package CodeGenerator;

import java.io.*;
import java.util.*;
import Tuga.*;
import VM.OpCode;
import VM.Instruction.*;

public class CodeGen extends TugaBaseVisitor<Void> {

    // the target code
    private final ArrayList<Instruction> code = new ArrayList<>();
    private final ArrayList<Object> constantPool = new ArrayList<>();
    private int constantPoolIndex = 0;
    //public void constantPoolAddDouble(Double d){this.constantPool.add(d);}
    //public void constantPoolAddString(String s){this.constantPool.add(s);}
    // stat: expr NEWLINE;
    @Override public Void visitStat(TugaParser.StatContext ctx) {
        visit(ctx.expr());
        emit(OpCode.iprint);
        return null;
    }

    // expr: '-' expr                          # Uminus
    @Override public Void visitUnary(TugaParser.UnaryContext ctx) {
        visit(ctx.expr());
        emit(OpCode.iuminus);
        return null;
    }

    // expr: expr '^' expr            # Pow
    @Override public Void visitPow(TugaParser.PowContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        emit(OpCode.ipow);
        return null;
    }

    // expr: expr op=('*'|'/') expr            # MulDiv
    @Override public Void visitMulDivMod(TugaParser.MulDivModContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        if ("*".equals(ctx.op.getText()))
            emit(OpCode.imult);
        else if ("%".equals(ctx.op.getText()))
            emit(OpCode.imod);
        else if ("/".equals(ctx.op.getText()))
            emit(OpCode.idiv);
        else
            System.out.println("erro muldivmod");
        return null;
    }

    // expr: expr op=('+'|'-') expr            # AddSub
    @Override public Void visitAddSub(TugaParser.AddSubContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));
        if ("+".equals(ctx.op.getText()))
            emit(OpCode.iadd);
        else // must be -
            emit(OpCode.isub);
        return null;
    }

    // expr: INT                           # Int
    @Override public Void visitInt(TugaParser.IntContext ctx) {
        emit(OpCode.iconst, Integer.valueOf(ctx.INT().getText()));
        return null;
    }

    @Override public Void visitDouble(TugaParser.DoubleContext ctx) {
        this.constantPool.add(Double.valueOf(ctx.DOUBLE().getText()));
        emit(OpCode.dconst, this.constantPoolIndex);
        this.constantPoolIndex++;
        return null;
    }

    @Override public Void visitString(TugaParser.StringContext ctx) {
        this.constantPool.add(String.valueOf(ctx.STRING().getText()));
        emit(OpCode.sconst, this.constantPoolIndex);
        this.constantPoolIndex++;
        return null;
    }

    // expr:  '(' expr ')'                      # Parens
    @Override public Void visitParens(TugaParser.ParensContext ctx) {
        return visit(ctx.expr());
    }

   /*
        Utility functions
    */

    public void emit(OpCode opc) {
        code.add( new Instruction(opc) );
    }

    public void emit(OpCode opc, int val) {
        code.add( new Instruction1Arg(opc, val) );
    }

    // dump the code to the screen in "assembly" format
    public void dumpCode() {
        System.out.println("Generated code in assembly format");
        for (int i=0; i< code.size(); i++)
            System.out.println( i + ": " + code.get(i) );
    }

    // save the generated bytecodes to file filename
    public void saveBytecodes(String filename) throws IOException {
        try ( DataOutputStream dout =
                      new DataOutputStream(new FileOutputStream(filename)) ) {
            for (Instruction inst : code)   // the instructions
                inst.writeTo(dout);
            System.out.println("Saving the bytecodes to " + filename);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
