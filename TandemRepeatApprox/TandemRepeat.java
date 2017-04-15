public class TandemRepeat {

    /**
     * The beginning of the tandem repeat
     */

    /**
     * The lenght of the tandem repeat
     */
    public final long length;
    public final long motif;
    public final long start;
    public final long end;

    public TandemRepeat(final long start, final long end, final long length, final long motif) {
            this.start = start;
            this.length = length;
            this.motif = motif;
            this.end = end;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TandemRepeat)) {
            return false;
        }

        TandemRepeat other = (TandemRepeat) obj;
        return other.start == this.start && other.length == this.length;
    }

    @Override
    public int hashCode() {
        return 17*(int)start+11*(int)length;
    }

    @Override
    public String toString() {
        return "[start: " + start + ", end: " + end + ", total length: " + length + ", motif length:" + motif + "]";
    }
}
