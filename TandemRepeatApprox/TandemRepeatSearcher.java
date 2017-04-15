import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract parent class for all variants of searching for tandem repeats.
 */
abstract public class TandemRepeatSearcher {

    protected final char[] input;
    protected final int minLength;
    protected final Set<TandemRepeat> tandemRepeats;

    /**
     * Save only right branched tandem repeats.
     */
    protected boolean noRightBranched;

    /**
     * Measure the execution time in milliseconds.
     */
    private long beginTimeStamp;
    private long endTimeStamp;

    public TandemRepeatSearcher(final String input, final int minLength, final boolean noRightBranched) {
        this.input = input.toCharArray();
        this.minLength = minLength;
        this.noRightBranched = noRightBranched;
        this.tandemRepeats = new HashSet<>();
    }


    abstract protected boolean canDivide(final int start, final int end);
    abstract protected int lceForward(final int start, final int end, final int h, final int q);
    abstract protected int lceBackward(final int start, final int end, final int h, final int q);

    public Set<TandemRepeat> getTandemRepeats() {
        return this.tandemRepeats;
    }

    public void search() {
        this.search(0, this.input.length - 1);
    }

    public void search(final int start, final int end) {
        this.beginTimeStamp = Time.currentUser();
        this.searchForTandemRepeat(start, end);
        this.endTimeStamp = Time.currentUser();
    }

    public long runtime() {
        return this.endTimeStamp - this.beginTimeStamp;
    }

    /**
     * Find all right and left Tandem Repeat (minimum length 2), which cross the center of the string.
     * This function will be called recursive by dividing the string and updating the postion
     * correspond to the whole string.
     * @param start - the start of the string
     * @param end - the end of the string
     */
    protected void searchForTandemRepeat(final int start, final int end) {
        // Step 0
        final int length = MathUtils.getLength(start, end);
        if (length <= 2) { return; }
        if (!this.canDivide(start, end)) { return; }

        final int h = MathUtils.getCenter(start, end);

        // Step 1
        this.searchForTandemRepeat(start, h);
        // Step 2
        this.searchForTandemRepeat(h + 1, end);

        // Step 3
        this.searchForTandemRepeatOverCenter(start, end, Side.RIGHT);

        // Step 4
        this.searchForTandemRepeatOverCenter(start, end, Side.LEFT);
    }

    /**
     * Find all right or left Tandem Repeat (minimum length 2), which cross the center of the string.
     * @param start - the start of the string
     * @param end - the end of the string
     * @param side - search for left or right Tandem Repeats
     * @return Tandem Repeats, which cross the center of the string
     */
    protected void searchForTandemRepeatOverCenter(final int start, final int end, final Side side) {
        final int h = MathUtils.getCenter(start, end);

        for (int l = 2; l <= h - start; l++) {

            final int q = this.q(h, l, side);
            final int lceBackward = this.lceBackward(start, end, h, q);
            final int lceForward = this.lceForward(start, end, h, q);

            if (lceBackward + lceForward >= l) {
                final int startPosition;
                final int endPos;
                if (side == Side.LEFT) {
                    startPosition = q - lceBackward + 1;
                    endPos = h + lceForward;
                } else {
                    startPosition = h - lceBackward + 1;
                    endPos = q + lceForward;
                }

                if (startPosition < 0) {continue;}

                final int count = this.noRightBranched ? 1 : lceForward + lceBackward - l + 1;
                for (int i = 0; i < count; i++) {
                    this.tandemRepeats.add(new TandemRepeat(startPosition + i, endPos, lceBackward+lceForward+l, l));
                }
            }
        }
    }

    /**
     * Calculates q for searching left and right repeats
     * @param h
     * @param l
     * @param side
     * @return
     */
    protected int q(final int h, final int l, final Side side) {
        if (side == Side.LEFT) {
            return h - l;
        } else {
            return h + l;
        }
    }

    public enum Side {
        LEFT, RIGHT;

        @Override
        public String toString() {
            switch (this) {
                case LEFT:
                    return "LEFT";
                default:
                    return "RIGHT";
            }
        }
    }


    /**
     * Direction for the lowest common extension
     */
    public enum Direction {
        FORWARD, BACKWARD;


        @Override
        public String toString() {
            switch (this) {
                case FORWARD:
                    return "FORWARD";
                default:
                    return "BACKWARD";
            }
        }
    }


}

