package core;

import physicalObjects.*;

public class RayHit {

    //Fields
    private Ray ray;
    private Surface hitObject;
    private Vector hitPoint;
    private double hitDistance;


    //Constructors
    public RayHit(Ray ray, Surface hitObject, Vector hitPoint) {
        this.ray = ray;
        this.hitObject = hitObject;
        this.hitPoint = hitPoint;
        Vector sourceToHitPoint = new Vector(ray.getSource(), hitPoint);
        this.hitDistance = sourceToHitPoint.magnitude();
    }

    public RayHit(Ray ray, Surface hitObject, double hitDistance) {
        this.ray = ray;
        this.hitObject = hitObject;
        this.hitPoint = ray.getPointAtDistance(hitDistance);
        this.hitDistance = hitDistance;
    }

    public Ray getTransmissionRay() {
        Ray transmissionRay = new Ray(hitPoint, this.ray.getDirection());
        return (transmissionRay);
    }

    public Ray getReflectionRay() {
        Vector rayDirection = this.ray.getDirection();
        Vector normalAtHitPoint = this.hitObject.getNormalAtPoint(this.hitPoint);
        double cosAngle = rayDirection.dot(normalAtHitPoint);
        Vector reflectionRayDirection = rayDirection.minus(normalAtHitPoint.scale(2.0 * cosAngle));
        Ray reflectionRay = new Ray(this.hitPoint, reflectionRayDirection);
        return (reflectionRay);
    }

    public Ray getRay() {
        return ray;
    }

    public Surface getHitObject() {
        return hitObject;
    }

    public Vector getHitPoint() {
        return hitPoint;
    }

    public double getHitDistance() {
        return hitDistance;
    }

}
