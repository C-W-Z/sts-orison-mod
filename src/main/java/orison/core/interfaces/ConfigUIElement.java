package orison.core.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ConfigUIElement {
    void update();

    void render(SpriteBatch sb);

    void setTargetY(float targetY);

    float getHeight();
}
