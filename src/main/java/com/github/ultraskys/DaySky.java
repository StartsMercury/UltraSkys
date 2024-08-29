package com.github.ultraskys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DaySky {

    private static Mesh cloudMesh;
    public static Cloudshader cloudShader;
    private float[] tmpVec3 = new float[3];

    public static void renderClouds(Camera WorldCamera){
        ShaderProgram shader = Cloudshader.CLOUD_SHADER.shader;

        // INFO: Render Gsl shaders for clouds and render to player position on screen


        Gdx.gl.glDepthMask(false);
        if (cloudMesh == null) {
            cloudShader = Cloudshader.CLOUD_SHADER;
            VertexAttribute[] attribs = new VertexAttribute[]{VertexAttribute.Position()};

            Quaternion quaternion = new Quaternion();
            FloatArray verts = new FloatArray();
            int numClouds = 45; // Increased number of clouds to cover the whole sky
            Vector3 center = new Vector3();
            Vector3 pointA = new Vector3();
            Vector3 pointB = new Vector3();
            Vector3 pointC = new Vector3();
            Vector3 pointD = new Vector3();

            float minDistance = 1.0f; // Reduced minimum distance between clouds for denser cloud cover

            int maxVert;
            for (maxVert = 0; maxVert < numClouds; ++maxVert) {
                quaternion.setEulerAnglesRad(MathUtils.random(MathUtils.PI2), 0.0F, 0.0F);
                center.set(MathUtils.random(-100.0F, 100.0F), MathUtils.random(128.0F, 192.0F), MathUtils.random(-100.0F, 100.0F)); // Randomized cloud positions more aggressively to cover the whole sky

                float radius = 8.0F; // Increased size range for larger clouds

                quaternion.transform(pointA.set(-radius, 0.0F, -radius).add(center));
                quaternion.transform(pointB.set(radius, 0.0F, -radius).add(center));
                quaternion.transform(pointC.set(-radius, 0.0F, radius).add(center));
                quaternion.transform(pointD.set(radius, 0.0F, radius).add(center));

                // Downward Faces
                verts.add(pointC.x, pointC.y, pointC.z);
                verts.add(pointB.x, pointB.y, pointB.z);
                verts.add(pointD.x, pointD.y, pointD.z);
                verts.add(pointA.x, pointA.y, pointA.z);
                verts.add(pointB.x, pointB.y, pointB.z);
                verts.add(pointC.x, pointC.y, pointC.z);

                // Upward Faces
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
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        cloudMesh.render(shader, GL20.GL_TRIANGLES);
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
