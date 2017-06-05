package core;

public class Light {

    //Fields
    private Vector pos;
    private Vector color;
    private double specularIntensity;
    private double shadowIntensity;
    private double lightRadius;


    //Constructor
    public Light(Vector pos, Vector color, double specularIntensity, double shadowIntensity, double lightRadius) {
        this.pos = pos;
        this.color = color;
        this.specularIntensity = specularIntensity;
        this.shadowIntensity = shadowIntensity;
        this.lightRadius = lightRadius;
    }


    //Getters
    public Vector getPos() {
        return pos;
    }

    public Vector getColor() {
        return color;
    }

    public double getSpecularIntensity() {
        return specularIntensity;
    }

    public double getShadowIntensity() {
        return shadowIntensity;
    }

    public double getLightRadius() {
        return lightRadius;
    }

}
