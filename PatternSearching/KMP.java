import java.util.ArrayList;

class KMP {
	static int [] lpsArr (String sequence) {
		int N = sequence.length();
		int lpsarr [] = new int[N];
		int i=1, j=0;
		while (i<N) {
			if (sequence.charAt(i) == sequence.charAt(j)) {
				lpsarr[i] = lpsarr[i-1] + 1;
				i++;
				j++;
			} else {
				if (j!=0)
					j = lpsarr[j-1];
				else {
					lpsarr[i] = 0;
					i += 1;
				}
			}
		}
		return lpsarr;
	}

	static ArrayList<Integer> findPattern (String sequence, String pattern) {
		ArrayList<Integer> al = new ArrayList<Integer>();
		int [] lps = lpsArr(pattern);
		int m = sequence.length();
		int n = pattern.length();
		int i=0, j=0;
		while (i<m) {
			if (sequence.charAt(i) == pattern.charAt(j)) {
				i++; j++;
			}
			if (j == n) {
				// Found Pattern
				al.add(i-j);
				j = lps[j-1];
			} else if (i<m && pattern.charAt(j) != sequence.charAt(i)) {
				if (j!=0)
					j = lps[j-1];
				else
					i++;
			}
		}
		return al;
	}

	public static void main(String [] args) {
		String dna = "AAGCTCGCATGCAGGAATGAATGAGAGAG";
		String pattern = "AG";
		ArrayList<Integer> list = findPattern(dna, pattern);
		System.out.println(list);
	}
}
