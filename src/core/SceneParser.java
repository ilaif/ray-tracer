package core;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import physicalObjects.*;

/**
 * Created by ilaif on 26/05/2017.
 */
public class SceneParser {

    private ArrayList<String[]> data;
    private Camera camera;
    private Settings settings;
    private ArrayList<Material> materials;
    private ArrayList<Sphere> spheres;
    private ArrayList<Plane> planes;
    private ArrayList<Triangle> triangles;
    private ArrayList<Light> lights;

    public SceneParser(String filepath) {

        this.parseFile(filepath);
        this.camera = parseCamera();
        this.settings = parseSettings();
        this.materials = parseMaterials();
        this.spheres = parseSpheres();
        this.planes = parsePlanes();
        this.triangles = parseTriangles();
        this.lights = parseLights();
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public ArrayList<Material> getMaterials() {
        return this.materials;
    }

    public ArrayList<Sphere> getSpheres() {
        return this.spheres;
    }

    public ArrayList<Plane> getPlanes() {
        return this.planes;
    }

    public ArrayList<Triangle> getTriangles() {
        return this.triangles;
    }

    public List<Surface> getSurfaces() {
        List<Surface> surfaces = new ArrayList<Surface>();
        surfaces.addAll(getPlanes());
        surfaces.addAll(getSpheres());
        surfaces.addAll(getTriangles());
        return surfaces;
    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    private Camera parseCamera() {
        String[] myLine = null;
        for (String[] line : this.data) {
            if (line[0].equals("cam")) {
                myLine = line;
                break;
            }
        }
        if (myLine == null) {
            throw new Error("Camera not found in config file.");
        }

        Vector pos = new Vector(Double.parseDouble(myLine[1]), Double.parseDouble(myLine[2]), Double.parseDouble(myLine[3]));
        Vector lookAtPos = new Vector(Double.parseDouble(myLine[4]), Double.parseDouble(myLine[5]), Double.parseDouble(myLine[6]));
        Vector upVector = new Vector(Double.parseDouble(myLine[7]), Double.parseDouble(myLine[8]), Double.parseDouble(myLine[9]));
        double screenDistance = Double.parseDouble(myLine[10]);
        double screenWidth = Double.parseDouble(myLine[11]);
        return new Camera(pos, lookAtPos, upVector, screenDistance, screenWidth);
    }

    private Settings parseSettings() {
        String[] myLine = null;
        for (String[] line : this.data) {
            if (line[0].equals("set")) {
                myLine = line;
                break;
            }
        }
        if (myLine == null) {
            throw new Error("Settings not found in config file.");
        }

        Vector bgColor = new Vector(Double.parseDouble(myLine[1]), Double.parseDouble(myLine[2]), Double.parseDouble(myLine[3]));
        double rootNumberShadows = Double.parseDouble(myLine[4]);
        int maxRecursion = Integer.parseInt(myLine[5]);
        int superSamplingLevel = Integer.parseInt(myLine[6]);
        return new Settings(bgColor, rootNumberShadows, maxRecursion, superSamplingLevel);
    }

    private ArrayList<Material> parseMaterials() {
        ArrayList<String[]> myLines = new ArrayList<String[]>();
        for (String[] line : this.data) {
            if (line[0].equals("mtl")) {
                myLines.add(line);
            }
        }

        ArrayList<Material> materials = new ArrayList<Material>();
        for (String[] line : myLines) {
            Vector diffuseColor = new Vector(Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]));
            Vector specularColor = new Vector(Double.parseDouble(line[4]), Double.parseDouble(line[5]), Double.parseDouble(line[6]));
            Vector reflectionColor = new Vector(Double.parseDouble(line[7]), Double.parseDouble(line[8]), Double.parseDouble(line[9]));
            double phongSpecularityCoefficient = Double.parseDouble(line[10]);
            double transparency = Double.parseDouble(line[11]);
            materials.add(new Material(diffuseColor, specularColor, reflectionColor, phongSpecularityCoefficient, transparency));
        }

        return materials;
    }

    private ArrayList<Sphere> parseSpheres() {
        ArrayList<String[]> myLines = new ArrayList<String[]>();
        for (String[] line : this.data) {
            if (line[0].equals("sph")) {
                myLines.add(line);
            }
        }

        ArrayList<Sphere> spheres = new ArrayList<>();
        for (String[] line : myLines) {
            Vector centerPos = new Vector(Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]));
            double radius = Double.parseDouble(line[4]);
            //TODO: What if index doesn't exist?
            Material material = getMaterials().get(Integer.parseInt(line[5]) - 1);
            spheres.add(new Sphere(centerPos, radius, material));
        }

        return spheres;
    }

    private ArrayList<Plane> parsePlanes() {
        ArrayList<String[]> myLines = new ArrayList<String[]>();
        for (String[] line : this.data) {
            if (line[0].equals("pln")) {
                myLines.add(line);
            }
        }

        ArrayList<Plane> planes = new ArrayList<Plane>();
        for (String[] line : myLines) {
            Vector normal = new Vector(Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]));
            int offset = Integer.parseInt(line[4]);
            //TODO: What if index doesn't exist?
            Material material = getMaterials().get(Integer.parseInt(line[5]) - 1);
            planes.add(new Plane(normal, offset, material));
        }

        return planes;
    }

    private ArrayList<Triangle> parseTriangles() {
        ArrayList<String[]> myLines = new ArrayList<String[]>();
        for (String[] line : this.data) {
            if (line[0].equals("trg")) {
                myLines.add(line);
            }
        }

        ArrayList<Triangle> triangles = new ArrayList<Triangle>();
        for (String[] line : myLines) {
            Vector vertex1 = new Vector(Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]));
            Vector vertex2 = new Vector(Double.parseDouble(line[4]), Double.parseDouble(line[5]), Double.parseDouble(line[6]));
            Vector vertex3 = new Vector(Double.parseDouble(line[7]), Double.parseDouble(line[8]), Double.parseDouble(line[9]));
            //TODO: What if index doesn't exist?
            Material material = getMaterials().get(Integer.parseInt(line[10]) - 1);
            triangles.add(new Triangle(vertex1, vertex2, vertex3, material));
        }

        return triangles;
    }

    private ArrayList<Light> parseLights() {
        ArrayList<String[]> myLines = new ArrayList<String[]>();
        for (String[] line : this.data) {
            if (line[0].equals("lgt")) {
                myLines.add(line);
            }
        }

        ArrayList<Light> lights = new ArrayList<Light>();
        for (String[] line : myLines) {
            Vector pos = new Vector(Double.parseDouble(line[1]), Double.parseDouble(line[2]), Double.parseDouble(line[3]));
            Vector color = new Vector(Double.parseDouble(line[4]), Double.parseDouble(line[5]), Double.parseDouble(line[6]));
            double specularIntensity = Double.parseDouble(line[7]);
            double shadowIntensity = Double.parseDouble(line[8]);
            double lightRadius = Double.parseDouble(line[9]);
            lights.add(new Light(pos, color, specularIntensity, shadowIntensity, lightRadius));
        }

        return lights;
    }

    private void parseFile(String filepath) {
        BufferedReader br = null;
        FileReader fr = null;

        this.data = new ArrayList<>();

        try {
            fr = new FileReader(filepath);
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader(filepath));

            while ((sCurrentLine = br.readLine()) != null) {
                String[] line = sCurrentLine.split("\\s+");
                if (line.length > 0 && !line[0].startsWith("#")) {
                    this.data.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}


