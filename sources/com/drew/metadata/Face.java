package com.drew.metadata;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/metadata/Face.class */
public class Face {
    private final int _x;
    private final int _y;
    private final int _width;
    private final int _height;

    @Nullable
    private final String _name;

    @Nullable
    private final Age _age;

    public Face(int x, int y, int width, int height, @Nullable String name, @Nullable Age age) {
        this._x = x;
        this._y = y;
        this._width = width;
        this._height = height;
        this._name = name;
        this._age = age;
    }

    public int getX() {
        return this._x;
    }

    public int getY() {
        return this._y;
    }

    public int getWidth() {
        return this._width;
    }

    public int getHeight() {
        return this._height;
    }

    @Nullable
    public String getName() {
        return this._name;
    }

    @Nullable
    public Age getAge() {
        return this._age;
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Face face = (Face) o;
        if (this._height != face._height || this._width != face._width || this._x != face._x || this._y != face._y) {
            return false;
        }
        if (this._age != null) {
            if (!this._age.equals(face._age)) {
                return false;
            }
        } else if (face._age != null) {
            return false;
        }
        return this._name != null ? this._name.equals(face._name) : face._name == null;
    }

    public int hashCode() {
        int result = this._x;
        return (31 * ((31 * ((31 * ((31 * ((31 * result) + this._y)) + this._width)) + this._height)) + (this._name != null ? this._name.hashCode() : 0))) + (this._age != null ? this._age.hashCode() : 0);
    }

    @NotNull
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("x: ").append(this._x);
        result.append(" y: ").append(this._y);
        result.append(" width: ").append(this._width);
        result.append(" height: ").append(this._height);
        if (this._name != null) {
            result.append(" name: ").append(this._name);
        }
        if (this._age != null) {
            result.append(" age: ").append(this._age.toFriendlyString());
        }
        return result.toString();
    }
}
