package net.philocraft.constants;

import java.awt.Color;

public enum Colors {
    DISCORD(Color.decode("#5865f2")),
    SUCCESS(Color.decode("#6fed66")),
    FAILURE(Color.decode("#ed6666")),
    WARNING(Color.decode("#f2e055")),
    COMMON(Color.decode("#66bced")),
    HIGHTLIGHT(Color.decode("#463be3")),
    OBFUSCATE(Color.decode("#858585"));

    private Color color;

    private Colors(Color color) {
        this.color = color;
    }

    public Color getValue() {
        return this.color;
    }

    public int getHashCode() {
        return this.color.hashCode();
    }
}
