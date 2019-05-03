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
 *
 * @author TARS
 */
public class IntVector {

    /**
     * Creates an int vector with a one in index index and zeros everywhere
     * else.
     *
     * @param d The dimension of the vector.
     * @param index The index which contains a one.
     * @return A d dimensional int vector.
     */
    public static IntVector e(int d, int index) {
        if (index < 0 || index >= d) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        IntVector nv = new IntVector((Integer) d);
        nv.vec[index] = 1;
        return nv;
    }

    /**
     * Creates an int vector with ones in every index.
     *
     * @param d The dimension of the vector.
     * @return A d dimensional int vector.
     */
    public static IntVector ones(int d) {
        IntVector nv = new IntVector((Integer) d);
        for (int i = 0; i < d; i++) {
            nv.vec[i] = 1;
        }
        return nv;
    }

    /**
     * Creates a d dimensional zero vector.
     *
     * @param d The dimension of the vector.
     * @return A d dimensional int vector.
     */
    public static IntVector zeros(int d) {
        return new IntVector((Integer) d);
    }

    /**
     * The dimension of the vector
     */
    public final int dim;
    private final int[] vec;

    /**
     * Creates an int vector from inputs. The dimension is the same as the
     * number of inputs.
     *
     * @param v The ordered inputs.
     */
    public IntVector(int... v) {
        dim = v.length;
        this.vec = Arrays.copyOf(v, dim);
    }

    /**
     * Creates an int vector from a vector of doubles by casting to (int).
     *
     * @param v The int vector from the double vector.
     */
    public IntVector(Vector v) {
        dim = v.dim;
        vec = new int[dim];
        for (int i = 0; i < dim; i++) {
            vec[i] = (int) v.get(i);
        }
    }

    private IntVector(IntVector v) {
        dim = v.dim;
        vec = Arrays.copyOf(v.vec, dim);
    }

    private IntVector(Integer d) {
        dim = d;
        vec = new int[d];
    }

    /**
     * Adds two int vectors together.
     *
     * @param v The other int vector.
     * @return A new int vector where each index is the sum of the same indeces
     * of the inputs.
     */
    public IntVector add(IntVector v) {
        checkDim(v);
        IntVector nv = new IntVector((Integer) dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] + v.vec[i];
        }
        return nv;
    }

    /**
     * Asserts that the dimension of the int vector is d.
     *
     * @param d The dimension that the int vector must be equal to.
     */
    public void assertSize(int d) {
        if (d < 0) {
            throw new IllegalArgumentException("Dimension cannot be smaller than 0");
        }
        assert dim == d;
    }

    /**
     * Applies bimapper to each index of the int vectors and creates a new int
     * vector from the output.
     *
     * @param v The other int vector.
     * @param bimapper The bioperator to apply to each index.
     * @return A new int vector where each index is the result of the bimapper
     * of the same indeces of the inputs.
     */
    public IntVector bimap(IntVector v, BiFunction<Integer, Integer, Integer> bimapper) {
        checkDim(v);
        IntVector nv = new IntVector((Integer) dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = bimapper.apply(vec[i], v.vec[i]);
        }
        return nv;
    }

    /**
     * Throws an exception if the other int vector is not the same dimension.
     *
     * @param v The other int vector.
     */
    protected final void checkDim(IntVector v) {
        if (dim != v.dim) {
            throw new IllegalArgumentException("Dimensions don't match: " + dim + " != " + v.dim);
        }
    }

    /**
     * Returns true if each int vector is in the same quadrant and each index of
     * v is less than or equal to each index of this.
     *
     * @param v The other int vector.
     * @return Whether v is contained by this int vector by the definition.
     */
    public boolean contains(IntVector v) {
        checkDim(v);
        IntVector adjusted = bimap(v, (d, t) -> d > 0 ? t : -t);
        IntVector positiveCompare = map(t -> Math.abs(t));
        for (int i = 0; i < dim; i++) {
            if (adjusted.vec[i] < 0 || positiveCompare.vec[i] < adjusted.vec[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if each int vector is in the same quadrant and each index of
     * v is less than each index of this.
     *
     * @param v The other int vector.
     * @return Whether v is contained by this int vector by the definition.
     */
    public boolean containsExclusive(IntVector v) {
        checkDim(v);
        IntVector adjusted = bimap(v, (d, t) -> d > 0 ? t : -t);
        IntVector positiveCompare = map(t -> Math.abs(t));
        for (int i = 0; i < dim; i++) {
            if (adjusted.vec[i] < 0 || positiveCompare.vec[i] <= adjusted.vec[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the crossproduct between two 3D int vectors.
     *
     * @param v The other int vector, which is to the right in this cross v.
     * @return The 3D crossproduct int vector.
     */
    public IntVector cross(IntVector v) {
        if (dim != 3 || v.dim != 3) {
            throw new IllegalArgumentException("Both vectors must have a dimension of 3: " + dim + " != 3; " + v.dim + " != 3");
        }
        IntVector nv = new IntVector(3);
        nv.vec[0] = vec[1] * v.vec[2] - vec[2] * v.vec[1];
        nv.vec[1] = vec[2] * v.vec[0] - vec[0] * v.vec[2];
        nv.vec[2] = vec[0] * v.vec[1] - vec[1] * v.vec[0];
        return nv;
    }

    /**
     * Returns the dot product between the two int vectors.
     *
     * @param v The other int vector.
     * @return The dot product.
     */
    public int dot(IntVector v) {
        checkDim(v);
        int sum = 0;
        for (int i = 0; i < dim; i++) {
            sum += vec[i] * v.vec[i];
        }
        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntVector other = (IntVector) obj;
        return Arrays.equals(this.vec, other.vec);
    }

    /**
     * Applies folder to aggregate values from index 0 to dim-1.
     *
     * @param b The initial value.
     * @param folder The bioperator to apply to the int vector. The first input
     * should be the previous value, followed by the value at the next index of
     * the int vector.
     * @return The aggregate after folding the int vector.
     */
    public double foldl(double b, BiFunction<Integer, Double, Double> folder) {
        double prev = b;
        for (int i = 0; i < dim; i++) {
            prev = folder.apply(vec[i], prev);
        }
        return prev;
    }

    /**
     * Applies folder to aggregate values from index dim-1 to 0.
     *
     * @param b The initial value.
     * @param folder The bioperator to apply to the int vector. The first input
     * should be the previous value, followed by the value at the next index of
     * the int vector.
     * @return The aggregate after folding the int vector.
     */
    public double foldr(double b, BiFunction<Integer, Double, Double> folder) {
        double prev = b;
        for (int i = dim - 1; i >= 0; i--) {
            prev = folder.apply(vec[i], prev);
        }
        return prev;
    }

    /**
     * Returns the value at the given index of the int vector.
     *
     * @param index The index to poll.
     * @return The value at the given index.
     */
    public int get(int index) {
        if (index < 0 || index >= dim) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return vec[index];
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return 13 * hash + Arrays.hashCode(this.vec);
    }

    /**
     * Returns the magnitude of the int vector.
     *
     * @return The int vector's length.
     */
    public double mag() {
        return Math.sqrt(magSquared());
    }

    /**
     * Returns the square of the magnitude of the int vector.
     *
     * @return The int vector's length squared.
     */
    public int magSquared() {
        return dot(this);
    }

    /**
     * Creates a new int vector with mapper applied to each index.
     *
     * @param mapper The unary operator to apply to each index.
     * @return A new int vector.
     */
    public IntVector map(Function<Integer, Integer> mapper) {
        IntVector nv = new IntVector((Integer) dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = mapper.apply(vec[i]);
        }
        return nv;
    }

    /**
     * Multiplies the int vector by a scalar.
     *
     * @param scalar The scalar.
     * @return A new scaled int vector.
     */
    public IntVector mult(int scalar) {
        IntVector nv = new IntVector((Integer) dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] * scalar;
        }
        return nv;
    }

    /**
     * Negates every index of the int vector.
     *
     * @return A new negated int vector.
     */
    public IntVector neg() {
        return mult(-1);
    }

    /**
     * Creates a new int vector with every entry the same except for the entry
     * at index, which has the value value.
     *
     * @param index The index to change.
     * @param value The value to put into index.
     * @return A new int vector with the index at index changed to value.
     */
    public IntVector replace(int index, int value) {
        if (index < 0 || index >= dim) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        IntVector nv = new IntVector(this);
        nv.vec[index] = value;
        return nv;
    }

    /**
     * Creates a new int vector with dimension d such that every entry is either
     * the same as the original vector, or zero.
     *
     * @param d The dimension of the int vector.
     * @return A resized int vector.
     */
    public IntVector resize(int d) {
        if (d < 0) {
            throw new IllegalArgumentException("Dimension cannot be smaller than 0");
        }
        IntVector nv = new IntVector((Integer) d);
        System.arraycopy(vec, 0, nv.vec, 0, Math.min(dim, d));
        return nv;
    }

    /**
     * Subtracts v from this int vector.
     *
     * @param v The other int vector.
     * @return The resultant int vector from the subtraction.
     */
    public IntVector sub(IntVector v) {
        checkDim(v);
        IntVector nv = new IntVector((Integer) dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] - v.vec[i];
        }
        return nv;
    }

    /**
     * Tensor multiplies the two int vectors.
     *
     * @param v The other int vector.
     * @return The resultant int vector where each index is the product of the
     * values at the same index from the other two int vectors.
     */
    public IntVector tensorMult(IntVector v) {
        checkDim(v);
        IntVector nv = new IntVector((Integer) dim);
        for (int i = 0; i < dim; i++) {
            nv.vec[i] = vec[i] * v.vec[i];
        }
        return nv;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("<");
        for (int i = 0; i < dim; i++) {
            if (i != 0) {
                s.append(", ");
            }
            s.append(vec[i]);
        }
        s.append(">");
        return s.toString();
    }

    /**
     * Gets the value at index zero.
     *
     * @return The value at index zero.
     */
    public int x() {
        return get(0);
    }

    /**
     * Sets the value at index zero.
     *
     * @param value The value to replace with.
     * @return A new int vector with the zeroth index changed to value.
     */
    public IntVector x(int value) {
        return replace(0, value);
    }

    /**
     * Gets the value at index one.
     *
     * @return The value at index one.
     */
    public int y() {
        return get(1);
    }

    /**
     * Sets the value at index one.
     *
     * @param value The value to replace with.
     * @return A new int vector with the first index changed to value.
     */
    public IntVector y(int value) {
        return replace(1, value);
    }

    /**
     * Gets the value at index two.
     *
     * @return The value at index two.
     */
    public int z() {
        return get(2);
    }

    /**
     * Sets the value at index two.
     *
     * @param value The value to replace with.
     * @return A new int vector with the second index changed to value.
     */
    public IntVector z(int value) {
        return replace(2, value);
    }
}
