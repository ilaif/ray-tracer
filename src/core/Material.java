package core;

public class Material {
    //Fields
    private Vector diffuseColor;
    private Vector specularColor;
    private Vector reflectionColor;
    private double phongSpecularityCoefficient;
    private double transparency;


    //Constructor
    public Material(Vector diffuseColor, Vector specularColor, Vector reflectionColor, double phongSpecularityCoefficient, double transparency) {
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.reflectionColor = reflectionColor;
        this.phongSpecularityCoefficient = phongSpecularityCoefficient;
        this.transparency = transparency;
    }


    //Getters
    public Vector getDiffuseColor() {
        return diffuseColor;
    }

    public Vector getSpecularColor() {
        return specularColor;
    }

    public Vector getReflectionColor() {
        return reflectionColor;
    }

    public double getPhongSpecularityCoefficient() {
        return phongSpecularityCoefficient;
    }

    public double getTransparency() {
        return transparency;
    }
}


