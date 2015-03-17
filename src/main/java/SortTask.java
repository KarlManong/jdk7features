import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

/**
 * Created by abc54_000 on 2015/3/17.
 */
public class SortTask extends RecursiveAction {
    final long[] array;
    final int lo, hi;

    SortTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    SortTask(long[] array) {
        this(array, 0, array.length);
    }

    protected void compute() {
        if (hi - lo < THRESHOLD) {
            sortSequentially(lo, hi);
            System.out.println("sort:" + Arrays.toString(array));
        } else {
            int mid = (lo + hi) >>> 1;
            System.out.println("mid = " + mid + ", low = " + lo + ", high = " + hi);
            System.out.println("split:" + Arrays.toString(array));
            invokeAll(new SortTask(array, lo, mid),
                    new SortTask(array, mid, hi));
            merge(lo, mid, hi);
        }
    }

    static final int THRESHOLD = 4;

    void sortSequentially(int lo, int hi) {
        Arrays.sort(array, lo, hi);
    }

    void merge(int lo, int mid, int hi) {
        long[] buf = Arrays.copyOfRange(array, lo, mid);
        for (int i = 0, j = lo, k = mid; i < buf.length; j++)
            array[j] = (k == hi || buf[i] < array[k]) ?
                    buf[i++] : array[k++];
    }
}