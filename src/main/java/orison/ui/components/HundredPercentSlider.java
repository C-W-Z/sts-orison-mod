package orison.ui.components;

import java.util.function.Consumer;

public class HundredPercentSlider extends CustomRangeSlider {
    public HundredPercentSlider(float x, float y, float initialValue, Consumer<Float> onChange) {
        super(x, y, initialValue, 0, 1, 0, true, onChange);
    }
}
