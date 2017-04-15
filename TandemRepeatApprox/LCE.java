
/**
 * The longest common extension.
 */
public class LCE {

    private static long maxLCEForward = 0;
    private static long sumLCEForward = 0;
    private static long countForward = 0;

    private static long maxLCEBackward = 0;
    private static long sumLCEBackward = 0;
    private static long countBackward = 0;


    public static double getAverageLCEForward() {
//        System.out.println("MAX Forward: " + LCE.maxLCEForward);
        if (LCE.sumLCEForward == 0 || LCE.countForward == 0) {
            return 0;
        }
        return sumLCEForward * 1.0 / countForward;
    }

    public static double getAverageLCEBackward() {
//        System.out.println("MAX Backward: " + LCE.maxLCEBackward);
        if (LCE.sumLCEBackward == 0 || LCE.countBackward == 0) {
            return 0;
        }
        return sumLCEBackward * 1.0 / countBackward;
    }

    /**
     * Compute the longest common forward extension for the string s at position i and j.
     * @param string
     * @param i
     * @param j
     * @return lce forward
     */
    public static int forward(final char[] string, final int i, final int j) {
        return LCE.forward(string, 0, string.length - 1, i, j);
    }

    /**
     * Compute the longest common forward extension for the string s at position i and j.
     * @param string
     * @param start of string
     * @param end  of string
     * @param i
     * @param j
     * @return lce forward
     */
    public static int forward(final char[] string, final int start, final int end, int i, int j) {
        int lce = 0;
        final int length = string.length;
        for(int k = 0; k <= length; k++, i++, j++) {
            lce = k;

            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(string[i] != string[j]) { break; }
        }

        // Check for overflow throws ArithmeticException
        LCE.sumLCEForward = Math.addExact(LCE.sumLCEForward, lce);
        LCE.countForward++;
        LCE.maxLCEForward = Math.max(LCE.maxLCEForward, lce);
        return  lce;
    }


    /**
     * Compute the longest common backward extension for the string s at position i and j.
     * @param string
     * @param i
     * @param j
     * @return lce backward
     */
    public static int backward(final char[] string, final int i, final int j) {
        return LCE.backward(string, 0, string.length - 1, i, j);
    }

    /**
     * Compute the longest common backward extension for the string s at position i and j.
     * @param string
     * @param start of string
     * @param end  of string
     * @param i
     * @param j
     * @return lce backward
     */
    public static int backward(final char[] string, final int start, final int end, int i, int j) {
        int lce = 0;
        final int length = string.length;
        for(int k = 0; k <= length; k++, i--, j--) {
            lce = k;
            if (i < start || i > end) { break; }
            if (j < start || j > end) { break; }
            if(string[i] != string[j]) { break; }
        }

        // Check for overflow throws ArithmeticException
        LCE.sumLCEBackward = Math.addExact(LCE.sumLCEBackward, lce);
        LCE.countBackward++;
        LCE.maxLCEBackward = Math.max(LCE.maxLCEBackward, lce);
        return  lce;
    }
}
