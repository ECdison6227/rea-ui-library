package study.style.reference;

import android.graphics.Color;

/** Semantic values; B declarations and A baseline mapping: ../baseline/spec.md. */
public final class Tokens {
    public final boolean night;
    public final int page, surface, text, secondary, primary, navigation, divider;
    public Tokens(boolean night) {
        this.night = night;
        page = Color.parseColor(night ? "#000000" : "#F6F7F9");
        surface = Color.parseColor(night ? "#191919" : "#FFFFFF");
        text = Color.parseColor(night ? "#E6E6E6" : "#000000");
        secondary = Color.parseColor(night ? "#808080" : "#A6A6A6");
        primary = Color.parseColor(night ? "#F04D18" : "#FF612E");
        navigation = Color.parseColor(night ? "#242424" : "#FFFFFF");
        divider = Color.parseColor(night ? "#303030" : "#E8E8E8");
    }
    public String mode() { return night ? "night" : "day"; }
    public String rnMode() { return night ? "dark" : "light"; }
    private Tokens() { this(false); }
}
