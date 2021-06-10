package DataAccess.SparseMatrix;

import java.util.Optional;

class Pair implements Comparable<Pair> {
    public int col;
    public double value;

    public Pair(int col, double value) {
        this.col = col;
        this.value = value;
    }

    @Override
    public int compareTo(Pair o) {
        if (col == o.col)
            return 0;
        else if (col < o.col)
            return -1;
        return 1;
    }
}


class Row implements Comparable<Row> {

    public final int rowNum;

    private final List<Pair> elements;

    public Row(int rowNum, int headCol, double headValue) {
        this.rowNum = rowNum;
        this.elements = new List<Pair>(new Pair(headCol, headValue));
    }

    public Optional<Pair> getElement(int col) {
        return elements.get(new Pair(col, 0));
    }

    public void setElement(int col, double value) {
        Optional<Pair> p = getElement(col);
        if (p.isPresent()) {
            p.get().value = value;
        } else {
            elements.add(new Pair(col, value));
        }
    }

    public Node<Pair> getHead() {
        return elements.getHead();
    }

    @Override
    public int compareTo(Row o) {
        if (rowNum == o.rowNum)
            return 0;
        else if (rowNum < o.rowNum)
            return -1;
        return 1;
    }
}


public class SparseMatrix {

    private List<Row> mat;

    public SparseMatrix() {
        mat = new List<Row>();
    }

    public Optional<Row> getRow(int i) {
        return mat.get(new Row(i, 0, 0));
    }

    public void setElement(int r, int c, double value) {
        Optional<Row> or = getRow(r);
        if (or.isPresent())
            or.get().setElement(c, value);
        else {
            mat.add(new Row(r, c, value));
        }
    }

    public double getElement(int r, int c) {
        Optional<Row> or = getRow(r);
        if (!or.isPresent())
            return 0;
        else {
            Optional<Pair> p = or.get().getElement(c);
            return p.isPresent() ? p.get().value : 0;
        }
    }

    public Node<Row> getHead() {
        return mat.getHead();
    }
}
