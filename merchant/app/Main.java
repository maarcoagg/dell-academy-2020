package merchant.app;

//import java.util.Scanner;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import merchant.tools.MetalBook;
import merchant.tools.Translator;

public class Main {
    public static void main(String args[]) {

        Path inputDir = Paths.get(System.getProperty("user.dir"), "merchant","inputs");
        File transaction = new File(inputDir.toString(), "input2.txt");
        Translator t = new Translator();

        System.out.println("");
        t.transactionReader(transaction);

        MetalBook mb = new MetalBook();        
        System.out.println("\nMetal Book:\n"+mb.toString());
    }
}
