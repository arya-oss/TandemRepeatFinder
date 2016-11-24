import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Rajmani Arya
 * Date: 8th Oct 2016
 */
public class TandemRepeatFinderSeq {
    public static HashMap<Integer, Integer> map;
    public static HashMap<Integer, Double> dmap;

    public static int[] suffixArray(CharSequence S) {
        int n = S.length();
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++)
            order[i] = n - 1 - i;
        Arrays.sort(order, (a, b) -> Character.compare(S.charAt(a), S.charAt(b)));
        int[] sa = new int[n];
        int[] classes = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = order[i];
            classes[i] = S.charAt(i);
        }
        for (int len = 1; len < n; len *= 2) {
            int[] c = classes.clone();
            for (int i = 0; i < n; i++) {
                classes[sa[i]] = i > 0 && c[sa[i - 1]] == c[sa[i]] && sa[i - 1] + len < n && c[sa[i - 1] + len / 2] == c[sa[i] + len / 2] ? classes[sa[i - 1]] : i;
            }
            int[] cnt = new int[n];
            for (int i = 0; i < n; i++)
                cnt[i] = i;
            int[] s = sa.clone();
            for (int i = 0; i < n; i++) {
                int s1 = s[i] - len;
                if (s1 >= 0)
                    sa[cnt[classes[s1]]++] = s1;
            }
        }
        return sa;
    }
    
    // longest common prefixes array in O(n)
    @SuppressWarnings("empty-statement")
    public static int[] lcp(int[] sa, CharSequence s) {
        int n = sa.length;
        int[] rank = new int[n];
        for (int i = 0; i < n; i++)
            rank[sa[i]] = i;
        int[] lcp = new int[n - 1];
        for (int i = 0, h = 0; i < n; i++) {
            if (rank[i] < n - 1) {
                for (int j = sa[rank[i] + 1]; Math.max(i, j) + h < s.length() && s.charAt(i + h) == s.charAt(j + h); ++h);
                lcp[rank[i]] = h;
                if (h > 0)
                    --h;
            }
        }
        return lcp;
    }
    
    public static void PrintUnitSequence(int startIndex, String dna) throws OutOfMemoryError {
        int pos, len, dna_len = dna.length();
        int SufArr[] = suffixArray(dna);
        int LCP[] = lcp(SufArr, dna);
        int K[] = new int[dna_len];
        int R[] = new int[dna_len];
        for (int i=1; i < dna_len; i++) {
            K[i] = Math.abs(SufArr[i]-SufArr[i-1]);
            R[i] = LCP[i-1]/K[i];
        } 
        System.out.println("Index,Period,Length,UnitLength,Unit");
        for (int i=1; i < dna_len; i++) {
            if (R[i] > 0) {
                pos = Math.min(SufArr[i], SufArr[i-1]);
                len = K[i]*(R[i]+1);
                System.out.println(startIndex+pos + "," + (R[i]+1) + "," + len + "," + K[i] + ","+ dna.substring(pos, pos+K[i]));
            }
        }
    }

    public static void PrintUnit(int startIndex, String dna) throws OutOfMemoryError {
        int pos, len, dna_len = dna.length();
        int SufArr[] = suffixArray(dna);
        int LCP[] = lcp(SufArr, dna);
        int K[] = new int[dna_len];
        int R[] = new int[dna_len];
        for (int i=1; i < dna_len; i++) {
            K[i] = Math.abs(SufArr[i]-SufArr[i-1]);
            R[i] = LCP[i-1]/K[i];
        } 
        System.out.println("Index,Period,Length,UnitLength");
        for (int i=1; i < dna_len; i++) {
            if (R[i] > 0) {
                pos = Math.min(SufArr[i], SufArr[i-1]);
                len = K[i]*(R[i]+1);
                System.out.println(startIndex+pos + "," + (R[i]+1) + "," + len + "," + K[i]);
            }
        }
    }

    public static void PrintRange(int startIndex, String dna) throws OutOfMemoryError {
        int pos, len, dna_len = dna.length();
        int SufArr[] = suffixArray(dna);
        int LCP[] = lcp(SufArr, dna);
        int K[] = new int[dna_len];
        int R[] = new int[dna_len];
        for (int i=1; i < dna_len; i++) {
            K[i] = Math.abs(SufArr[i]-SufArr[i-1]);
            R[i] = LCP[i-1]/K[i];
        } 
        System.out.println("S-Index,E-Index,Period,Sequence");
        for (int i=1; i < dna_len; i++) {
            if (R[i] > 0) {
                pos = Math.min(SufArr[i], SufArr[i-1]);
                len = K[i]*(R[i]+1);
                System.out.println(startIndex+pos+ "," +(startIndex+pos+len)+ "," + (R[i]+1) + "," +dna.substring(pos, pos+K[i]));
            }
        }
    }

    public static void PrintSequence(int startIndex, String dna) throws OutOfMemoryError {
        int pos, len, dna_len = dna.length();
        int SufArr[] = suffixArray(dna);
        int LCP[] = lcp(SufArr, dna);
        int K[] = new int[dna_len];
        int R[] = new int[dna_len];
        for (int i=1; i < dna_len; i++) {
            K[i] = Math.abs(SufArr[i]-SufArr[i-1]);
            R[i] = LCP[i-1]/K[i];
        } 
        System.out.println("Index,Period,Length,Sequence");
        for (int i=1; i < dna_len; i++) {
            if (R[i] > 0) {
                pos = Math.min(SufArr[i], SufArr[i-1]);
                len = K[i]*(R[i]+1);
                System.out.println(startIndex+pos + ","+ (R[i]+1) + "," + len + "," + dna.substring(pos, pos+len));
            }
        }
    }
    
    public static void PrintFrequency(int startIndex, String dna) {
        map = new HashMap<>();
        dmap = new HashMap<>();
        int pos, len, dna_len = dna.length();
        int SufArr[] = suffixArray(dna);
        int LCP[] = lcp(SufArr, dna);
        int K[] = new int[dna_len];
        int R[] = new int[dna_len];
        for (int i=1; i < dna_len; i++) {
            K[i] = Math.abs(SufArr[i]-SufArr[i-1]);
            R[i] = LCP[i-1]/K[i];
        }
        for (int i=1; i < dna_len; i++) {
            if (R[i] > 0) {
                pos = Math.min(SufArr[i], SufArr[i-1]);
                len = K[i]*(R[i]+1);
                if(map.containsKey(K[i])) {
                    int x = map.get(K[i]);
                    map.put(K[i], x+1);
                    dmap.put(K[i], (dmap.get(K[i])*x+len)/x);
                } else {
                    map.put(K[i], 1);
                    dmap.put(K[i], (double)len);
                }
            }
        }

        System.out.println("Period,Frequency,AverageLength");
        for (Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey()+","+entry.getValue()+","+ dmap.get(entry.getKey()));
        }                    
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length != 2) {
            System.out.println("Usage: java TandemRepeatFinder <fasta_file> <output_type>" + 
            "\n1.PrintUnit, 2.PrintSequence, 3.PrintFrequency, 4.PrintUnitSequence, 5.PrintRange\n\t fasta file with {A,C,G,T} only");
            System.exit(1);
        }
        File file = new File(args[0]);
        int option = Integer.parseInt(args[1]);

        int size = (int)file.length();
        byte bytes[] = new byte[size];

        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        String dna_seq = new String(bytes);
        if (option == 1) {
            PrintUnit(0, dna_seq);
        } else if (option == 2) {
            PrintSequence(0, dna_seq);
        } else if ( option == 3) {
            PrintFrequency(0, dna_seq);
        } else if (option == 4) {
            PrintUnitSequence(0, dna_seq);
        } else if (option == 5) {
            PrintRange(0, dna_seq);
        } else {
            System.out.println("Invalid option");
        }
    }
}
