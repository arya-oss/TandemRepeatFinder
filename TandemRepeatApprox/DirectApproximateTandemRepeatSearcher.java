
/**
 * Implementation based on the algorithm of Main and Lorentz (1984).
 * This will find all approximate single repeats within a string.
 */
public class DirectApproximateTandemRepeatSearcher extends  ApproximateTandemRepeatSearcher {


    public DirectApproximateTandemRepeatSearcher(final String input, final int minLength, final int errors) {
        this(input, minLength, false, errors);
    }

    public DirectApproximateTandemRepeatSearcher(String input, int minLength, boolean noRightBranched, int errors) {
        super(input, minLength, noRightBranched, errors);
    }

    @Override
    protected int lceForward(final int start, final int end, final int h, final int q) {
        return LCE.forward(this.input, start, end, h + 1, q + 1);
    }

    @Override
    protected int lceBackward(final int start, final int end, final int h, final int q) {
        return LCE.backward(this.input, start, end, h, q);
    }
}
