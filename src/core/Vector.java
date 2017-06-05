package core;

import java.lang.Math;
import java.util.Arrays;

public class Vector {

    //Vector Fields
    private final int n = 3;         // length of the vector
    private double[] data;       // array of vector's components


    //Vector Constructors
    public Vector() {
        this.data = new double[this.n];
    }

    public Vector(double val) {
        this.data = new double[this.n];
        for (int i = 0; i < this.n; i++)
            this.data[i] = val;
    }

    public Vector(double x, double y, double z) {
        this.data = new double[]{x, y, z};
    }

    public Vector(Vector from, Vector to) {
        Vector v = to.minus(from);
        this.data = v.getData();
    }


    //Methods
    public Vector plus(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        Vector c = new Vector();
        for (int i = 0; i < n; i++)
            c.data[i] = this.data[i] + that.data[i];
        return c;
    }

    public Vector minus(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        Vector c = new Vector();
        for (int i = 0; i < n; i++)
            c.data[i] = this.data[i] - that.data[i];
        return c;
    }

    // create and return a new object whose value is (this * factor)
    public Vector scale(double factor) {
        Vector c = new Vector();
        for (int i = 0; i < n; i++)
            c.data[i] = factor * data[i];
        return c;
    }

    // dot pruduct
    public double dot(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        double sum = 0.0;
        for (int i = 0; i < n; i++)
            sum = sum + (this.data[i] * that.data[i]);
        return sum;
    }

    // return the Euclidean norm of this Vector
    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    // return the Euclidean distance between this and that
    public double distanceTo(Vector that) {
        if (this.length() != that.length())
            throw new IllegalArgumentException("dimensions disagree");
        return this.minus(that).magnitude();
    }

    // return the corresponding unit vector
    public Vector direction() {
        if (this.magnitude() == 0.0)
            throw new ArithmeticException("zero-vector has no direction");
        return this.scale(1.0 / this.magnitude());
    }

    public Vector normalize() {
        return this.direction();
    }

    public Vector negate() {
        return this.scale(-1);
    }

    public Vector cross(Vector v) {
        double xn = y() * v.z() - z() * v.y();
        double yn = z() * v.x() - x() * v.z();
        double zn = x() * v.y() - y() * v.x();
        return new Vector(xn, yn, zn);
    }

    public Vector mult(Vector v) {
        return new Vector(this.x() * v.x(), this.y() * v.y(), this.z() * v.z());
    }


    //Getters
    public double x() {
        return (this.data[0]);
    }

    public double y() {
        return (this.data[1]);
    }

    public double z() {
        return (this.data[2]);
    }

    public double cartesian(int i) {
        return data[i];
    }

    public int length() {
        return (this.n);
    }

    public double[] getData() {
        return data;
    }

    //Overriding
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (n != vector.n) return false;
        return Arrays.equals(data, vector.data);
    }

    @Override
    public int hashCode() {
        int result = n;
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Vector(" + Arrays.toString(data) + ")";
    }


}