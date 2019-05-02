/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.vec;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An immutable linear algebra vector. Has methods which facilitate with general linear algebra as well as with graphics.
 * @author Kosmic
 */
public class Vector {

    /**
     * Double nearer to each other than EPSILON are considered the same.
     */
    public static final double EPSILON = 1E-24;
    
    /**
     * Creates a vector with a one in index index and zeros everywhere else.
     * @param d The dimension of the vector.
     * @param index The index which contains a one.
     * @return A d dimensional vector.
     */
    public static Vector e(int d, int index) {
        if (index < 0 || index >= d) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        Vector nv = new Vector(d);
        nv.vec[index] = 1;
        return nv;
    }
    
    /**
     * Creates a vector with ones in every index. 
     * @param d The dimension of the vector.
     * @return A d dimensional vector.
     */
    public static Vector ones(int d) {
        Vector nv = new Vector(d);
        for (int i = 0; i < d; i++) {
            nv.vec[i] = 1;
        }
        return nv;
    }
    
    /**
     * Creates a d dimensional zero vector.
     * @param d The dimension of the vector.
     * @return A d dimensional vector.
     */
    public static Vector zeros(int d) {
        return new Vector(d);
    }

    /**
     * The dimension of the vector.
     */
    public final int dim;
    private final double[] vec;

    /**
     * Creates a vector from inputs. The dimension is the same as the number of inputs.
     * @param v The ordered inputs.
     */
    public Vector(double... v) {
        dim = v.length;
        vec = Arrays.copyOf(v, dim);
    }

    private Vector(Vector v) {
        dim = v.dim;
        vec = Arrays.copyOf(v.vec, dim);
    }

    /**
     * Creates a new vector of zeros with dimension d.
     * @param d The dimension of the vector.
     */
    protected Vector(int d) {
        dim = d;
        vec = new double[dim];
    }

    /**
     * Adds two vectors together.
     * @param v The other vector.
     * @return A new vector where each index is the sum of the same indeces of the inputs.
     */
    public Vector add(Vector v) {
        checkDim(v);
        Vector nv = new Vector(dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] + v.vec[i];
        }
        return nv;
    }

    /**
     * Asserts that the dimension of the vector is d.
     * @param d The dimension that the vector must be equal to.
     */
    public void assertSize(int d) {
        if (d < 0) {
            throw new IllegalArgumentException("Dimension cannot be smaller than 0");
        }
        assert dim == d;
    }

    /**
     * Applies bimapper to each index of the vectors and creates a new vector from the output.
     * @param v The other vector.
     * @param bimapper The bioperator to apply to each index.
     * @return A new vector where each index is the result of the bimapper of the same indeces of the inputs.
     */
    public Vector bimap(Vector v, BiFunction<Double, Double, Double> bimapper) {
        checkDim(v);
        Vector nv = new Vector(dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = bimapper.apply(vec[i], v.vec[i]);
        }
        return nv;
    }

    /**
     * Throws an exception if the other vector is not the same dimension.
     * @param v The other vector.
     */
    protected final void checkDim(Vector v) {
        if (dim != v.dim) {
            throw new IllegalArgumentException("Dimensions don't match: " + dim + " != " + v.dim);
        }
    }

    /**
     * Returns true if each vector is in the same quadrant and each index of v is less than or equal to each index of this.
     * @param v The other vector.
     * @return Whether v is contained by this vector by the definition.
     */
    public boolean contains(Vector v) {
        checkDim(v);
        Vector adjusted = bimap(v, (d, t) -> d > 0 ? t : -t);
        Vector positiveCompare = map(t -> Math.abs(t));
        for (int i = 0; i < dim; i++) {
            if (adjusted.vec[i] < 0 || positiveCompare.vec[i] < adjusted.vec[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if each vector is in the same quadrant and each index of v is less than each index of this.
     * @param v The other vector.
     * @return Whether v is contained by this vector by the definition.
     */
    public boolean containsExclusive(Vector v) {
        checkDim(v);
        Vector adjusted = bimap(v, (d, t) -> d > 0 ? t : -t);
        Vector positiveCompare = map(t -> Math.abs(t));
        for (int i = 0; i < dim; i++) {
            if (adjusted.vec[i] < 0 || positiveCompare.vec[i] <= adjusted.vec[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the crossproduct between two 3D vectors.
     * @param v The other vector, which is to the right in this cross v.
     * @return The 3D crossproduct vector.
     */
    public Vector cross(Vector v) {
        if (dim != 3 || v.dim != 3) {
            throw new IllegalArgumentException("Both vectors must have a dimension of 3: " + dim + " != 3; " + v.dim + " != 3");
        }
        Vector nv = new Vector(3);
        nv.vec[0] = vec[1] * v.vec[2] - vec[2] * v.vec[1];
        nv.vec[1] = vec[2] * v.vec[0] - vec[0] * v.vec[2];
        nv.vec[2] = vec[0] * v.vec[1] - vec[1] * v.vec[0];
        return nv;
    }

    /**
     * Returns the dot product between the two vectors.
     * @param v The other vector.
     * @return The dot product.
     */
    public double dot(Vector v) {
        checkDim(v);
        double sum = 0;
        for (int i = 0; i < dim; i++) {
            sum += vec[i] * v.vec[i];
        }
        return sum;
    }
    
    @Override
    public boolean equals(Object v) {
        if (!(v instanceof Vector)) {
            return false;
        }
        Vector w = (Vector) v;
        if (dim != w.dim) {
            return false;
        }
        for (int i = 0; i < dim; i++) {
            if (Math.abs(vec[i] - w.vec[i]) >= EPSILON) {
                return false;
            }
        }
        return true;
    }

    /**
     * Applies folder to aggregate values from index 0 to dim-1.
     * @param b The initial value.
     * @param folder The bioperator to apply to the vector. The first input should be the previous value, followed by the value at the next index of the vector.
     * @return The aggregate after folding the vector.
     */
    public double foldl(double b, BiFunction<Double, Double, Double> folder) {
        double prev = b;
        for (int i = 0; i < dim; i++) {
            prev = folder.apply(prev, vec[i]);
        }
        return prev;
    }

    /**
     * Applies folder to aggregate values from index dim-1 to 0.
     * @param b The initial value.
     * @param folder The bioperator to apply to the vector. The first input should be the previous value, followed by the value at the next index of the vector.
     * @return The aggregate after folding the vector.
     */
    public double foldr(double b, BiFunction<Double, Double, Double> folder) {
        double prev = b;
        for (int i = dim - 1; i >= 0; i--) {
            prev = folder.apply(prev, vec[i]);
        }
        return prev;
    }

    /**
     * Returns the value at the given index of the vector.
     * @param index The index to poll.
     * @return The value at the given index.
     */
    public double get(int index) {
        if (index < 0 || index >= dim) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return vec[index];
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.dim;
        return hash;
    }

    /**
     * Returns the magnitude of the vector.
     * @return The vector's length.
     */
    public double mag() {
        return Math.sqrt(magSquared());
    }

    /**
     * Returns the square of the magnitude of the vector.
     * @return The vector's length squared.
     */
    public double magSquared() {
        return dot(this);
    }

    /**
     * Creates a new vector with mapper applied to each index.
     * @param mapper The unary operator to apply to each index.
     * @return A new vector.
     */
    public Vector map(Function<Double, Double> mapper) {
        Vector nv = new Vector(dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = mapper.apply(vec[i]);
        }
        return nv;
    }

    /**
     * Multiplies the vector by a scalar.
     * @param scalar The scalar.
     * @return A new scaled vector.
     */
    public Vector mult(double scalar) {
        Vector nv = new Vector(dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] * scalar;
        }
        return nv;
    }

    /**
     * Negates every index of the vector.
     * @return A new negated vector.
     */
    public Vector neg() {
        return mult(-1);
    }

    /**
     * Returns a new vector with magnitude 1 which faces in the same direction.
     * @return A new normalized vector.
     */
    public Vector normalize() {
        double mag = mag();
        if (mag < EPSILON) {
            throw new RuntimeException("Trying to normalize a vector of magnitude 0");
        }
        return mult(1 / mag);
    }

    /**
     * Creates a new vector with every entry the same except for the entry at index, which has the value value.
     * @param index The index to change.
     * @param value The value to put into index.
     * @return A new vector with the index  at index changed to value.
     */
    public Vector replace(int index, double value) {
        if (index < 0 || index >= dim) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        Vector nv = new Vector(this);
        nv.vec[index] = value;
        return nv;
    }

    /**
     * Creates a new vector with dimension d such that every entry is either the same as the original vector, or zero.
     * @param d The dimension of the vector.
     * @return A resized vector.
     */
    public Vector resize(int d) {
        if (d < 0) {
            throw new IllegalArgumentException("Dimension cannot be smaller than 0");
        }
        Vector nv = new Vector(d);
        System.arraycopy(vec, 0, nv.vec, 0, Math.min(dim, d));
        return nv;
    }

    /**
     * Subtracts v from this vector.
     * @param v The other vector.
     * @return The resultant vector from the subtraction.
     */
    public Vector sub(Vector v) {
        checkDim(v);
        Vector nv = new Vector(dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] - v.vec[i];
        }
        return nv;
    }

    /**
     * Tensor multiplies the two vectors.
     * @param v The other vector.
     * @return The resultant vector where each index is the product of the values at the same index from the other two vectors.
     */
    public Vector tensorMult(Vector v) {
        checkDim(v);
        Vector nv = new Vector(dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] * v.vec[i];
        }
        return nv;
    }

    /**
     * Returns the array of doubles representing the vector.
     * @return A double array.
     */
    public double[] toDoubles() {
        return Arrays.copyOf(vec, dim);
    }

    /**
     * Returns the array of floats representing the vector.
     * @return A float array.
     */
    public float[] toFloats() {
        float[] va = new float[dim];
        for (int i = 0; i < dim; i++) {
            va[i] = (float) vec[i];
        }
        return va;
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("<");
        for (int i = 0; i < dim; i++) {
            if (i != 0) {
                s.append(", ");
            }
            s.append((float) vec[i]);
        }
        s.append(">");
        return s.toString();
    }

    /**
     * Gets the value at index zero.
     * @return The value at index zero.
     */
    public double x() {
        return get(0);
    }

    /**
     * Sets the value at index zero.
     * @param value The value to replace with.
     * @return A new vector with the zeroth index changed to value.
     */
    public Vector x(double value) {
        return replace(0, value);
    }

    /**
     * Gets the value at index one.
     * @return The value at index one.
     */
    public double y() {
        return get(1);
    }

    /**
     * Sets the value at index one.
     * @param value The value to replace with.
     * @return A new vector with the first index changed to value.
     */
    public Vector y(double value) {
        return replace(1, value);
    }

    /**
     * Gets the value at index two.
     * @return The value at index two.
     */
    public double z() {
        return get(2);
    }

    /**
     * Sets the value at index two.
     * @param value The value to replace with.
     * @return A new vector with the second index changed to value.
     */
    public Vector z(double value) {
        return replace(2, value);
    }
}
