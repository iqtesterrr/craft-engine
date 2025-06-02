package net.momirealms.craftengine.core.util;

import java.util.Arrays;

public class Color {
    private static final byte DEFAULT_ALPHA = (byte) 255;
    private final byte r;
    private final byte g;
    private final byte b;
    private final byte a;

    public Color(byte r, byte g, byte b, byte a) {
        this.b = b;
        this.g = g;
        this.r = r;
        this.a = a;
    }

    public Color(byte r, byte g, byte b) {
        this(r, g, b, DEFAULT_ALPHA);
    }

    public static Color fromString(String[] strings) {
        if (strings.length == 3) {
            return new Color(Byte.parseByte(strings[0]), Byte.parseByte(strings[1]), Byte.parseByte(strings[2]));
        } else if (strings.length == 4) {
            return new Color(Byte.parseByte(strings[0]), Byte.parseByte(strings[1]), Byte.parseByte(strings[2]), Byte.parseByte(strings[3]));
        } else {
            throw new IllegalArgumentException("Invalid color format: " + Arrays.toString(strings));
        }
    }

    public byte a() {
        return a;
    }

    public byte b() {
        return b;
    }

    public byte g() {
        return g;
    }

    public byte r() {
        return r;
    }
}
