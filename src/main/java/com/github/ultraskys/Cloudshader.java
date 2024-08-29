package com.github.ultraskys;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import finalforeach.cosmicreach.rendering.shaders.GameShader;

public class Cloudshader extends GameShader {
    public static Cloudshader CLOUD_SHADER;
    private static VertexAttribute posAttrib = VertexAttribute.Position();
    public static VertexAttribute[] allVertexAttributes;
    PerspectiveCamera skyCam = new PerspectiveCamera();

    public void bind(Camera worldCamera) {
        super.bind(worldCamera);
        this.skyCam.up.set(worldCamera.up);
        this.skyCam.direction.set(worldCamera.direction);
        this.skyCam.fieldOfView = ((PerspectiveCamera)worldCamera).fieldOfView;
        this.skyCam.position.set(worldCamera.position);
        this.skyCam.near = 1.0F;
        this.skyCam.far = 1000.0F;
        this.skyCam.viewportWidth = worldCamera.viewportWidth;
        this.skyCam.viewportHeight = worldCamera.viewportHeight;
        this.skyCam.update();
        this.shader.setUniformMatrix("u_projViewTrans", this.skyCam.combined);
    }

    public static void initCloudShader() {
        CLOUD_SHADER = new Cloudshader("shader.vert", "shader.frag");
    }

    static {
        allVertexAttributes = new VertexAttribute[]{posAttrib};
    }

    public Cloudshader(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
    }
}
