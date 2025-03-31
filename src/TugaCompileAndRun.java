public class TugaCompileAndRun {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java calRun <filename> [-asm] [-trace]");
            System.exit(0);
        }

        String filename = args[0];
        boolean showAsm = false;
        boolean trace = false;

        for (String arg : args) {
            if (arg.equals("-asm")) showAsm = true;
            if (arg.equals("-trace")) trace = true;
        }

        tugaCompiler.showAsm = showAsm;

        System.out.println("Compiling...");
        boolean success = tugaCompiler.compile(filename);

        if (success) {
            System.out.println("Compilation successful.");
            String bytecodeFile = filename + "bc";
            System.out.println("Running VM...");
            tugaVM.runVM(bytecodeFile, trace);
        } else {
            System.out.println("Compilation failed. VM not executed.");
        }
    }
}