package DataAccess.DenseMatrix;

// matrix elemtents are primitive double values
public class PrimitiveMatrix implements DenseMatrix {

    private double[][] values;

    public PrimitiveMatrix(int r, int c) {
        super();
        values = new double[r][c];
    }

    @Override
    public double get(int i, int j) {
        return values[i][j];
    }

    @Override
    public void set(int i, int j, double v) {
        values[i][j] = v;
    }

}
