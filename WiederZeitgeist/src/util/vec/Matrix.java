/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.vec;

/**
 * An immutable linear algebra matrix. Has methods which facilitate with general linear algebra as well as with graphics.
 * @author Kosmic
 */
public class Matrix {
    
    /**
     * Create the identity matrix.
     * @param dim The dimension of the square identity matrix.
     * @return The square identity matrix of dimension dim.
     */
    public static Matrix i(int dim) {
        Matrix m = new Matrix(dim, dim);
        for (int i = 0; i < dim; i++) {
            m.mat[dim][dim] = 1;
        }
        return m;
    }
    
    /**
     * Creates a new projective matrix for 2D graphics. The output matrix is a square 3x3 matrix.
     * @param left Left value.
     * @param right Right value.
     * @param bottom Bottom value.
     * @param top Top value.
     * @return The 3x3 matrix representing the projection matrix for 2D graphics.
     */
    public static Matrix projective2d(double left, double right, double bottom, double top) {
        Matrix m = new Matrix(3, 3);
        m.mat[0][0] = 2 / (right - left);
        m.mat[1][1] = 2 / (top - bottom);
        m.mat[0][2] = -((right + left) / (right - left));
        m.mat[1][2] = -((top + bottom) / (top - bottom));
        m.mat[2][2] = 1;
        return m;
    }
    
    /**
     * Creates a new projective matrix for 3D graphics. The output matrix is a square 4x4 matrix.
     * @param left Left value.
     * @param right Right value.
     * @param bottom Bottom value.
     * @param top Top value.
     * @param fore Forward value.
     * @param back Back value.
     * @return The 4x4 matrix representing the projection matrix for 3D graphics.
     */
    public static Matrix projective3d(double left, double right, double bottom, double top, double fore, double back) {
        Matrix m = new Matrix(4, 4);
        m.mat[0][0] = 2 / (right - left);
        m.mat[1][1] = 2 / (top - bottom);
        m.mat[2][2] = 2 / (fore - back);
        m.mat[0][3] = -((right + left) / (right - left));
        m.mat[1][3] = -((top + bottom) / (top - bottom));
        m.mat[2][3] = -((fore + back) / (fore - back));
        m.mat[3][3] = 1;
        return m;
    }
    
    /**
     * Creates a 2D world matrix for 2D graphics.
     * @param position The position.
     * @param unitDimensions The units.
     * @return The 3x3 matrix representing the 2D world matrix.
     */
    public static Matrix world2d(Vector position, Vector unitDimensions) {
        Matrix m = new Matrix(3, 3);
        position.assertSize(2);
        unitDimensions.assertSize(2);
        m.mat[0][0] = unitDimensions.x();
        m.mat[1][1] = unitDimensions.y();
        m.mat[0][2] = position.x() * unitDimensions.x();
        m.mat[1][2] = position.y() * unitDimensions.y();
        m.mat[2][2] = 1;
        return m;
    }
    
    /**
     * Creates a 3D world matrix for 3D graphics.
     * @param position The position.
     * @param unitDimensions The units.
     * @return The 4x4 matrix representing the 3D world matrix.
     */
    public static Matrix world3d(Vector position, Vector unitDimensions) {
        Matrix m = new Matrix(4, 4);
        position.assertSize(3);
        unitDimensions.assertSize(3);
        m.mat[0][0] = unitDimensions.x();
        m.mat[1][1] = unitDimensions.y();
        m.mat[2][2] = unitDimensions.z();
        m.mat[0][3] = position.x() * unitDimensions.x();
        m.mat[1][3] = position.y() * unitDimensions.y();
        m.mat[2][3] = position.z() * unitDimensions.z();
        m.mat[3][3] = 1;
        return m;
    }
    
    /**
     * The x dimension of the matrix.
     */
    public final int dimX;

    /**
     * The y dimension of the matrix.
     */
    public final int dimY;
    private final double[][] mat;

    /**
     * Creates a new matrix from a 2D array. The dimY of the resulting matrix is the same as m.length, unless m[0].length == 0.
     * @param m The array matrix.
     */
    public Matrix(double[][] m) {
        if (m.length == 0 || m[0].length == 0) {
            dimY = 0;
            dimX = 0;
            mat = new double[0][0];
        } else {
            dimY = m.length;
            dimX = m[0].length;
            mat = new double[dimY][dimX];
            for (int i = 0; i < dimY; i++) {
                if (m[i].length != dimX) {
                    throw new IllegalArgumentException("Matrix array has irregularly sized subarrays");
                }
                System.arraycopy(m[i], 0, mat[i], 0, dimX);
            }
        }
    }

    private Matrix(Matrix m) {
        this(m.mat);
    }

    /**
     * Create a new matrix full of zeros of dimension dy by dx.
     * @param dy The dimension in the y direction.
     * @param dx The dimension in the x direction.
     */
    protected Matrix(int dy, int dx) {
        dimY = dy;
        dimX = dx;
        mat = new double[dimY][dimX];
    }

    /**
     * Gets the value at the index (indY, indX).
     * @param indY The y index.
     * @param indX The x index.
     * @return The value of the matrix at the specified index.
     */
    public double get(int indY, int indX) {
        if (indY < 0 || indY >= dimY || indX < 0 || indX >= dimX) {
            throw new IllegalArgumentException("Index out of bounds: (" + indY + ", " + indX + ")");
        }
        return mat[indY][indX];
    }

    /**
     * Creates a new matrix which is the same as the old one, excepting the index (indY, indX) which has the value value.
     * @param indY The y index.
     * @param indX The x index.
     * @param value The new value to replace with.
     * @return A new matrix with the same values except for value at (indY, indX).
     */
    public Matrix replace(int indY, int indX, double value) {
        if (indY < 0 || indY >= dimY || indX < 0 || indX >= dimX) {
            throw new IllegalArgumentException("Index out of bounds: (" + indY + ", " + indX + ")");
        }
        Matrix nm = new Matrix(this);
        nm.mat[indY][indX] = value;
        return nm;
    }
    
    /**
     * Creates an 1D array containing the matrix values as doubles organized columns first.
     * @return An array of length dimY * dimX.
     */
    public double[] toDoublesByCol() {
        double[] ma = new double[dimY * dimX];
        int index = 0;
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                ma[index] = mat[j][i];
                index++;
            }
        }
        return ma;
    }

    /**
     * Creates an 1D array containing the matrix values as doubles organized rows first.
     * @return An array of length dimY * dimX.
     */
    public double[] toDoublesByRow() {
        double[] ma = new double[dimY * dimX];
        int index = 0;
        for (int i = 0; i < dimY; i++) {
            for (int j = 0; j < dimX; j++) {
                ma[index] = mat[i][j];
                index++;
            }
        }
        return ma;
    }
    
    /**
     * Creates an 1D array containing the matrix values as floats organized columns first.
     * @return An array of length dimY * dimX.
     */
    public float[] toFloatsByCol() {
        float[] ma = new float[dimY * dimX];
        int index = 0;
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                ma[index] = (float) mat[j][i];
                index++;
            }
        }
        return ma;
    }
    
    /**
     * Creates an 1D array containing the matrix values as floats organized rows first.
     * @return An array of length dimY * dimX.
     */
    public float[] toFloatsByRow() {
        float[] ma = new float[dimY * dimX];
        int index = 0;
        for (int i = 0; i < dimY; i++) {
            for (int j = 0; j < dimX; j++) {
                ma[index] = (float) mat[i][j];
                index++;
            }
        }
        return ma;
    }
    
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < dimY; i++) {
            s.append("|");
            for (int j = 0; j < dimX; j++) {
                if(j != 0){
                    s.append(" ");
                }
                s.append((float) mat[i][j]);
            }
            s.append("|\n");
        }
        return s.toString();
    }
}
