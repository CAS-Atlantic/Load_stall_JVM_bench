package ObjectLayout;

import baseUtils.Benchmark;
import baseUtils.Annotations.DontInLine;
import baseUtils.Annotations.Trace;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


class DummyBigObject {

    private long f0;
    private long f1;
    private long f2;
    private long f3;
    private long f4;
    private long f5;
    private long f6;
    private long f7;

    private long f10;
    private long f11;
    private long f12;
    private long f13;
    private long f14;
    private long f15;
    private long f16;
    private long f17;

    private long f20;
    private long f21;
    private long f22;
    private long f23;
    private long f24;
    private long f25;
    private long f26;
    private long f27;

    private long f30;
    private long f31;
    private long f32;
    private long f33;
    private long f34;
    private long f35;
    private long f36;
    private long f37;

    private long f40;
    private long f41;
    private long f42;
    private long f43;
    private long f44;
    private long f45;
    private long f46;
    private long f47;

    public DummyBigObject(Random random) {

        f0 = random.nextLong();
        f1 = random.nextLong();
        f2 = random.nextLong();
        f3 = random.nextLong();
        f4 = random.nextLong();
        f5 = random.nextLong();
        f6 = random.nextLong();
        f7 = random.nextLong();

        f10 = random.nextLong();
        f11 = random.nextLong();
        f12 = random.nextLong();
        f13 = random.nextLong();
        f14 = random.nextLong();
        f15 = random.nextLong();
        f16 = random.nextLong();
        f17 = random.nextLong();

        f20 = random.nextLong();
        f21 = random.nextLong();
        f22 = random.nextLong();
        f23 = random.nextLong();
        f24 = random.nextLong();
        f25 = random.nextLong();
        f26 = random.nextLong();
        f27 = random.nextLong();

        f30 = random.nextLong();
        f31 = random.nextLong();
        f32 = random.nextLong();
        f33 = random.nextLong();
        f34 = random.nextLong();
        f35 = random.nextLong();
        f36 = random.nextLong();
        f37 = random.nextLong();

        f40 = random.nextLong();
        f41 = random.nextLong();
        f42 = random.nextLong();
        f43 = random.nextLong();
        f44 = random.nextLong();
        f45 = random.nextLong();
        f46 = random.nextLong();
        f47 = random.nextLong();
    }

    @DontInLine
    @Trace(optLevel = "scorching")
    public long handle1() {
        long res = 0;
        res += (this.f46 | 1048575);
        res *= (this.f11 ^ 1048575);
        for (int i = 0; i < 3; i++) {
            res += (++this.f22 | 1048575);
            res *= (--this.f33 ^ 1048575);
            res ^= (++this.f44 | 1048575);
        }
        res += (this.f10 | 1048575);
        res *= (this.f21 ^ 1048575);
        res ^= (this.f35 | 1048575);
        res += (this.f42 ^ 1048575);
        res *= (this.f12 ^ 1048575);
        return res | 1048575;
    }

    @DontInLine
    @Trace(optLevel = "scorching")
    public long handle2() {
        long res = 255;
        for (int i = 0; i < 3; i++) {
            res += (--this.f46 | 1048575);
            res *= (++this.f31 ^ 1048575);
            res ^= (--this.f20 | 1048575);
        }
        res += (this.f43 | 1048575);
        res *= (this.f13 ^ 1048575);
        res ^= (this.f34 | 1048575);
        res += (this.f23 ^ 1048575);
        res *= (this.f35 ^ 1048575);
        res ^= (this.f42 | 1048575);
        res += (this.f12 | 1048575);
        return res | 1048575;
    }

    public long handle3() {
        return (f0 ^ f1 ^ f2 ^ f3 ^ f4 ^ f5 ^ f6 ^ f7)
                & (f10 ^ f11 ^ f12 ^ f13 ^ f14 ^ f15 ^ f16 ^ f17)
                & (f20 ^ f21 ^ f22 ^ f23 ^ f24 ^ f25 ^ f26 ^ f27)
                & (f30 ^ f31 ^ f32 ^ f33 ^ f34 ^ f35 ^ f36 ^ f37)
                & (f40 ^ f41 ^ f42 ^ f43 ^ f44 ^ f45 ^ f46 ^ f47);
    }
}


public class HotFieldsInObject extends Benchmark {

    // array size
    int size;
    // number of iterations in each run
    int iter;
    // proportion of elements in the array to be updated (with new instance) after each run
    double changeFactor;
    // seeded random number generator for consistency over different runs of the benchmark
    Random random = new Random(0);
    DummyBigObject[] array;
    ThreadPoolExecutor[] pool;
    static final int threadNum = 2;
    // hold the results for validation and ensuring the benchmark is actually run
    long[] results;

    public HotFieldsInObject(String perfArgs, int size, int iter, double cFactor) {
        super(perfArgs);
        this.size = size;
        this.iter = iter;
        this.changeFactor = cFactor;
        this.array = new DummyBigObject[size];
        this.results = new long[threadNum];

        this.pool = new ThreadPoolExecutor[threadNum];
        for (int i = 0; i < threadNum; i++)
            pool[i] = (ThreadPoolExecutor) Executors.newFixedThreadPool(1, threadFactory);
    }

    @Override
    public void setUpUtil() {
        for (int i = 0; i < size; i++)
            array[i] = new DummyBigObject(random);
    }

    class Task1 implements Callable<Long> {

        @Override
        public Long call() throws Exception {
            long result = 0;
            for (DummyBigObject obj : array)
                result += obj.handle1();
            return result;
        }
    }

    class Task2 implements Callable<Long> {

        @Override
        public Long call() throws Exception {
            long result = 0;
            for (DummyBigObject obj : array)
                result += obj.handle2();
            return result;
        }


    }

    private void changeElements() {
        int cSize = (int) (size * changeFactor);
        for (int i = 0; i < cSize; i++) {
            int index = random.nextInt(size);
            array[index] = new DummyBigObject(random);
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < iter; i++) {
            Future<Long> future1 = pool[0].submit(new Task1());
            Future<Long> future2 = pool[1].submit(new Task2());
            try {
                results[0] |= future1.get();
                results[1] |= future2.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            changeElements();
        }
    }

    @Override
    public void endUtil() {
        for (ThreadPoolExecutor threadPoolExecutor : pool)
            threadPoolExecutor.shutdown();
        long result = 0;
        for (long r : results)
            result += r;
        System.out.println("validation result: " + result);
    }

}
