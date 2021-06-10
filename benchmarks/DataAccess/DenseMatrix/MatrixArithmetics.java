package DataAccess.DenseMatrix;

import java.util.Random;
import baseUtils.Benchmark;
import baseUtils.Annotations.DontInLine;
import baseUtils.Annotations.Trace;

public class MatrixArithmetics extends Benchmark {
    private int m, n, r, iter;
    private boolean isWrapped;
    private DenseMatrix M1;
    private DenseMatrix M2;
    private DenseMatrix M3;
    Random elementGernerator = new Random(0);

    public MatrixArithmetics(String perfArgs, int m, int n, int r, int iter, boolean isWrapped) {
        super(perfArgs);
        this.m = m;
        this.n = n;
        this.r = r;
        this.iter = iter;
        this.isWrapped = isWrapped;
    }

    @DontInLine
    @Trace(optLevel = "scorching")
    public void matMultiplyAdd() {
        for (int i = 0; i < m; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < r; k++)
                    sum += M1.get(i, k) * M2.get(j, k);
                M3.set(i, j, M3.get(i, j) * 0.4 + sum * 0.6);
            }
        }
    }

    private void changeElement() {
        M2.set(elementGernerator.nextInt(n), elementGernerator.nextInt(r),
                elementGernerator.nextDouble() + elementGernerator.nextInt(2) - 1.0);
    }

    @Override
    public void setUpUtil() {
        if (isWrapped) {
            M1 = new WrapMatrix(m, r);
            M2 = new WrapMatrix(n, r);
            M3 = new WrapMatrix(m, n);
        } else {
            M1 = new PrimitiveMatrix(m, r);
            M2 = new PrimitiveMatrix(n, r);
            M3 = new PrimitiveMatrix(m, n);
        }
        for (int i = 0; i < m; i++)
            for (int j = 0; j < r; j++)
                M1.set(i, j, elementGernerator.nextDouble() + elementGernerator.nextInt(2) - 1.0);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < r; j++)
                M2.set(i, j, elementGernerator.nextDouble() + elementGernerator.nextInt(2) - 1.0);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                M3.set(i, j, 0);
    }

    @Override
    public void run() {
        for (int i = 0; i < iter; i++) {
            matMultiplyAdd();
            changeElement();
        }
    }

    @Override
    public void endUtil() {
        double av = 0;
        double t = m * n;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                av += M3.get(i, j) / t;
        System.out.println(av);
    }
}
