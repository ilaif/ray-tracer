/*

package core;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import physicalObjects.*;

/**
 * _old_Main class for ray tracing exercise.
 */
/*
public class RayTracer11 {

    private int imageWidth;
    private int imageHeight;
    private SceneParser sceneParser;

    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
/*
    public static void main(String[] args) {
        try {
            RayTracer11 tracer = new RayTracer11();

            // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;

            if (args.length < 2)
                throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

            String sceneFileName = args[0];
            String outputFileName = args[1];

            if (args.length > 3) {
                tracer.imageWidth = Integer.parseInt(args[2]);
                tracer.imageHeight = Integer.parseInt(args[3]);
            }

            // Parse scene file:


            tracer.parseScene(sceneFileName);

            // Render scene:
            tracer.renderScene(outputFileName);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (RayTracerException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
/*
    private void parseScene(String sceneFileName) throws IOException, RayTracerException {

        System.out.println("Started parsing scene file " + sceneFileName);
        this.sceneParser = new SceneParser(sceneFileName);
        System.out.println("Finished parsing scene file " + sceneFileName);
    }

    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
/*
    private void renderScene(String outputFileName) {

//previous code:
//        Vector n = camera.getPos().minus(camera.getLookAtPos()).normalize();
//        Vector u = camera.getUp().cross(n).normalize(); // Get the vector u perpendicular to n
//        Vector v = n.cross(u); // And get the one perpendicular to it
//
//        double x = u.dot(camera.getPos()) * -1;
//        double y = v.dot(camera.getPos()) * -1;
//        double z = n.dot(camera.getPos()) * -1;
//        Vector e = new Vector(x, y, z);
//
//        // The transformation from screen world to camera world:
//        double V[][] = new double[][]{
//                new double[]{u.x(), u.y(), u.z(), e.x()},
//                new double[]{v.x(), v.y(), v.z(), e.y()},
//                new double[]{n.x(), n.y(), n.z(), e.z()},
//                new double[]{0.0, 0.0, 0.0, 1.0}
//        };
//        Matrix viewingTransform = new Matrix(V, true);
//
//        double aspectRatio = this.imageWidth / (double) this.imageHeight;
//        double screenWidth = camera.getScreenWidth();
//        double screenHeight = screenWidth / aspectRatio;
//
//        // TODO: not sure about this
//        double angle = Math.atan((screenWidth / 2) / camera.getScreenDistance()) * 2;
//        Vector rayOrigin = camera.getPos();
//
//        for (int i = 0; i < this.imageHeight; i++) {
//            for (int j = 0; j < this.imageWidth; j++) {
//                image[i][j] = new Vector();
//                double ii = i + 0.0;
//                double jj = j + 0.0;
//                double xx = (2 * ((ii + 0.5) / screenWidth) - 1) * angle * aspectRatio;
//                double yy = (1 - 2 * ((jj + 0.5) / screenHeight)) * angle;
//                Vector rayDirection = new Vector(xx, yy, -1);
//
//				// Ray transformation to world coordinate system
//                rayDirection = viewingTransform.transform(rayDirection, 0);
//                rayDirection.normalize();

        long startTime = System.currentTimeMillis();
        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

        Camera camera = this.sceneParser.getCamera();
        int superSamplingLevel = this.sceneParser.getSettings().getSuperSamplingLevel();

//        for (int i = 0; i < this.imageWidth; i++) {
//            for (int j = 0; j < this.imageHeight; j++) {
//
//                //Super Sampling
//                Vector accumColor = new Vector(0,0,0);
//                for (int a = 0; a < superSamplingLevel; a++) {
//                    for (int b = 0; b < superSamplingLevel; b++) {
//                        double w = (double) i / (double) superSamplingLevel;
//                        double h = (double) j / (double) superSamplingLevel;
//                        Ray rayThroughPixel = camera.constructRayThroughPixel((i + w), (j + h), this.imageWidth, this.imageHeight);
//                        RayHit hit = findClosestIntersection(rayThroughPixel, null);
//                        Vector pixelColor = findColor(hit);
//                        accumColor=accumColor.plus(pixelColor);
//                        //TODO: coloring the pixel, summing up super sampling pixels and avaraging them, assigning them to rgbData.
//                    }
//                    double samplingPixels=superSamplingLevel^2;
//                    accumColor=accumColor.scale(1.0/samplingPixels);
//                    rgbData[(j * this.imageWidth + i) * 3] = (byte) ((int) (255 * (accumColor.x())));
//                    rgbData[(j * this.imageWidth + i) * 3 + 1] = (byte) ((int) (255 * (accumColor.y())));
//                    rgbData[(j * this.imageWidth + i) * 3 + 2] = (byte) ((int) (255 * (accumColor.z())));
//
//                }
//
//            }
//        }
        for (int i = 0; i < this.imageWidth; i++) {
            for (int j = 0; j < this.imageHeight; j++) {

                //Super Sampling
                Vector accumColor = new Vector(0, 0, 0);
                for (int a = 0; a < superSamplingLevel; a++) {
                    for (int b = 0; b < superSamplingLevel; b++) {
                        double w = (double) i / (double) superSamplingLevel;
                        double h = (double) j / (double) superSamplingLevel;
                        Ray rayThroughPixel = camera.constructRayThroughPixel((i + w), (j + h), this.imageWidth, this.imageHeight);
                        RayHit hit = findClosestIntersection(rayThroughPixel, null);

                        for (Light lightSource : this.sceneParser.getLights())
                            accumColor = accumColor.plus(findColor(hit, lightSource, 0));
                    }

                }
                double samplingPixels = Math.pow(superSamplingLevel,2);
                accumColor = accumColor.scale(1.0 / samplingPixels);
                rgbData[(j * this.imageWidth + i) * 3] = (byte) (Math.min(255,(int) (255 * (accumColor.x()))));
                rgbData[(j * this.imageWidth + i) * 3 + 1] = (byte) (Math.min(255,(int) (255 * (accumColor.y()))));
                rgbData[(j * this.imageWidth + i) * 3 + 2] = (byte) (Math.min(255,(int) (255 * (accumColor.z()))));



            }
        }
        // Put your ray tracing code here!
        //
        // Write pixel color values in RGB format to rgbData:
        // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
        //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
        //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
        //
        // Each of the red, green and blue components should be a byte, i.e. 0-255

        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;

        // The time is measured for your own conveniece, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

        // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);

        System.out.println("Saved file " + outputFileName);
    }
//prvious code:
//    Vector sendRay(Vector rayOrigin, Vector rayDirection) {
//        double intersectPoint = Double.MAX_VALUE;
//        boolean doesIntersect = false;
//        Surface intersectedSurface = null;
//
//	    /*checking for nearest intersection*/
//        for (Surface surface : this.sceneParser.getSurfaces()) {
//            double point = surface.intersectionPoints(rayOrigin, rayDirection);
//            if (point > 0) {
//                doesIntersect = true;
//                if (intersectPoint > point) {
//                    intersectPoint = point;
//                    intersectedSurface = surface;
//                }
//            }
//        }
//
//        /*no intersection => background color*/
//        //if (!doesIntersect) return new Vector(2);
//        if (intersectedSurface == null) return new Vector(2);
//
//	    /*if there is intersection*/
//        Vector point = rayOrigin.plus(rayDirection.scale(intersectPoint));
//        //Vector normal = intersectedSurface.normal(point);
//        Vector finalColor = intersectedSurface.getSurfaceColor();
//
//        // TODO: Add all the other shit here...
//
//        return finalColor;
//    }

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    /*
    private static void saveImage(int width, byte[] rgbData, String fileName) {
        try {
            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));

        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }
    }

    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    /*
    private static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);
        return result;
    }

    private static class RayTracerException extends Exception {
        public RayTracerException(String msg) {
            super(msg);
        }
    }

    public RayHit findClosestIntersection(Ray ray, Surface ignoreObject) {
        SceneParser scene = this.sceneParser;
        double minDistance = Double.POSITIVE_INFINITY;
        RayHit minHit = null;
        for (Sphere obj : scene.getSpheres()) {
            RayHit hit = obj.getIntersection(ray);
            if (hit != null){
                if (hit.getHitDistance() < minDistance) {
                    if (hit.getHitObject() != ignoreObject) {
                        minDistance = hit.getHitDistance();
                        minHit = hit;

                    }
                }
            }
        }
        return (minHit);
    }




    public Vector findColor(RayHit hit, Light lightSource, int recDepth) {

        Vector bgColor = this.sceneParser.getSettings().getBgColor();
        if(recDepth==this.sceneParser.getSettings().getMaxRecursion() || hit==null)
            return(bgColor);

        Vector finalColor=new Vector(0,0,0);

        recDepth++;

        //light Color for defuse and specular calculations
        Vector lightColorIntensity=lightSource.getColor();
        if(isObscured(hit,lightSource))
            lightColorIntensity=lightColorIntensity.scale(1-lightSource.getShadowIntensity());

        //defuse color
        Vector defuseColor;
        Vector hitMaterialDefuseColor=hit.getHitObject().getMaterial().getDiffuseColor();
        double NLCosAngle;
        Vector hitPointNormal=hit.getHitObject().getNormalAtPoint(hit.getHitPoint());
        Vector lightVectorDirection=lightSource.getPos().minus(hit.getHitPoint()).normalize();
        NLCosAngle = hitPointNormal.dot(lightVectorDirection);
        defuseColor=hitMaterialDefuseColor.scale(NLCosAngle).mult(lightColorIntensity);
        finalColor=finalColor.plus(defuseColor.scale(1-hit.getHitObject().getMaterial().getTransparency()));


        //specular color
        Vector specColor;
        Vector hitMaterialSpecColor=hit.getHitObject().getMaterial().getSpecularColor();
        double phongSpecCoef=hit.getHitObject().getMaterial().getPhongSpecularityCoefficient();
        double VRCosAngle;
        Vector returningLightDirection ;
        Vector hitToEyeDirection = hit.getRay().getDirection().scale(-1.0);

        Ray lightToHit = new Ray(lightSource.getPos(), lightSource.getPos().minus(hit.getHitPoint()));
        RayHit lightToHitHit = new RayHit(lightToHit,hit.getHitObject(),hit.getHitPoint());
        Ray returningLightToHit = lightToHitHit.getReflectionRay();
        VRCosAngle = hitToEyeDirection.dot(returningLightToHit.getDirection());
        specColor = hitMaterialSpecColor.scale(Math.pow(VRCosAngle,phongSpecCoef)).mult(lightColorIntensity);
        finalColor=finalColor.plus(specColor.scale(1-hit.getHitObject().getMaterial().getTransparency()));

        //transmission rays color
        Ray hitRay = hit.getRay();
        Ray transmissionRay = hit.getTransmissionRay();
        RayHit nextTransmissionHit = findClosestIntersection(transmissionRay,hit.getHitObject());
        if(nextTransmissionHit!=null){
            finalColor = finalColor.plus(findColor(nextTransmissionHit,lightSource,recDepth).scale(hit.getHitObject().getMaterial().getTransparency()));
        }
        else{
            finalColor = finalColor.plus(bgColor);
        }

        //reflective rays color
        Vector hitMaterialRefectionColor = hit.getHitObject().getMaterial().getReflectionColor();
        Ray reflectedRay = hit.getReflectionRay();
        RayHit nextReflectedHit = findClosestIntersection(reflectedRay,hit.getHitObject());
        if(nextReflectedHit!=null) {
            finalColor = finalColor.plus(findColor(nextReflectedHit, lightSource, recDepth).mult(hitMaterialRefectionColor));
        }
        else{
            finalColor = finalColor.plus(bgColor);
        }

        return (finalColor);
    }

    public boolean isObscured(RayHit hit, Light lightSource){
        Ray transmissionRay = new Ray(hit.getHitPoint(), lightSource.getPos().minus(hit.getHitPoint()));
        if(findClosestIntersection(transmissionRay,hit.getHitObject())!=null)
            return true;
        return false;
    }





//    public Vector findColor(RayHit hit){
//
//        Vector color =new Vector(0,0,0);
//        int maxRec;
//        for(Light lightSource : this.sceneParser.getLights())
//        {
//            maxRec = sceneParser.getSettings().getMaxRecursion();
//            double transperency;
//            Ray hitToLight = new Ray(hit.getHitPoint(),lightSource.getPos().minus(hit.getHitPoint()));
//            RayHit nextHit = findClosestIntersection(hitToLight,hit.getHitObject());
//
//            if(nextHit!=null) {
//                transperency = hit.getHitObject().material.getTransparency();
//                if (transperency != 0) {
//                    color.plus(findColor(nextHit, maxRec - 1, lightSource).scale(transperency));
//                    color.plus(hit.getHitObject().material.getDiffuseColor()).scale(1 - transperency);
//                    color.plus(hit.getHitObject().material.getSpecularColor().scale((1 - transperency)));
//                } else {
//                    color.plus(this.sceneParser.getSettings().getBgColor());
//                }
//            }
//            else {
//                color.plus(lightSource.getColor().mult((hit.getHitObject().material.getDiffuseColor())));
//                color.plus(lightSource.getColor().mult((hit.getHitObject().material.getSpecularColor())).scale(lightSource.getSpecularIntensity()));
//            }
//
//            }



//        return null;
}

//    private Vector findColor(RayHit hit, int maxRec, Light lightSource) {
//        Vector color = new Vector(0, 0, 0);
//        double transperency;
//        Ray hitToLight = new Ray(hit.getHitPoint(), lightSource.getPos().minus(hit.getHitPoint()));
//        RayHit nextHit = findClosestIntersection(hitToLight, hit.getHitObject());
//
//        if (nextHit != null) {
//            transperency = hit.getHitObject().material.getTransparency();
//            if (transperency != 0) {
//                color.plus(findColor(nextHit, maxRec - 1, lightSource).scale(transperency));
//                color.plus(hit.getHitObject().material.getDiffuseColor()).scale(1 - transperency);
//                color.plus(hit.getHitObject().material.getSpecularColor().scale((1 - transperency)));
//            } else {
//                color.plus(this.sceneParser.getSettings().getBgColor());
//            }
//        } else {
//            color.plus(lightSource.getColor().mult((hit.getHitObject().material.getDiffuseColor())));
//            color.plus(lightSource.getColor().mult((hit.getHitObject().material.getSpecularColor())).scale(lightSource.getSpecularIntensity()));
//        }
//
//        return color;
//    }

**/










