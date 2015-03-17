import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by abc54_000 on 2015/3/17.
 */
public class ForkJoinTest {

    @Test
    public void testSort() throws Exception {
        Random rand = new Random();

        long[] array = new long[16];

        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextLong() % 100;
        }
        System.out.println("Initial Array: " + Arrays.toString(array));

        ForkJoinTask sort = new SortTask(array);
        ForkJoinPool fjpool = new ForkJoinPool();
        fjpool.submit(sort);
        fjpool.shutdown();

        fjpool.awaitTermination(30, TimeUnit.SECONDS);

        assertTrue(checkSorted(array));
    }

    boolean checkSorted(long[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > (a[i + 1])) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testFactorial() throws InterruptedException, ExecutionException {
        ForkJoinTask<Long> fjt = new Factorial(1, 10);

        ForkJoinPool fjpool = new ForkJoinPool();
        Future<Long> result = fjpool.submit(fjt);

        // do something
        System.out.println(result.get());

    }

    @Test
    public void testException(){
        ForkJoinTask<Long> fjt = new Factorial(1, 100);

        ForkJoinPool fjpool = new ForkJoinPool();
        Future<Long> result = fjpool.submit(fjt);

        // do something
        try {
            System.out.println(result.get());
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }

    }
}
