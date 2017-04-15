import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.*;


/**
 * Abstract parent class for all variants of searching for approximate tandem repeats.
 */
abstract public class ApproximateTandemRepeatSearcher extends TandemRepeatSearcher {

    final int errors;

    public ApproximateTandemRepeatSearcher(final String input, final int minLength, final boolean noRightBranched, final int errors) {
        super(input, minLength, noRightBranched);
        this.errors = errors;
    }

    @Override
    protected boolean canDivide(final int start, final int end) {
        final int min = this.minLength > this.errors ?  this.minLength*2 : this.errors*2 + 2;
        return MathUtils.getLength(start, end) >= min;
    }

    @Override
    protected void searchForTandemRepeatOverCenter(final int start, final int end, final Side side) {
        final int h = MathUtils.getCenter(start, end);

        final TreeSet<Integer> errors = new TreeSet<>();
        final TreeSet<Integer> errorsForward = new TreeSet<>();

        for (int l = this.errors + 2; l <= h - start; l++) {

            final int q = this.q(h, l, side);

            final int lceBackward = this.lce(start, end, h, q, errors, Direction.BACKWARD);
            final int lceForward = this.lce(start, end, h, q, errorsForward, Direction.FORWARD);

            errors.addAll(errorsForward);

            if (lceBackward + lceForward >= l) {
                final int startPosition;
                if (side == Side.LEFT) {
                    startPosition = q - lceBackward + 1;
                } else {
                    startPosition = h - lceBackward + 1;
                }
                if (startPosition < 0) {continue;}
                int endPosition = startPosition + l - 1;

                final int count = lceBackward + lceForward - l + 1;
                int numberOfErrors = 0;

                Iterator<Integer> firstErrorIterator = errors.iterator();
                Iterator<Integer> nextErrorIterator = errors.iterator();

                int first = -1;
                if (firstErrorIterator.hasNext()) {
                    first = firstErrorIterator.next();
                }

                int next = -1;
                while (nextErrorIterator.hasNext()) {
                    int error = nextErrorIterator.next();
                    if (endPosition < error) {
                        next = error;
                        break;
                    }
                    if (error >= startPosition && error <= endPosition) {
                        numberOfErrors++;
                    }
                }

                for (int i = 0; i < count; i++) {
                    final int startOfTandemRepeat = startPosition + i;
                    endPosition = startOfTandemRepeat + l - 1;


                    if (first != -1 && first < startOfTandemRepeat) {
                        if (firstErrorIterator.hasNext()) {
                            first = firstErrorIterator.next();
                        }
                        numberOfErrors--;
                    }

                    if (next != -1 && next >= startOfTandemRepeat && next <= endPosition) {
                        if (nextErrorIterator.hasNext()) {
                            next = nextErrorIterator.next();
                        }
                        numberOfErrors++;
                    }

                    if (numberOfErrors > this.errors) { continue; }
                    this.tandemRepeats.add(new TandemRepeat(startOfTandemRepeat, endPosition, lceBackward+lceForward+l, l));
                    if (this.noRightBranched) { break; }
                }
            }
        }
    }

    // Helper
    private int lce(final int start, final int end, int h, int q, TreeSet<Integer> errors, Direction direction) {
        int lce = 0;
        errors.clear();
        for (int k = 0; k < this.errors + 1; k++) {

            if (h < 0 || h > end) { break; }
            if (q < 0 || q > end) { break; }

            if (k != 0) {
                lce++;
                errors.add(Math.min(h, q));

                if (direction == Direction.BACKWARD) {
                    h--;
                    q--;
                }
            }

            final int tmpLCE;
            if (direction == Direction.FORWARD) {
                tmpLCE = this.lceForward(start, end, h, q);
                h += tmpLCE + 1;
                q += tmpLCE + 1;
            } else {
                tmpLCE = this.lceBackward(start, end, h, q);

                h -= tmpLCE;
                q -= tmpLCE;
            }

            lce += tmpLCE;
        }
        return lce;
    }
}