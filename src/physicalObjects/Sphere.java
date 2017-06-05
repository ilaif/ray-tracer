package physicalObjects;

import core.*;

public class Sphere extends Surface {
    //Fields
    Vector centerPos;
    double radius;

    //Constructor
    public Sphere(Vector centerPos, double radius, Material material) {
        super(material);
        this.centerPos = centerPos;
        this.radius = radius;
        this.keyPoint = centerPos;
    }

    //Super class methods implementations
    public RayHit getIntersection(Ray ray) {
        Vector p = ray.getSource();
        Vector u = ray.getDirection();
        Vector v = new Vector(this.centerPos, p);
        double b = 2 * (v.dot(u));
        double c = v.dot(v) - this.radius * this.radius;
        double discriminant = b * b - 4 * c;
        if (discriminant < 0) {
            return null;
        }

        double tMinus = (-b - Math.sqrt(discriminant)) / 2;
        double tPlus = (-b + Math.sqrt(discriminant)) / 2;

        if (tMinus < 0 && tPlus < 0) {
            return null;
        }

        double tValue;
        if (tMinus < 0 && tPlus > 0) {
            tValue = tPlus;
        } else {
            tValue = tMinus;
        }

        return new RayHit(ray, this, tValue);
    }

    public Vector getNormalAtPoint(Vector point) {
        return new Vector(this.centerPos, point).normalize();
    }
}
