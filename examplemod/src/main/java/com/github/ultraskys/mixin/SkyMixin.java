package com.github.ultraskys.mixin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Logger;
import com.github.ultraskys.DaySky;
import com.github.ultraskys.SharedData;
import static com.github.ultraskys.UltraSkys.LOGGER;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import finalforeach.cosmicreach.rendering.shaders.SkyStarShader;
import org.spongepowered.asm.mixin.Mixin;
import finalforeach.cosmicreach.world.Sky;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;


@Mixin(Sky.class)
public class SkyMixin {
    @Shadow
    private static boolean shouldDrawStars;
    @Shadow
    private static Mesh starMesh;
    @Shadow
    private static GameShader starShader;
    @Shadow public static Color skyColor;

    @Inject(method = "drawStars", at = @At("HEAD"))
    private static void drawStars0(Camera worldCamera, CallbackInfo ci) {
        SharedData sharedData = SharedData.getInstance();
        // WARNING: DrawStars is on loop aka multi-thread =-=
        if(Objects.equals(String.valueOf(Sky.skyColor), "4c99ccff")){
            DaySky.renderClouds(worldCamera);
        }


        //LOGGER.info(String.valueOf(Sky.skyColor));
        if(sharedData.Updated()){
            Gdx.gl.glDepthMask(false);
            if (starMesh != null) {
                starMesh.dispose();
                starMesh = null;
            }
            starShader = SkyStarShader.SKY_STAR_SHADER;
            VertexAttribute[] attribs = new VertexAttribute[]{VertexAttribute.Position(), VertexAttribute.ColorPacked()};
            FloatArray verts = new FloatArray();
            int numStars = SharedData.getNumStars();
            Vector3 pointOff = new Vector3();
            Vector3 pointA = new Vector3();
            Vector3 pointB = new Vector3();
            Vector3 pointC = new Vector3();
            Vector3 pointD = new Vector3();

            for (int maxVert = 0; maxVert < numStars; ++maxVert) {
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
                verts.add(pointC.x, pointC.y, pointC.z, Color.YELLOW.toFloatBits());
                verts.add(pointB.x, pointB.y, pointB.z, Color.YELLOW.toFloatBits());
                verts.add(pointA.x, pointA.y, pointA.z, Color.YELLOW.toFloatBits());
                verts.add(pointD.x, pointD.y, pointD.z, Color.YELLOW.toFloatBits());
                verts.add(pointB.x, pointB.y, pointB.z, Color.YELLOW.toFloatBits());
                verts.add(pointC.x, pointC.y, pointC.z, Color.YELLOW.toFloatBits());
            }

            int maxVert = verts.size / attribs.length;
            starMesh = new Mesh(true, maxVert, 0, attribs);
            starMesh.setVertices(verts.toArray());

            if (starMesh != null) {
                starShader.bind(worldCamera);
                starMesh.bind(starShader.shader);
                starMesh.render(starShader.shader, 4);
                SharedData.setUpdated(false);
                Gdx.gl.glDepthMask(true);
            }

        }

    }
}

