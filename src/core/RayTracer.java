package core;


import physicalObjects.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class RayTracer {

    private int imageWidth;
    private int imageHeight;
    private SceneParser sceneParser;

    public static void main(String[] args) {
        try {
            RayTracer tracer = new RayTracer();

            // Default values:
            tracer.imageWidth = 500;
            tracer.imageHeight = 500;

            if (args.length < 2)
                throw new RayTracer.RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

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
        } catch (RayTracer.RayTracerException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static class RayTracerException extends Exception {
        public RayTracerException(String msg) {
            super(msg);
        }
    }

    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    private void parseScene(String sceneFileName) throws IOException, RayTracer.RayTracerException {

        System.out.println("Started parsing scene file " + sceneFileName);
        this.sceneParser = new SceneParser(sceneFileName);
        System.out.println("Finished parsing scene file " + sceneFileName);
    }

    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    private void renderScene(String outputFileName) {

        long startTime = System.currentTimeMillis();
        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

        Camera camera = this.sceneParser.getCamera();
        int superSamplingLevel = this.sceneParser.getSettings().getSuperSamplingLevel();
        int sampledPixels;
        for (int i = 0; i < this.imageHeight; i++) {
            for (int j = 0; j < this.imageWidth; j++) {
                sampledPixels = 0;
                // Super Sampling
                Vector accumColor = new Vector(0, 0, 0);
                for (int a = 0; a < superSamplingLevel; a++) {
                    for (int b = 0; b < superSamplingLevel; b++) {
                        double w = (double) a / (double) superSamplingLevel;
                        double h = (double) b / (double) superSamplingLevel;
                        Ray rayThroughPixel = camera.constructRayThroughPixel((i + w), (j + h), this.imageWidth, this.imageHeight);
                        RayHit hit = findClosestIntersection(rayThroughPixel, null);

                        /*if (i % 50 == 0 && j % 50 == 0)
                            System.out.println(System.out.format("i: %d, j: %d, Point at world: %s", i, j, rayThroughPixel.getPointAtDistance(10)));*/

                        /*if (hit != null) {
                            accumColor = accumColor.plus(hit.getHitObject().getMaterial().getDiffuseColor());
                            sampledPixels++;
                        }*/

                        for (Light lightSource : this.sceneParser.getLights()) {
                            Vector color = findColor(hit, lightSource, 0);
                            accumColor = accumColor.plus(color);
                        }

                        sampledPixels++;
                    }

                }
                accumColor = accumColor.scale(1.0 / (double) sampledPixels);
                rgbData[(i * this.imageWidth + j) * 3] = (byte) (Math.min(255, (int) (255 * (accumColor.x()))));
                rgbData[(i * this.imageWidth + j) * 3 + 1] = (byte) (Math.min(255, (int) (255 * (accumColor.y()))));
                rgbData[(i * this.imageWidth + j) * 3 + 2] = (byte) (Math.min(255, (int) (255 * (accumColor.z()))));
            }

        }
        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;

        // The time is measured for your own conveniece, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

        // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);

        System.out.println("Saved file " + outputFileName);
    }

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
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
    private static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        return new BufferedImage(cm, raster, false, null);
    }

    public RayHit findClosestIntersection(Ray ray, Surface ignoreObject) {
        SceneParser scene = this.sceneParser;
        double minDistance = Double.POSITIVE_INFINITY;
        RayHit minHit = null;
        for (Surface obj : scene.getSurfaces()) {
            RayHit hit = obj.getIntersection(ray);
            if (hit != null) {
                if (hit.getHitDistance() < minDistance) {
                    if (hit.getHitObject() != ignoreObject) {
                        minDistance = hit.getHitDistance();
                        minHit = hit;
                    }
                }
            }
        }
        return minHit;
    }

    public Vector findColor(RayHit hit, Light lightSource, int recDepth) {
        Vector bgColor = this.sceneParser.getSettings().getBgColor();
        if (recDepth == this.sceneParser.getSettings().getMaxRecursion() || hit == null)
            return bgColor;

        Vector finalColor = new Vector(0, 0, 0);

        recDepth++;

        Vector hitPoint = hit.getHitPoint();
        Surface hitObject = hit.getHitObject();
        Material hitObjectMaterial = hitObject.getMaterial();

        //light Color for defuse and specular calculations
        Vector lightColorIntensity = lightSource.getColor();
        Vector lightPos = lightSource.getPos();
        RayHit obscuredBy = obscuredBy(hit, lightSource);
        if (obscuredBy != null && obscuredBy.getHitObject() == hitObject) {
            //&& obscuredBy.getHitPoint().minus(hit.getHitPoint()).magnitude() > 0.001
            return finalColor;
        } else if (obscuredBy != null) {
            /*double softShadow = getSoftShadow(lightSource, hit, this.sceneParser.getSettings().getRootNumberShadowRays());
            if (softShadow == 0) {
                lightColorIntensity = lightColorIntensity.scale(lightSource.getShadowIntensity());
            } else if (softShadow != 1) {
                lightColorIntensity = lightColorIntensity.scale(((1 - lightSource.getShadowIntensity()) + softShadow) / 2);
            }*/
            lightColorIntensity = lightColorIntensity.scale(1 - lightSource.getShadowIntensity());
        }

        //defuse color
        Vector hitPointNormal = hitObject.getNormalAtPoint(hitPoint);
        Vector lightVectorDirection = new Vector(hitPoint, lightPos).normalize();
        double NLCosAngle = Math.abs(hitPointNormal.dot(lightVectorDirection));

        Vector hitMaterialDefuseColor = hitObjectMaterial.getDiffuseColor();
        Vector defuseColor = hitMaterialDefuseColor.scale(NLCosAngle).mult(lightColorIntensity);
        double objTransparency = hitObjectMaterial.getTransparency();
        finalColor = finalColor.plus(defuseColor.scale(1 - objTransparency));

        //specular color
        lightColorIntensity = lightColorIntensity.scale(lightSource.getSpecularIntensity());  // For specular we have intensity
        double phongSpecCoef = hitObjectMaterial.getPhongSpecularityCoefficient();
        Vector hitMaterialSpecColor = hitObjectMaterial.getSpecularColor();
        Vector hitToEyeDirection = hit.getRay().getDirection().negate();
        Ray lightToHit = new Ray(lightPos, new Vector(lightPos, hitPoint));
        RayHit lightToHitHit = new RayHit(lightToHit, hitObject, hitPoint);
        Ray returningLightToHit = lightToHitHit.getReflectionRay();
        double VRCosAngle = hitToEyeDirection.dot(returningLightToHit.getDirection());
        Vector specColor = hitMaterialSpecColor.scale(Math.pow(VRCosAngle, phongSpecCoef)).mult(lightColorIntensity);
        finalColor = finalColor.plus(specColor.scale(1 - objTransparency));

        //transmission rays color
        Ray transmissionRay = hit.getTransmissionRay();
        RayHit nextTransmissionHit = findClosestIntersection(transmissionRay, hitObject);
        Vector nextColor;
        if (nextTransmissionHit != null && hitObjectMaterial.getTransparency() > 0) {
            nextColor = findColor(nextTransmissionHit, lightSource, recDepth);
            finalColor = finalColor.plus(nextColor.scale(hitObjectMaterial.getTransparency()));
        } else if (hitObjectMaterial.getTransparency() > 0) {
            nextColor = bgColor;
            finalColor = finalColor.plus(nextColor.scale(hitObjectMaterial.getTransparency()));
        }

        //reflective rays color
        Ray reflectedRay = hit.getReflectionRay();
        RayHit nextReflectedHit = findClosestIntersection(reflectedRay, hitObject);
        Vector reflectionColor = hitObject.getMaterial().getReflectionColor();
        if (nextReflectedHit != null) {
            nextColor = findColor(nextReflectedHit, lightSource, recDepth);
        } else {
            nextColor = bgColor;
        }
        finalColor = finalColor.plus(nextColor.mult(reflectionColor).scale(lightSource.getSpecularIntensity()));

        return finalColor;
    }

    /**
     * Does the light source not hit the hit object directly.
     *
     * @param hit
     * @param lightSource
     * @return
     */
    public RayHit obscuredBy(RayHit hit, Light lightSource) {
        Ray rayHitToLight = new Ray(hit.getHitPoint(), new Vector(hit.getHitPoint(), lightSource.getPos()));
        return findClosestIntersection(rayHitToLight, null);
    }

    public double getSoftShadow(Light lightSource, RayHit hit, double numShadowRays) {
        double length = lightSource.getLightRadius();
        double cellLength = length / numShadowRays;

        Vector lightToHit = new Vector(lightSource.getPos(), hit.getHitPoint()).normalize();
        Vector x = new Vector(-lightToHit.y(), lightToHit.x(), 0).normalize();
        Vector groundBase = lightSource.getPos();
        groundBase = groundBase.minus(lightToHit.scale(length / 2)).minus(x.scale(length / 2));

        Random r = new Random();
        int cou = 0;
        for (int i = 0; i < numShadowRays; i++) {
            for (int j = 0; j < numShadowRays; j++) {
                double rndI = r.nextDouble();
                double rndJ = r.nextDouble();
                Vector cell = groundBase.plus(lightToHit.scale((i + rndI) * cellLength)).plus(x.scale((j + rndJ) * cellLength));
                Ray ray = new Ray(cell, new Vector(cell, hit.getHitPoint()));
                RayHit cellToHit = findClosestIntersection(ray, null);
                if (cellToHit != null && cellToHit.getHitObject() == hit.getHitObject()) {
                    cou++;
                }
            }
        }
        return cou / (numShadowRays * numShadowRays);
    }

}
