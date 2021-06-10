package DataAccess.SearchTree;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import baseUtils.Benchmark;
import baseUtils.Annotations.DontInLine;
import baseUtils.Annotations.Trace;
import DataAccess.SearchTree.Tree.TreeBuilder;

// DFS in a 16-ary tree, expects a large amount of dereferencing
public class TreeSearch extends Benchmark {

    private int treeHeight;
    private Tree tree;
    private TreeBuilder treeBuilder = new TreeBuilder();
    // number of runs
    private int iter1;
    // number of search keys in each run
    private int iter2;
    private int numThreads;
    // Key range of the tree is 2^(4*treeHieght)-1, changed node key range is (0,
    // changeElementKeyRange) so larger changeElementKeyRange value indicates more smaller subtrees
    // are possible to be changed after one run
    private long changeElementKeyRange;
    private ThreadPoolExecutor[] pool;
    private Random random = new Random(0);
    double validateResult = 0.0;

    public TreeSearch(String perfArgs, int treeHeight, int iter1, int iter2, int numThreads,
            long changeElementKeyRange) {
        super(perfArgs);
        this.treeHeight = treeHeight;
        this.iter1 = iter1;
        this.iter2 = iter2;
        this.numThreads = numThreads;
        this.changeElementKeyRange = changeElementKeyRange;
        this.pool = new ThreadPoolExecutor[numThreads];
        for (int i = 0; i < numThreads; i++)
            pool[i] = (ThreadPoolExecutor) Executors.newFixedThreadPool(1, threadFactory);
    }

    @Override
    public void setUpUtil() {
        tree = treeBuilder.build(treeHeight);
    }

    // dfs the full 16-ary tree
    @DontInLine
    @Trace(optLevel = "scorching")
    private TreeNode dfs(long key, TreeNode node) {
        if (node.getKey() == key)
            return node;
        else {
            if (node.isLeaf())
                return null;
            else {
                TreeNode resultTreeNode = null;
                for (TreeNode currentChildNode =
                        node.getChild(0); currentChildNode != null; currentChildNode =
                                currentChildNode.getNextSibling()) {
                    resultTreeNode = dfs(key, currentChildNode);
                    if (resultTreeNode != null)
                        return resultTreeNode;
                }
                return null;
            }
        }
    }

    class Task implements Callable<Double> {

        Random random = new Random(0);

        @Override
        public Double call() throws Exception {
            long keyRange = tree.getNumberNodes();
            long key;
            int hits = 0;
            for (int i = 0; i < iter2; i++) {
                key = Math.abs(random.nextLong() % keyRange);
                TreeNode result = dfs(key, tree.getRoot());
                if (result != null)
                    hits++;
            }
            return hits / (iter2 + 0.0);
        }

    }

    @Override
    @SuppressWarnings("rawtypes")
    public void run() {
        Future[] futures = new Future[numThreads];
        for (int i = 0; i < iter1; i++) {
            validateResult = 0.0;
            for (int j = 0; j < numThreads; j++) {
                futures[j] = pool[j].submit(new Task());
            }
            for (int j = 0; j < numThreads; j++) {
                try {
                    double result = (double) futures[j].get();
                    validateResult += result / numThreads;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            changeElements();
        }
    }

    // Replace subtree with a new copy of the tree. Trigger GC
    private void changeElements() {
        long rootKey = Math.abs(random.nextLong() % changeElementKeyRange);
        TreeNode replacedTargeParentTreeNode = dfs(rootKey, tree.getRoot());
        int index = random.nextInt(TreeNode.maxChildren);
        replacedTargeParentTreeNode.setChild(index,
                treeBuilder.cloneSubTree(replacedTargeParentTreeNode.getChild(index)));
    }

    @Override
    public void endUtil() {
        for (ThreadPoolExecutor threadPoolExecutor : pool) {
            threadPoolExecutor.shutdown();
        }
        System.out.println("validation result is: " + validateResult);
    }

}
