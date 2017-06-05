package physicalObjects;

import core.*;

public class Plane extends Surface {
    //Fields
    private Vector normal;
    private double offset;

    //Constructor
    public Plane(Vector normal, double offset, Material material) {
        super(material);
        this.normal = normal;
        this.offset = offset;
        this.keyPoint = normal;
    }

    //Super class methods implementations
    public RayHit getIntersection(Ray ray) {
        double d = this.normal.dot(this.normal.scale(this.offset));
        double VN = ray.getDirection().dot(this.normal);
        double P0N = ray.getSource().dot(this.normal);
        double t;

        if (VN == 0) {
            return null;
        }
        t = (-P0N + d) / VN;
        if (t <= 0) {
            return null;
        }

        return new RayHit(ray, this, t);
    }

    public RayHit TMPgetIntersection(Ray ray) {
        // from http://www.tar.hu/gamealgorithms/ch22lev1sec2.html
        double a = this.normal.x();
        double b = this.normal.y();
        double c = this.normal.z();
        double denominator = (a * ray.getDirection().x() + b * ray.getDirection().y() + c * ray.getDirection().z());
        if (denominator == 0.0) {
            return null;
        }

        double tValue = -(a * ray.getSource().x() + b * ray.getSource().y() + c * ray.getSource().z() + this.offset) / denominator;

        if (tValue < 0) {
            return null;
        }

        return new RayHit(ray, this, tValue);
    }

    public Vector getNormalAtPoint(Vector point) {
        return this.normal;
    }

}
