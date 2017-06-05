package core;

public class Ray {
    private Vector source;
    private Vector direction;

    public Ray(Vector source, Vector direction) {
        this.source = source;
        this.direction = direction.normalize();
    }

    public Vector getPointAtDistance(double distance) {
        return (this.source.plus(this.direction.scale(distance)));
    }

    public Vector getSource() {
        return source;
    }

    public Vector getDirection() {
        return direction;
    }
}
