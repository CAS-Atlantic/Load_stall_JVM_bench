package InterfaceChecking;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import baseUtils.Benchmark;
import baseUtils.Annotations.DontInLine;
import baseUtils.Annotations.Trace;

/*
 * the relationship between interfaces and the implementation of interfaces are variations. There
 * might be a lot of boilerplate code. Source code generator such as JavaPoet can be considered. The
 * implementations of interface methods do not matter as long as they do not interfere the
 * experiment.
 */

import InterfaceChecking.dummyInterfaces.*;
import InterfaceChecking.dummyClasses.*;

public class InterfaceLayout extends Benchmark {

    int size;
    int iter;
    double changeFactor;

    /*
     * proportion1, 2 and 3 control the frequencies with which each interface implementation is
     * utilized. For instance, (proportion1=0.4, proportion2=0.3, proportion3=0.2) indicates 40% of
     * the elements in object array view1 are instances of DummyClass1, 30% are instances of
     * DummyClass4, 20% are instances of DummyClass3 and the rest are instances of DummyClass2. For
     * DummyClass1, 40% of its instances are utilized as implementation of DummyInterface1, 30% as
     * DummyInterface2, 20% as DummyInterface3 and the rest as DummyInterface4.
     */
    double proportion1;
    double proportion2;
    double proportion3;
    Object[] view1;
    Object[] view2;
    Object[] view3;
    Object[] view4;
    DummyClass1[] arr1;
    DummyClass2[] arr2;
    DummyClass3[] arr3;
    DummyClass4[] arr4;
    ThreadPoolExecutor[] pool;
    Random random = new Random(0);
    int[] results;

    static final int threadNum = 4;

    public InterfaceLayout(String perfArgs, int size, int iter, double cFactor, double p1,
            double p2, double p3) {
        super(perfArgs);
        this.size = size;
        this.iter = iter;
        this.changeFactor = cFactor;
        this.proportion1 = p1;
        this.proportion2 = p2;
        this.proportion3 = p3;
        this.arr1 = new DummyClass1[size];
        this.arr2 = new DummyClass2[size];
        this.arr3 = new DummyClass3[size];
        this.arr4 = new DummyClass4[size];
        this.view1 = new Object[size];
        this.view2 = new Object[size];
        this.view3 = new Object[size];
        this.view4 = new Object[size];

        this.results = new int[threadNum];
        for (int i = 0; i < threadNum; i++)
            results[i] = 0;

        this.pool = new ThreadPoolExecutor[threadNum];
        for (int i = 0; i < threadNum; i++)
            pool[i] = (ThreadPoolExecutor) Executors.newFixedThreadPool(1, threadFactory);
    }

    // Fisher-Yates shuffle
    private void shuffle(Object[] arr) {
        Random random = new Random(0);
        int index = 0;
        Object tempDummy;
        for (int i = arr.length - 1; i >= 0; i--) {
            index = random.nextInt(i + 1);
            tempDummy = arr[i];
            arr[i] = arr[index];
            arr[index] = tempDummy;
        }
    }

    @Override
    public void setUpUtil() {
        for (int i = 0; i < size; i++) {
            arr1[i] = new DummyClass1(random.nextInt(16), random.nextInt(16), random.nextInt(16),
                    random.nextInt(16), random.nextInt(16));
            arr2[i] = new DummyClass2(random.nextInt(16), random.nextInt(16), random.nextInt(16),
                    random.nextInt(16), random.nextInt(16));
            arr3[i] = new DummyClass3(random.nextInt(16), random.nextInt(16), random.nextInt(16),
                    random.nextInt(16), random.nextInt(16));
            arr4[i] = new DummyClass4(random.nextInt(16), random.nextInt(16), random.nextInt(16),
                    random.nextInt(16), random.nextInt(16));
        }

        // distribute the objects to interfaceviews
        int i = 0;
        int size1 = (int) (size * proportion1);
        int size2 = (int) (size * (proportion1 + proportion2));
        int size3 = (int) (size * (proportion1 + proportion2 + proportion3));
        for (; i < size1; i++) {
            view1[i] = arr1[i];
            view2[i] = arr2[i];
            view3[i] = arr3[i];
            view4[i] = arr4[i];
        }
        for (; i < size2; i++) {
            view1[i] = arr4[i];
            view2[i] = arr1[i];
            view3[i] = arr2[i];
            view4[i] = arr3[i];
        }
        for (; i < size3; i++) {
            view1[i] = arr3[i];
            view2[i] = arr4[i];
            view3[i] = arr1[i];
            view4[i] = arr2[i];
        }
        for (; i < size; i++) {
            view1[i] = arr2[i];
            view2[i] = arr3[i];
            view3[i] = arr4[i];
            view4[i] = arr1[i];
        }
        shuffle(view1);
        shuffle(view2);
        shuffle(view3);
        shuffle(view4);
    }

    // change elements to avoid possible "over-smart" of the compiler, maybe creating new objects
    // should be tried rather than just setting the arguments
    private void changeElements() {
        int cSize = (int) (size * changeFactor);
        for (int i = 0; i < cSize; i++) {
            int index = random.nextInt(size);
            arr1[index].setArg1(random.nextInt(16));
            arr1[index].setArg2(random.nextInt(16));
            arr1[index].setArg3(random.nextInt(16));
            arr1[index].setArg4(random.nextInt(16));

            int index2 = random.nextInt(size);
            arr2[index2].setArg1(random.nextInt(16));
            arr2[index2].setArg2(random.nextInt(16));
            arr2[index2].setArg3(random.nextInt(16));
            arr2[index2].setArg4(random.nextInt(16));

            int index3 = random.nextInt(size);
            arr3[index3].setArg1(random.nextInt(16));
            arr3[index3].setArg2(random.nextInt(16));
            arr3[index3].setArg3(random.nextInt(16));
            arr3[index3].setArg4(random.nextInt(16));

            int index4 = random.nextInt(size);
            arr4[index4].setArg1(random.nextInt(16));
            arr4[index4].setArg2(random.nextInt(16));
            arr4[index4].setArg3(random.nextInt(16));
            arr4[index4].setArg4(random.nextInt(16));
        }
    }

    class Task1 implements Callable<Integer> {

        @DontInLine
        @Trace(optLevel = "scorching")
        @Override
        public Integer call() throws Exception {
            int result = 0;
            for (Object itf : view1)
                result += ((DummyInterface1) itf).DummyMethod1();
            return result;
        }
    }

    class Task2 implements Callable<Integer> {

        @DontInLine
        @Trace(optLevel = "scorching")
        @Override
        public Integer call() throws Exception {
            int result = 0;
            for (Object itf : view2)
                result += ((DummyInterface2) itf).DummyMethod2();
            return result;
        }


    }

    class Task3 implements Callable<Integer> {

        @DontInLine
        @Trace(optLevel = "scorching")
        @Override
        public Integer call() throws Exception {
            int result = 0;
            for (Object itf : view3)
                result += ((DummyInterface3) itf).DummyMethod3();
            return result;
        }

    }

    class Task4 implements Callable<Integer> {

        @DontInLine
        @Trace(optLevel = "scorching")
        @Override
        public Integer call() throws Exception {
            int result = 0;
            for (Object itf : view4)
                result += ((DummyInterface4) itf).DummyMethod4();
            return result;
        }

    }

    @Override
    public void run() {
        for (int i = 0; i < iter; i++) {
            Future<Integer> future1 = pool[0].submit(new Task1());
            Future<Integer> future2 = pool[1].submit(new Task2());
            Future<Integer> future3 = pool[2].submit(new Task3());
            Future<Integer> future4 = pool[3].submit(new Task4());
            try {
                results[0] = future1.get();
                results[1] = future2.get();
                results[2] = future3.get();
                results[3] = future4.get();
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
        int result = 0;
        for (int r : results)
            result += r;
        System.out.println("validation result: " + result);
    }

}
