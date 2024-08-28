package com.github.ultraskys;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.audio.SoundManager;
import finalforeach.cosmicreach.ui.UIElement;

import java.util.Arrays;

public class IntsSlider extends UIElement {
    // ASSERTION: nonnull, +2 items, unique, sorted
    private final int[] elements;

    private int index;

    public IntsSlider(int[] elements, float x, float y, float w, float h) {
        this(elements, x, y, w, h, true);
    }

    public IntsSlider(int[] elements, float x, float y, float w, float h, boolean triggerOnCreate) {
        super(x, y, w, h, triggerOnCreate);

        this.elements = elements;

        onValueUpdate(elements[0]);
    }

    protected void onValueUpdate(int value) {}

    public int getValue() {
        return this.elements[this.index];
    }

    public void setIndexAtOrBefore(final int value) {
        int lastIndex = this.index;
        int location = Arrays.binarySearch(elements, value);
        int newIndex = this.index = location >= 0 ? location : -1 + Math.min(-location, elements.length);
        if (lastIndex != newIndex) {
            updateText();
        }
    }

    // Adapted from finalforeach.cosmicreach.ui.UISlider.drawBackground (v1.0.50)
    @Override
    public void drawBackground(Viewport uiViewport, SpriteBatch batch, float mouseX, float mouseY) {
        super.drawBackground(uiViewport, batch, mouseX, mouseY);

        int index = this.index;
        if (this.isHeld) {
            float maxX = this.getDisplayX(uiViewport);
            int max = this.elements.length - 1;

            int approx;
            if (this.w <= max) {
                // As if the width was bigger making all elements visitable
                approx = Math.round(mouseX - maxX);
            } else {
                approx = Math.round(max * (mouseX - maxX) / this.w);
            }

            int newIndex = MathUtils.clamp(approx,0, max);
            if (index != newIndex) {
                index = this.index = newIndex;
                this.onValueUpdate(this.elements[index]);
                SoundManager.INSTANCE.playSound(onHoverSound);
            }
        }

        this.buttonTex = UIElement.uiPanelTex;
        this.drawKnobBackground(uiViewport, batch, index);
    }

    // Adapted from finalforeach.cosmicreach.ui.UISlider (v1.0.50)
    protected void drawKnobBackground(Viewport uiViewport, SpriteBatch batch, int index) {
        super.drawElementBackground(uiViewport, batch);
        float x = this.getDisplayX(uiViewport);
        float y = this.getDisplayY(uiViewport);
        float ratio = (float) index / (this.elements.length - 1);
        float knobW = 10.0F;
        float knobH = this.h + 8.0F;
        float knobX = x + ratio * this.w - knobW / 2.0F;
        float knobY = y - 4.0F;
        batch.draw(uiPanelHoverBoundsTex, knobX, knobY, 1.0F, 1.0F, knobW, knobH, 1.0F, 1.0F, 0.0F, 0, 0, this.buttonTex.getWidth(), this.buttonTex.getHeight(), false, true);
        batch.draw(uiPanelTex, knobX + 1.0F, knobY + 1.0F, 1.0F, 1.0F, knobW - 2.0F, knobH - 2.0F, 1.0F, 1.0F, 0.0F, 0, 0, this.buttonTex.getWidth(), this.buttonTex.getHeight(), false, true);
    }
}
