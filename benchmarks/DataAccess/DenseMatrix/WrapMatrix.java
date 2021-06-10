package DataAccess.DenseMatrix;

import java.util.ArrayList;

// values of the matrix is wrapped in an immutable Double object
public class WrapMatrix implements DenseMatrix {

    private ArrayList<Double[]> values;

    public WrapMatrix(int r, int c) {
        values = new ArrayList<Double[]>();
        for (int i = 0; i < r; i++)
            values.add(new Double[c]);
    }

    @Override
    public double get(int i, int j) {
        return values.get(i)[j];
    }

    @Override
    public void set(int i, int j, double v) {
        Double wv = Double.valueOf(v);
        values.get(i)[j] = wv;
    }

}
