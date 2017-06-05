package core;

public class Camera {
    //Note: camera fields are defining field of view/view plane 3D coardinate system
    //by orthonormal basis {x=left,y=up, z=lookAt} (orthogonal and normalized vectors)

    //Fields
    private Vector pos;//aka: "camera eye-pisiton".
    private Vector lookAtPos;
    private Vector lookAt;//aka: "towards".
    private Vector left;
    private Vector up;
    private double screenDistance;
    private double screenWidth;
    // private double aspectRatio;//def:= (screenWidth/screenHeight).
    // private double screenHeight;

    public Camera(Vector pos, Vector lookAtPos, Vector up, double screenDistance, double screenWidth) {
        this.pos = pos;
        this.lookAtPos = lookAtPos;

        //TODO: Make sure this is correct
        this.lookAt = lookAtPos.minus(pos).normalize();
        this.left = up.cross(lookAt).normalize().negate();
        this.up = left.cross(lookAt).normalize(); // fixing the given up vector.

        this.lookAtPos = new Ray(pos, new Vector(pos, lookAtPos)).getPointAtDistance(screenDistance);

        this.screenDistance = screenDistance;
        this.screenWidth = screenWidth;
    }

    /**
     * @param i            View plane x coordinate
     * @param j            View plane y coordinate
     * @param pixelsWidth  View plane pixels x width total
     * @param pixelsHeight View plane pixels y height total
     * @return eyeToPixelVector
     */
    public Vector getViewPlanePosition(double i, double j, int pixelsWidth, int pixelsHeight) {
        double pixelWidth = this.screenWidth / pixelsWidth; // defining physical width of a pixel.
        double aspectRatio = (double) pixelsWidth / (double) pixelsHeight;
        double pixelHeight = (1.0 / aspectRatio) * pixelWidth;
        // building groundZero =: Vector representing the (i,j)=(0,0) point in (left, up, towards) space.
        // (i,j)s counting is as matrix entries.
        //TODO: Decide direction
        Vector groundZero = this.lookAtPos;
        groundZero = groundZero.plus(this.left.scale((pixelsWidth / 2) * pixelWidth)); // i=0
        groundZero = groundZero.plus(this.up.scale((pixelsHeight / 2) * pixelHeight)); // j=0
        // building (i,j)'s pixel physical position.
        Vector pixelPhysicalPoint = groundZero.minus(this.left.scale(j * pixelWidth)); // set j
        pixelPhysicalPoint = pixelPhysicalPoint.minus(this.up.scale(i * pixelHeight)); // set i
        return new Vector(this.pos, pixelPhysicalPoint); // eyeToPixelVector
    }

    /**
     * @param i            View plane x coordinate
     * @param j            View plane y coordinate
     * @param pixelsWidth  View plane pixels x width total
     * @param pixelsHeight View plane pixels y height total
     * @return
     */
    public Ray constructRayThroughPixel(double i, double j, int pixelsWidth, int pixelsHeight) {
        Vector pixelPhysicalPosition = getViewPlanePosition(i, j, pixelsWidth, pixelsHeight);
        return new Ray(this.pos, pixelPhysicalPosition);
    }

    public Vector getPos() {
        return pos;
    }

    public Vector getLookAtPos() {
        return lookAtPos;
    }

    public Vector getLookAt() {
        return lookAt;
    }

    public Vector getLeft() {
        return left;
    }

    public Vector getUp() {
        return up;
    }

    public double getScreenDistance() {
        return screenDistance;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

}

