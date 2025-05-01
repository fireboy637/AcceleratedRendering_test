package com.github.argon4w.acceleratedrendering.core.utils;

import net.minecraft.util.ARGB;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4i;

public class Vertex {

    private final Vector3f position;
    private final Vector2f uv;
    private final Vector4i color;
    private final Vector2i light;
    private final Vector3f normal;

    public Vertex() {
        this.position = new Vector3f();
        this.uv = new Vector2f();
        this.color = new Vector4i();
        this.light = new Vector2i();
        this.normal = new Vector3f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getUV() {
        return uv;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector4i getColor() {
        return color;
    }

    public Vector2i getLight() {
        return light;
    }

    public int getPackedLight() {
        return light.x | light.y << 16;
    }

    public int getPackedColor() {
        return ARGB.color(
                color.w,
                color.x,
                color.y,
                color.z
        );
    }
}
