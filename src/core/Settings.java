package core;

public class Settings {

    //Fields
    private Vector bgColor;
    private double rootNumberShadowRays;
    private int maxRecursion;
    private int superSamplingLevel;


    //Constructor
    public Settings(Vector bgColor, double rootNumberShadowRays, int maxRecursion, int superSamplingLevel) {
        this.bgColor = bgColor;
        this.rootNumberShadowRays = rootNumberShadowRays;
        this.maxRecursion = maxRecursion;
        this.superSamplingLevel = superSamplingLevel;
    }


    //Getters
    public int getSuperSamplingLevel(){
        return this.superSamplingLevel;
    }

    public Vector getBgColor() {
        return bgColor;
    }

    public double getRootNumberShadowRays() {
        return rootNumberShadowRays;
    }

    public int getMaxRecursion() {
        return maxRecursion;
    }
}

