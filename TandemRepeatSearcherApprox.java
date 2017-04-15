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
	static double TOLERATE = 0.4;
	static int minLength = 2;
	static int maxLength = 8;

	private static int getCenter(int start, int end) {
		return (end - start)/2 + start;
	}

	public static void main(String [] args) throws FileNotFoundException, IOException {
		if (args.length < 1) {
			System.out.println("Usage: java TandemRepeatSearcher /path/to/data.fasta [minLength maxLength] [ERROR]");
			System.exit(1);
		}
		if (args.length == 3) {
			minLength = Integer.parseInt(args[1]);
			maxLength = Integer.parseInt(args[2]);
		}
		if (args.length == 4) {
			TOLERATE = Double.parseDouble(args[3]);
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
		for (int l = minLength; l <= h - start && l <= maxLength; l++) {
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
				repeats.add(new Repeat(startPos, lceBackward+lceForward+l, l));
			}
		}
	}

	public static int backwardLce(final int start, final int end, int i, int j) {
        int lce = 0;
        int t = 0;
        final int length = input.length;
        for(int k = 0; k <= length; k++, i--, j--) {
            lce = k;
            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(input[i] != input[j]) {
             	t++;
        		if ((double) t/k > TOLERATE) {
        			break;
        		}
        	}
        }

        return lce;
    }

    public static int forwardLce(final int start, final int end, int i, int j) {
        int lce = 0;
        int t = 0;
        final int length = input.length;
        for(int k = 0; k <= length; k++, i++, j++) {
            lce = k;
            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(input[i] != input[j]) {
             	t++;
        		if ((double) t/k > TOLERATE) {
        			break;
        		}
        	}
        }
        return lce;
    }

	static class Repeat {
		final int motif;
		final int start;
		final int length;
		Repeat(int start, int length, int motif) {
			this.start = start;
			this.length = length;
			this.motif = motif;
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
        	return "[start: " + start + ", length: " + length + ", motif:" + motif + "]";
    	}
	}
	static class Side {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
	}
}