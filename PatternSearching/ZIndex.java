import java.util.ArrayList;

class ZIndex {
	static ArrayList<Integer> findPattern (String sequence, String pattern) {
		ArrayList<Integer> al = new ArrayList<Integer>();
		int m = sequence.length();
		int n = pattern.length();
		int i=1, L=0, R=0;
		String zstr = pattern + "$" + sequence;
		int len = zstr.length();
		int [] zarr = new int[zstr.length()];
		while (i < len) {
			if (i > R) {
				L = R = i;
				while (R < len && zstr.charAt(R-L) == zstr.charAt(R)) {
					R++;
				}
				zarr[i] = R-L;
				R--;
			} else {
				int k = i-L;
				if (zarr[k] < R-i+1) {
					zarr[i] = zarr[k];
				} else {
					L = i;
					while (R < len && zstr.charAt(R-L) == zstr.charAt(R))
						R++;
					zarr[i] = R-L;
					R--;
				}
			}
			i++;
		}

		for(i=0; i<len; i++) {
			if (zarr[i] == n)
				al.add(i-n-1);
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
