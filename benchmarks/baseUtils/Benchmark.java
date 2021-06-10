package baseUtils;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public abstract class Benchmark {

    // thread factory must be modified otherwise the names of threads are too long for perf
    static public final ThreadFactory threadFactory = new ThreadFactory() {

        ThreadFactory sourceFactory = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(Runnable r) {
            Thread newThread = sourceFactory.newThread(r);
            newThread.setName(newThread.getName().substring(7));
            return newThread;
        }
    };

    private String perfArgs;
    private Process perfProcess;

    public Benchmark(String perfArgs) {
        this.perfArgs = perfArgs;
    }

    // for setting up the environment before running the benchmark. No perf at this stage.
    public abstract void setUpUtil();

    protected void startProfile() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        String pid = processName.substring(0, processName.indexOf('@'));
        long perf_pid = -1;

        // use reflection to get the pid of the perf process, hacking is necessart since pid is not
        // exposed to users
        try {
            ProcessBuilder processBuilder =
                    perfArgs.equals("") ? new ProcessBuilder("perf", "record", "-p", pid)
                            : new ProcessBuilder("perf", "record", perfArgs, "-p", pid);
            processBuilder.inheritIO();
            perfProcess = processBuilder.start();
            Field pidField = perfProcess.getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            perf_pid = pidField.getLong(perfProcess);
            pidField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("perf at " + perf_pid + " is already started on " + pid);
    }

    public void setup() {
        setUpUtil();
        startProfile();
    }

    // running the benchmark
    public abstract void run();

    // validation and other operations after perf is stopped
    public abstract void endUtil();

    protected void stopProfile() {
        try {
            Field pidField = perfProcess.getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            long pid = pidField.getLong(perfProcess);
            pidField.setAccessible(false);

            // kill the perf process from command line
            Runtime.getRuntime().exec("kill " + pid);
            System.out.println("perf at " + pid + " is already ended");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void end() {
        stopProfile();
        endUtil();
    }

}
