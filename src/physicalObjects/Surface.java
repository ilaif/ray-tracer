package physicalObjects;

import core.*;

public abstract class Surface {
    //Fields:
    protected Vector keyPoint; // used for equals method overriding only.
    protected Material material;


    //Constructor
    public Surface(Material material) {
        this.material = material;
    }


    //Abstract Methods
    public abstract RayHit getIntersection(Ray ray);

    public abstract Vector getNormalAtPoint(Vector point);


    //Getters
    public Vector getKeyPoint() {
        return keyPoint;
    }

    public Material getMaterial() {
        return material;
    }

    //Overriding
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Surface surface = (Surface) o;

        return keyPoint.equals(surface.keyPoint);
    }

    @Override
    public int hashCode() {
        return keyPoint.hashCode();
    }


}
