import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.InputStream;
import Tuga.*;
import CodeGenerator.*;

public class tugaCompiler {
    public static boolean showAsm = false;

    public static boolean compile(String inputFilename) {
        if (!inputFilename.endsWith(".tuga")) {
            System.out.println("input file must have a '.tuga' extension");
            return false;
        }
        String outputFilename = inputFilename + "bc";

        try {
            InputStream is = new FileInputStream(inputFilename);
            CharStream input = CharStreams.fromStream(is);
            TugaLexer lexer = new TugaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TugaParser parser = new TugaParser(tokens);
            ParseTree tree = parser.prog();

            if (parser.getNumberOfSyntaxErrors() != 0) {
                System.out.println(inputFilename + " has syntax errors.");
                return false;
            } else {
                CodeGen codeGen = new CodeGen();
                codeGen.visit(tree);
                if (showAsm) codeGen.dumpCode();
                codeGen.saveBytecodes(outputFilename);
                return true;
            }
        } catch (java.io.IOException e) {
            System.out.println("Compilation failed: " + e.getMessage());
            return false;
        }
    }
}