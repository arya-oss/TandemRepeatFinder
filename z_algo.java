
import java.io.File;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
// import java.nio.file.Path;
import java.nio.file.Paths;
public class z_algo {

    private int[] calculateZ(char input[]) {
        int Z[] = new int[input.length];
        int left = 0;
        int right = 0;
        for(int k = 1; k < input.length; k++) {
            if(k > right) {
                left = right = k;
                while(right < input.length && input[right] == input[right - left]) {
                    right++;
                }
                Z[k] = right - left;
                right--;
            } else {
                //we are operating inside box
                int k1 = k - left;
                //if value does not stretches till right bound then just copy it.
                if(Z[k1] < right - k + 1) {
                    Z[k] = Z[k1];
                } else { //otherwise try to see if there are more matches.
                    left = k;
                    while(right < input.length && input[right] == input[right - left]) {
                        right++;
                    }
                    Z[k] = right - left;
                    right--;
                }
            }
        }
        return Z;
    }

    /**
     * Returns list of all indices where pattern is found in text.
     */
    public List<Integer> matchPattern(char text[], char pattern[]) {
        char newString[] = new char[text.length + pattern.length + 1];
        int i = 0;
        for(char ch : pattern) {
            newString[i] = ch;
            i++;
        }
        newString[i] = '$';
        i++;
        for(char ch : text) {
            newString[i] = ch;
            i++;
        }
        List<Integer> result = new ArrayList<>();
        int Z[] = calculateZ(newString);

        for(i = 0; i < Z.length ; i++) {
            if(Z[i] == pattern.length) {
                result.add(i - pattern.length - 1);
            }
        }
        return result;
    }

    /*
    Read file in byte array. (not working. Problem in readAllBytes )

    public static String readFile(File file, Charset encoding) throws IOException
    {
      byte[] encoded = Files.readAllBytes(file.toPath());
      return new String(encoded, encoding);
    }

    */

    public static String readFile(File file) throws IOException{
        FileInputStream fileInputStream=null;

        //File file = new File("C:\\testing.txt");

        byte[] bFile = new byte[(int) file.length()];
        char[] charArr = new char[(int) file.length()];
        try {
            //convert file into array of bytes
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(bFile);
        fileInputStream.close();

        for (int i = 0; i < bFile.length; i++) {
            charArr[i] = (char)bFile[i];
            }

        //System.out.println("Done");
        }catch(Exception e){
            e.printStackTrace();
        }
        String text = String.valueOf(charArr);
        return text;
    }
    public static void main(String args[]) {
        //String text = "aaabcxyzaaaabczaaczabbaaaaaabc";
        //String pattern = "aaabc";
        if (args.length != 2) {
            System.out.println("Usage: java TandemRepeatFinder <fasta_file> <pattern>\n\t fasta file with {A,C,G,T} only");
            System.exit(1);
        }
        File file = new File(args[0]);
        //System.out.println(file);
        String pattern = new String(args[1]);
        System.out.println("Pattern we are searching is (Using z Algorithm linear time O(m+n): " + pattern);
        String text = new String();
        z_algo zAlgorithm = new z_algo();
        try{
            text = readFile(file);
        }
         catch(IOException e){
             e.printStackTrace();
        }
        //System.out.println(text);
        List<Integer> result = zAlgorithm.matchPattern(text.toCharArray(), pattern.toCharArray());
        //result.forEach(System.out::println);

        for(Integer i: result ){
             System.out.println(i);
        }
    }


}
