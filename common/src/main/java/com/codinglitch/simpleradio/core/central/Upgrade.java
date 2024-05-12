package com.codinglitch.simpleradio.core.central;

import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class Upgrade {
    public ResourceLocation identifier;
    public ResourceLocation texture;
    public List<Type> types;

    public Upgrade(ResourceLocation identifier) {
        this(identifier, new ResourceLocation(identifier.getNamespace(), "upgrade/"+identifier.getPath()));
    }
    public Upgrade(ResourceLocation identifier, Type... types) {
        this(identifier, new ResourceLocation(identifier.getNamespace(), "upgrade/"+identifier.getPath()), types);
    }
    public Upgrade(ResourceLocation identifier, ResourceLocation texture) {
        this(identifier, texture, Type.TRANSMITTING);
    }
    public Upgrade(ResourceLocation identifier, ResourceLocation texture, Type... types) {
        this.identifier = identifier;
        this.texture = texture;
        this.types = Arrays.stream(types).toList();
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public enum Type {
        TRANSMITTING("transmitting"),
        RECEIVING("receiving"),
        EMITTING("emitting"),
        LISTENING("listening"),
        POWERING("powering");

        private final String name;
        public String getName() {
            return name;
        }

        Type(String name) {
            this.name = name;
        }
    }
}
