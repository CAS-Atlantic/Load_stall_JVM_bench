package DataAccess.SparseMatrix;

import java.util.Random;
import baseUtils.Benchmark;
import baseUtils.Annotations.DontInLine;
import baseUtils.Annotations.Trace;

public class SparseMatrixArithmetics extends Benchmark {

    private int m, n, r, iter;
    private SparseMatrix M1 = new SparseMatrix();
    private SparseMatrix M2 = new SparseMatrix();
    private double result = 0.0;
    Random elementGenerator = new Random(0);

    // traversing the linked list to get elements
    @DontInLine
    @Trace(optLevel = "scorching")
    public void matMultiplyAdd2() {
        for (Node<Row> it1 = M1.getHead(); it1 != null; it1 = it1.getNext()) {
            Row r1 = it1.getValue();
            for (Node<Row> it2 = M2.getHead(); it2 != null; it2 = it2.getNext()) {
                Row r2 = it2.getValue();
                double sum = 0;

                // faster algorithm to calculate the result otherwise the benchmark runs too long

                Node<Pair> n1 = r1.getHead();
                Node<Pair> n2 = r2.getHead();
                while (n1 != null && n2 != null) {
                    Pair p1 = n1.getValue();
                    Pair p2 = n2.getValue();
                    if (p1.col == p2.col) {
                        sum += (p1.value * 0.2) + (p2.value * 0.125);
                        n1 = n1.getNext();
                        n2 = n2.getNext();
                    } else if (p1.col < p2.col)
                        n1 = n1.getNext();
                    else
                        n2 = n2.getNext();
                }
                if (sum != 0) {
                    result = result * 0.6 + sum * 0.4;
                }
            }
        }
    }

    private void changeElement() {
        int r = elementGenerator.nextInt(n);
        int c = elementGenerator.nextInt(r);
        double value = elementGenerator.nextDouble() + elementGenerator.nextInt(2) - 1.0;
        M2.setElement(r, c, value);
    }

    public SparseMatrixArithmetics(String perfArgs, int m, int n, int r, int iter) {
        super(perfArgs);
        this.m = m;
        this.n = n;
        this.r = r;
        this.iter = iter;
    }

    @Override
    public void setUpUtil() {
        for (int i = 0; i < m; i++)
            for (int j = 0; j < r; j++)
                if (elementGenerator.nextInt() % 2 == 1) {
                    double value =
                            elementGenerator.nextDouble() + elementGenerator.nextInt(2) - 1.0;
                    M1.setElement(i, j, value);
                }
        for (int i = 0; i < n; i++)
            for (int j = 0; j < r; j++)
                if (elementGenerator.nextInt() % 2 == 1) {
                    double value =
                            elementGenerator.nextDouble() + elementGenerator.nextInt(2) - 1.0;
                    M2.setElement(i, j, value);
                }
    }

    @Override
    public void run() {
        for (int i = 0; i < iter; i++) {
            matMultiplyAdd2();
            changeElement();
        }
    }


    @Override
    public void endUtil() {
        System.out.println("validation result is: " + result);
    }

}
