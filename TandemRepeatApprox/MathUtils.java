
public class MathUtils {

    // http://stackoverflow.com/a/3305710
    public static int ld(int bits) {
        int log = 0;
        if( ( bits & 0xffff0000 ) != 0 ) { bits >>>= 16; log = 16; }
        if( bits >= 256 ) { bits >>>= 8; log += 8; }
        if( bits >= 16  ) { bits >>>= 4; log += 4; }
        if( bits >= 4   ) { bits >>>= 2; log += 2; }
        return log + ( bits >>> 1 );
    }

    public static int getCenter(final int start, final int end) {
        return ((MathUtils.getLength(start, end) - 1) / 2) + start;
    }

    public static int getLength(final int start, final int end) {
        return end - start + 1;
    }

    public static int pow(final int base, final int pow) {
        int tmp = 1;
        for (int i = 0; i < pow; i++) {
            tmp *= base;
        }
        return tmp;
    }
}
