import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

interface Counter {
    void increment();
    int getCount();
}

class LockCounter implements Counter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}

class AtomicCounter implements Counter {
    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void increment() {
        count.incrementAndGet();
    }

    @Override
    public int getCount() {
        return count.get();
    }
}

public class CounterTestHarness {
    private static final int NUM_THREADS = 4;
    private static final int INCREMENTS_PER_THREAD = 100000;
    private static final int EXPECTED_TOTAL = NUM_THREADS * INCREMENTS_PER_THREAD;

    public static void main(String[] args) throws InterruptedException {
        testCounter("LockCounter", new LockCounter());
        testCounter("AtomicCounter", new AtomicCounter());
    }

    private static void testCounter(String name, Counter counter) throws InterruptedException {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    counter.increment();
                }
            });
        }

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
            System.err.println(name + " tasks did not finish in time.");
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000; 

        System.out.println("--- " + name + " Results ---");
        System.out.println("Final Count: " + counter.getCount());
        System.out.println("Expected Count: " + EXPECTED_TOTAL);
        System.out.println("Execution Time: " + duration + " ms");
        System.out.println("Count is correct: " + (counter.getCount() == EXPECTED_TOTAL));
        System.out.println();
    }
}