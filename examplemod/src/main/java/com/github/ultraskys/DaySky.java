package com.github.ultraskys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import finalforeach.cosmicreach.rendering.shaders.SkyStarShader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DaySky {

    private static Mesh cloudMesh;
    public static Cloudshader cloudShader;
    private float[] tmpVec3 = new float[3];
    static ShaderProgram shader = Cloudshader.CLOUD_SHADER.shader;
    public static void renderClouds(Camera WorldCamera){
        // INFO: Render Gsl shaders for clouds and render to player position on screen
        Cloudshader.initCloudShader();

        Gdx.gl.glDepthMask(false);
        if (cloudMesh == null) {
            cloudShader = Cloudshader.CLOUD_SHADER;
            VertexAttribute[] attribs = new VertexAttribute[]{VertexAttribute.Position()};
            FloatArray verts = new FloatArray();
            int numStars = 1000;
            Vector3 pointOff = new Vector3();
            Vector3 pointA = new Vector3();
            Vector3 pointB = new Vector3();
            Vector3 pointC = new Vector3();
            Vector3 pointD = new Vector3();

            int maxVert;
            for(maxVert = 0; maxVert < numStars; ++maxVert) {
                float s = MathUtils.random(0.01F, 0.05F) / 2.0F;
                float ax = MathUtils.random(360.0F);
                float ay = MathUtils.random(360.0F);
                float az = MathUtils.random(360.0F);
                pointA.set(0.0F, 0.0F, 0.0F);
                pointA.rotate(ax, 1.0F, 0.0F, 0.0F);
                pointA.rotate(ay, 0.0F, 1.0F, 0.0F);
                pointA.rotate(az, 0.0F, 0.0F, 1.0F);
                pointB.set(s, 0.0F, 0.0F);
                pointB.rotate(ax, 1.0F, 0.0F, 0.0F);
                pointB.rotate(ay, 0.0F, 1.0F, 0.0F);
                pointB.rotate(az, 0.0F, 0.0F, 1.0F);
                pointC.set(0.0F, s, 0.0F);
                pointC.rotate(ax, 1.0F, 0.0F, 0.0F);
                pointC.rotate(ay, 0.0F, 1.0F, 0.0F);
                pointC.rotate(az, 0.0F, 0.0F, 1.0F);
                pointD.set(s, s, 0.0F);
                pointD.rotate(ax, 1.0F, 0.0F, 0.0F);
                pointD.rotate(ay, 0.0F, 1.0F, 0.0F);
                pointD.rotate(az, 0.0F, 0.0F, 1.0F);
                pointOff.set(0.0F, 0.0F, 5.0F);
                pointOff.rotate(ax, 1.0F, 0.0F, 0.0F);
                pointOff.rotate(ay, 0.0F, 1.0F, 0.0F);
                pointOff.rotate(az, 0.0F, 0.0F, 1.0F);
                pointA.add(pointOff);
                pointB.add(pointOff);
                pointC.add(pointOff);
                pointD.add(pointOff);
                verts.add(pointC.x, pointC.y, pointC.z);
                verts.add(pointB.x, pointB.y, pointB.z);
                verts.add(pointA.x, pointA.y, pointA.z);
                verts.add(pointD.x, pointD.y, pointD.z);
                verts.add(pointB.x, pointB.y, pointB.z);
                verts.add(pointC.x, pointC.y, pointC.z);
            }

            maxVert = verts.size / attribs.length;
            cloudMesh = new Mesh(true, maxVert, 0, attribs);
            cloudMesh.setVertices(verts.toArray());
        }

        cloudShader.bind(WorldCamera);
        cloudMesh.bind(shader);
        cloudMesh.render(shader, 4);
        Gdx.gl.glDepthMask(true);
    }




    public void bindOptionalUniform3f(String uniformName, float x, float y, float z) {
        //int u =  cloudShader.getUniformLocation(uniformName);
        //if (u != -1) {
            this.tmpVec3[0] = x;
            this.tmpVec3[1] = y;
            this.tmpVec3[2] = z;
            //cloudShader.setUniform3fv(u, this.tmpVec3, 0, 3);
        //}

    }





    private static String loadShaderFromFile(String shaderPath) {
        InputStream inputStream = UltraSkys.class.getResourceAsStream("/" + shaderPath);

        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder shaderContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    shaderContent.append(line).append("\n");
                }
                return shaderContent.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Shader file not found: " + shaderPath);
        }

        return null;
    }

    public static void loadShader() {
        String vertexShaderPath = "com/github/ultraskys/shaders/shader.vert";
        String fragmentShaderPath = "com/github/ultraskys/shaders/shader.frag";

        String vertexShaderContent = loadShaderFromFile(vertexShaderPath);

        String fragmentShaderContent = loadShaderFromFile(fragmentShaderPath);

        //cloudShader = new ShaderProgram(vertexShaderContent, fragmentShaderContent);

        //if (!cloudShader.isCompiled()) {
          // UltraSkys.LOGGER.info("Failed to compile");
       // }


    }
}
