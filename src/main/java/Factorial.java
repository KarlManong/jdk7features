import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Created by abc54_000 on 2015/3/17.
 */
public class Factorial extends RecursiveTask<Long> {
    final int start;
    final int end;
    final int THRESHOLD = 3;

    Factorial(int start, int end) {
        this.start = start;
        this.end = end;
        System.out.println("new thread from " + start + " to " + end);
    }

    @Override
    protected Long compute() {
        if (end == 100) {
            throw new RuntimeException("越界");
        }

        long sum = 1;
        if (end - start < THRESHOLD) {
            for (int i = start; i <= end; i++) {
                sum *= i;
            }
        } else {
            int middle = (start + end) / 2;
            ForkJoinTask<Long> t1 = new Factorial(start, middle);
            ForkJoinTask<Long> t2 = new Factorial(middle + 1, end);
            t1.fork();

            t2.fork();
            sum = t1.join() * t2.join();
        }
        return sum;
    }
}
