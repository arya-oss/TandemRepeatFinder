import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Compile: javac TandemRepeatSearcherApprox.java
 * Usage: time java TandemRepeatSearcherApprox dna.fasta [min max] [error_rate] > output.txt
 */

public class TandemRepeatSearcherApprox {
	/** char sequence of dna */
	private char[] input;
	
	/** stores a repeats */
	private HashSet<Repeat> repeats = new HashSet<>();
	
	/** Minimum length limit of a motif */
	private int minLength = 2;
	
	/** Maximum length limit of a motif */
	private int maxLength = 8;

	/** starting offset of DNA Chunk */
	private int offset = 0;

	/** Error Rate depending upon length of Repeats */
	private double tolerate = 0.4;

	public TandemRepeatSearcherApprox(char [] input, int minLength, int maxLength, int offset, double tolerate) {
		this.input = input;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.offset = offset;
		this.tolerate = tolerate;
	}

	public void print() {
		for (Repeat repeat : repeats) {
			System.out.println(repeat);
		}
	}

	public void status() {
		System.out.println(repeats.size() + " Repeats found from " + offset + " to " + (input.length + offset));
	}

	public int getSize() {
		return this.input.length;
	}

	/**
	 * 
	 * @param  start starting index of DNA Sequences (0 inclusive)
	 * @param  end   ending index of DNA Sequences (n-1 inclusive)
	 * @return       center of starting and ending index
	 */
	private int getCenter(int start, int end) {
		return (end - start)/2 + start;
	}

	/**
	 * This program find exact tandem repeats in a DNA Sequences
	 * @param  args                  <path/to/fasta_file> [minMotifLength maxMotifLength]
	 * @throws FileNotFoundException 
	 * @throws IOException           
	 */
	public static void main(String [] args) throws FileNotFoundException, IOException {
		int minLength=2;
		int maxLength=8;
		int chunk_size = 100000;
		double tolerate = 0.4;
		if (args.length < 1) {
			System.out.println("Usage: java TandemRepeatSearcherApprox /path/to/data.fasta [minLength maxLength] [Error Rate]");
			System.exit(1);
		}
		
		if (args.length == 3) {
			minLength = Integer.parseInt(args[1]);
			maxLength = Integer.parseInt(args[2]);
		}

		if (args.length == 4) {
			tolerate = Double.parseDouble(args[3]);
		}
		
		File file = new File(args[0]);
        ExecutorService thpool = Executors.newFixedThreadPool(8);
        int size = (int)file.length();
        int chunks = (int) Math.ceil ((double)size/chunk_size);
        byte bytes[] = new byte[chunk_size];
        
        FileInputStream fis = new FileInputStream(file);
        
        for (int i=0; i < chunks; i++) {
            fis.read(bytes);
            Runnable worker = new WorkerThread(new String(bytes), i*chunk_size, minLength, maxLength, tolerate, true);
            thpool.execute(worker);
        }
        
        thpool.shutdown();  
        while (!thpool.isTerminated()) {
        }
	}

	private static class WorkerThread implements Runnable {
        private TandemRepeatSearcherApprox tandemRepeatSearcher;
        private boolean print = false;
        public WorkerThread(String dna, int offset, int minLength, int maxLength, double tolerate, boolean print) {
        	this.print = print;
            tandemRepeatSearcher = new TandemRepeatSearcherApprox(dna.toCharArray(), minLength, maxLength, offset, tolerate);
        }
        @Override
        public void run() {
            tandemRepeatSearcher.findTandemRepeat(0, tandemRepeatSearcher.getSize()-1);
            tandemRepeatSearcher.status();
            if (print)
	            tandemRepeatSearcher.print();
        }
    }

	/**
	 * Divide and Conquer function for finding tandem repeats
	 * @param  start starting index of DNA Sequences (0 inclusive)
	 * @param  end   ending index of DNA Sequences (n-1 inclusive)
	 *  
	 */
	public void findTandemRepeat(final int start, final int end) {
		int length = end-start+1;
		if (length < 2 * minLength) return;

		final int h = getCenter(start, end);

		findTandemRepeat(start, h);
		findTandemRepeat(h+1, end);
		/** check for repeats, prefer right side */
		findTandemRepeatOverCenter (start, end, Side.RIGHT);
		/** check for repeats, prefer left side */
		findTandemRepeatOverCenter (start, end, Side.LEFT);
	}

	/**
	 * Find Tandem Repeats from center of input to both side
	 * @param start start starting index of DNA Sequences (0 inclusive)
	 * @param end   end   ending index of DNA Sequences (n-1 inclusive)
	 * @param side  LEFT or RIGHT
	 */
	public void findTandemRepeatOverCenter(final int start, final int end, final int side) {
		final int h = getCenter(start, end);
		for (int l = minLength; l <= h - start && l <= maxLength; l++) {
			final int q;
			if (side == Side.LEFT) {
				q = h - l;
			} else {
				q = h + l;
			}
			/** longest common extension in backward direction */
			final int lceBackward = backwardLce(start, end, h, q);
			/** longest common extension in forward direction */
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
				/** Add repeat to HashSet */
				repeats.add(new Repeat(startPos + offset, lceBackward+lceForward+l, l));
			}
		}
	}

	/**
	 * Calculate Backward longest common extension
	 * @param  start starting index of sequence
	 * @param  end   ending index of sequence
	 * @param  i     starting index of motif
	 * @param  j     ending index of motif
	 * @return       no. of base pairs mathched
	 */
	public int backwardLce(final int start, final int end, int i, int j) {
		int lce = 0;
        int t = 0;
        final int length = input.length;
        for(int k = 0; k <= length; k++, i--, j--) {
            lce = k;
            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(input[i] != input[j]) {
             	t++;
        		if ((double) t/k > tolerate) {
        			break;
        		}
        	}
        }

        return lce;
    }

    /**
	 * Calculate Backward longest common extension
	 * @param  start starting index of sequence
	 * @param  end   ending index of sequence
	 * @param  i     starting index of motif
	 * @param  j     ending index of motif
	 * @return       no. of base pairs mathched
	 */
    public int forwardLce(final int start, final int end, int i, int j) {
        int lce = 0;
        int t = 0;
        final int length = input.length;
        for(int k = 0; k <= length; k++, i++, j++) {
            lce = k;
            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(input[i] != input[j]) {
             	t++;
        		if ((double) t/k > tolerate) {
        			break;
        		}
        	}
        }
        return lce;
    }

	class Repeat {
		/** motif length */
		final int motif;
		/** Starting index */
		final int start;
		/** total length of repeat */
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

		/**
		 * repeat with same starting point and same length is replaced with later on
		 * @return hashcode of a Repeat object
		 */
		@Override
    	public int hashCode() {
        	return 17*(int)start + 11*(int)length;
    	}

    	@Override
    	public String toString() {
        	return "[start: " + start + ", length: " + length + ", motif:" + motif + "]";
    	}
	}
	
	class Side {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
	}
}