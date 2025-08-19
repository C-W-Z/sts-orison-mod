package orison.core.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ConfigUIElement {
    void setTargetY(float targetY);

    void update();

    void render(SpriteBatch sb);

    float getHeight();
}
