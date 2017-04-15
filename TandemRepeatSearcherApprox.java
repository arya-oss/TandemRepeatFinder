import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Compile: javac TandemRepeatSearcher.java
 * Usage: time java TandemRepeatSearcher dna.fasta > output.txt
 */

public class TandemRepeatSearcherApprox {
	static char[] input;
	static HashSet<Repeat> repeats = new HashSet<>();
	static final int minLength = 2;
	static int TOLERATE = 2;

	private static int getCenter(int start, int end) {
		return (end - start)/2 + start;
	}

	public static void main(String [] args) throws FileNotFoundException, IOException {
		if (args.length != 1) {
			System.out.println("Usage: java TandemRepeatSearcher /path/to/data.fasta");
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
		findTandemRepeat(0, input.length - 1);
		System.err.println("Found Repeats: "+ repeats.size());
		for (Repeat repeat : repeats) {
			System.out.println(repeat);
			// System.out.println(repeat.motif + "," + dna.substring(repeat.start, repeat.start+repeat.length+1));
		}
	}

	public static void findTandemRepeat(final int start, final int end) {
		int length = end-start+1;
		if (length < 2 * minLength) return;

		final int h = getCenter(start, end);

		findTandemRepeat(start, h);
		findTandemRepeat(h+1, end);

		findTandemRepeatOverCenter (start, end, Side.RIGHT);
		findTandemRepeatOverCenter (start, end, Side.LEFT);
	}

	static void findTandemRepeatOverCenter(final int start, final int end, final int side) {
		final int h = getCenter(start, end);
		for (int l=TOLERATE + 1; l <= h - start; l++) {
			final int q;
			if (side == Side.LEFT) {
				q = h - l;
			} else {
				q = h + l;
			}

			final int lceBackward = backwardLce(start, end, h, q);
			final int lceForward = forwardLce(start, end, h+1, q+1);

			if (lceBackward + lceForward >= l) // Tandem Repeat Found
			{
				final int startPos, endPos;
				if (side == Side.LEFT) {
					startPos = q - lceBackward + 1;
					endPos = h + lceForward;
				} else {
					startPos = h - lceBackward + 1;
					endPos = q + lceForward;
				}
				// it must have tolerance with all consecutive copies of the
				// repeat. Filter if it is not.
				// motif lenght = l
				List<String> parts = split_the_string(
					String.copyValueOf(input).substring(startPos, endPos), l);
				int check_all = check_hamming_dist(parts);
				if (check_all == 1){
					repeats.add(
						new Repeat(startPos, endPos, lceBackward+lceForward+l, l));
				}
			}
		}
	}

	public static List<String> split_the_string(String s, int size){
		List<String> ret = new ArrayList<String>();
		for (String retval: s.split("(?<=\\G.{" + size +"})")) {
			ret.add(retval);
		}

		// // return Arrays.toString(s.split("(?<=\\G.{" + size +"})"));
		return ret;
	}

	public static int check_hamming_dist(List<String> parts){
		for(int i=0;i < parts.size(); i++){
			for(int j = i+1; j< parts.size(); j++){
				if(TOLERATE != getHammingDistance(parts.get(i), parts.get(j))){
					return 0;
				}
			}
		}
		return 1;
	}

	public static int backwardLce(final int start, final int end, int i, int j) {
        int lce = 0;
        int t = TOLERATE;
        final int length = input.length;
        for(int k = 0; k <= length; k++, i--, j--) {
            lce = k;
            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(input[i] != input[j]) {
             if(t == 0) {
             	break;
             }
             else{
             	t--;
             }
        	}
        }

        return lce;
    }

    public static int forwardLce(final int start, final int end, int i, int j) {
        int lce = 0;
        int t = TOLERATE;
        final int length = input.length;
        for(int k = 0; k <= length; k++, i++, j++) {
            lce = k;
            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(input[i] != input[j]) {
             if(t == 0) {
             	break;
             }
             else{
             	t--;
             }
        	}
        }
        return lce;
    }

    public static int getHammingDistance(String sequence1, String sequence2) {
	    char[] s1 = sequence1.toCharArray();
	    char[] s2 = sequence2.toCharArray();

	    int shorter = Math.min(s1.length, s2.length);
	    int longest = Math.max(s1.length, s2.length);

	    int result = 0;
	    for (int i=0; i<shorter; i++) {
	        if (s1[i] != s2[i]) result++;
	    }

	    result += longest - shorter;

	    return result;
	}

	static class Repeat {
		final int motif;
		final int start;
		final int length;
		final int end;
		Repeat(int start, int end, int length, int motif) {
			this.start = start;
			this.length = length;
			this.motif = motif;
			this.end = end;
		}

		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Repeat)) return false;
        	Repeat other = (Repeat) obj;
        	return other.start == this.start && (other.length == this.length || other.motif == other.motif);
		}

		@Override
    	public int hashCode() {
        	return 17*(int)start + 11*(int)length;
    	}

    	@Override
    	public String toString() {
        	return "[start: " + start + ", end: " + end + ", total length: " + length + ", motif length:" + motif + "]";
    	}
	}
	static class Side {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
	}
}