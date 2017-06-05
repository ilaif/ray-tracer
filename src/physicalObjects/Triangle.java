package physicalObjects;

import core.*;

public class Triangle extends Surface {
    //Fields
    private Vector v1, v2, v3;
    private Vector u, v;
    private Plane plane;
    private Vector normal;


    //Constructor
    public Triangle(Vector p1, Vector p2, Vector p3, Material material) {
        super(material);
        this.v1 = p1;
        this.v2 = p2;
        this.v3 = p3;
        this.u = new Vector(p1, p2);
        this.v = new Vector(p1, p3);
        this.normal = u.cross(v).normalize();

        double a = normal.x();
        double b = normal.y();
        double c = normal.z();
        double d = p1.x() * normal.x() + p1.y() * normal.y() + p1.z() * normal.z();
        this.plane = new Plane((new Vector(a, b, c)), (-d), material);
    }


    //Super class methods implementations
    public RayHit getIntersection(Ray ray) {
        RayHit planeHit = this.plane.getIntersection(ray);
        if (planeHit == null) {
            return null;
        }

        double uu, uv, vv, wu, wv, D;
        uu = u.dot(u);
        uv = u.dot(v);
        vv = v.dot(v);
        Vector w = planeHit.getHitPoint().plus(this.v1.scale(-1.0));

        wu = w.dot(u);
        wv = w.dot(v);
        D = uv * uv - uu * vv;

        double s, t;
        s = (uv * wv - vv * wu) / D;
        if (s < 0 || s > 1) {
            return null;
        }
        t = (uv * wu - uu * wv) / D;
        if (t < 0 || (s + t) > 1) {
            return null;
        }

        return new RayHit(planeHit.getRay(), this, planeHit.getHitPoint());
    }

    public Vector getNormalAtPoint(Vector point) {
        return (this.normal);
    }
}
