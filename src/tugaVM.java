import VM.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class tugaVM {
    public static void runVM(String filename, boolean trace) {
        if (!filename.endsWith(".tugabc")) {
            System.out.println("input file must have a '.tugabc' extension");
            return;
        }

        try {
            byte[] bytecodes = loadBytecodes(filename);
            if (trace) {
                System.out.println("Bytecodes");
                dumpBytecodes(bytecodes);
            }
            vm VM = new vm(bytecodes, trace);
            VM.run();
        } catch (IOException e) {
            System.out.println("Execution failed: " + e.getMessage());
        }
    }

    public static void dumpBytecodes(byte [] bytecodes) {
        StringBuilder s = new StringBuilder();
        for (byte b : bytecodes)
            s.append(String.format("%02X ", b));
        System.out.println(s);
    }

    public static byte[] loadBytecodes(String filename) throws IOException {
        File file = new File(filename);
        byte [] bytecodes = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytecodes);
        }
        return bytecodes;
    }
}