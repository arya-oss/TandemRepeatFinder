

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


public class Application {
        static char[] input;
        public static void main(String [] args) throws FileNotFoundException, IOException {
        if (args.length != 2) {
            System.out.println("Usage: java Application /path/to/data.fasta errors");
            System.exit(1);
        }
        File file = new File(args[0]);
        int size = (int)file.length();
        byte [] bytes = new byte[size];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();
        String dna = new String(bytes, "UTF-8");
        dna = dna.trim();
        input = dna.toCharArray();

        int errors = new Integer(args[1]);
        // int errors = 4;
        boolean noRightBranched = false;
        int minLength = 2;
        final TandemRepeatSearcher searcher;
        searcher = new DirectApproximateTandemRepeatSearcher(new String(input), minLength, noRightBranched, errors);
        searcher.search();
        Set<TandemRepeat> tandemRepeats = searcher.getTandemRepeats();
        Map<Long, Long> histogram = new HashMap<>();
        // System.out.println(tandemRepeats);
        for (TandemRepeat tandemRepeat : tandemRepeats) {
            long length = tandemRepeat.length;
            System.out.println(tandemRepeat);
            if (histogram.containsKey(length)) {
                histogram.put(length, histogram.get(length) + 1);
            } else {
                histogram.put(length, 1L);
            }
        }
    }
}
