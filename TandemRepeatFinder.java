package com.arya.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Rajmani Arya
 * Date: 8th Oct 2016
 */
public class TandemRepeatFinder {
    
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
    
    public static void Repeat(int startIndex, String dna) throws OutOfMemoryError {
        int pos, len, dna_len = dna.length();
        int SufArr[] = suffixArray(dna);
        int LCP[] = lcp(SufArr, dna);
        int K[] = new int[dna_len];
        int R[] = new int[dna_len];
        for (int i=1; i < dna_len; i++) {
            K[i] = Math.abs(SufArr[i]-SufArr[i-1]);
            R[i] = LCP[i-1]/K[i];
        }
//        System.out.println("Index   Period   Length   UnitLength");
        for (int i=1; i<dna_len; i++) {
            if (R[i] > 0) {
                pos = Math.min(SufArr[i], SufArr[i-1]);
                len = K[i]*(R[i]+1);
//                System.out.println("Repeat at " + pos + " "+ (R[i]+1) + " " + dna.substring(pos, pos+len));
                System.out.println(startIndex+pos + "\t" + (R[i]+1) + "\t" + len + "\t" + K[i]);
            }
        }
    }
    
    private static class WorkerThread implements Runnable {
        private String dna;
        private int startIndex;
        public WorkerThread(int index, String dna) {
            this.startIndex = index;
            this.dna = dna;
        }
        @Override
        public void run() {
            Repeat(startIndex, dna);
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("/home/arya/Downloads/Project/data.fasta");
        ExecutorService thpool = Executors.newFixedThreadPool(8);
        int size = (int)file.length();
        int chunk_size = (int)Math.ceil((double)size/16000.0);
        byte bytes[] = new byte[16000];
        
        FileInputStream fis = new FileInputStream(file);
        
        for (int i=0; i<chunk_size; i++) {
            fis.read(bytes);
            Runnable worker = new WorkerThread(i*2000, new String(bytes));
            thpool.execute(worker);
        }
        
        thpool.shutdown();  
        while (!thpool.isTerminated()) {
        }
        System.out.println("Finished all threads");  
    }
}