package pl.bkkuc.purutils.builders.impl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParticleBuilder {

    private Particle particle;
    private int count;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double extra;
    private Object data;

    public ParticleBuilder particle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public ParticleBuilder count(int count) {
        this.count = count;
        return this;
    }

    public ParticleBuilder offsetX(double offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public ParticleBuilder offsetY(double offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public ParticleBuilder offsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    public ParticleBuilder extra(double extra) {
        this.extra = extra;
        return this;
    }

    public ParticleBuilder data(Object data) {
        this.data = data;
        return this;
    }

    public void spawn(@NotNull Location location) {
        if (location.getWorld() == null) return;
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Nullable
    public static ParticleBuilder fromConfiguration(ConfigurationSection section) {
        if (section == null) return null;

        ParticleBuilder builder = new ParticleBuilder();

        String particleName = section.getString("particle");
        if (particleName != null) {
            try {
                builder.particle(Particle.valueOf(particleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else {
            return null;
        }

        builder.count(section.getInt("count", 1));
        builder.offsetX(section.getDouble("offsetX", 0));
        builder.offsetY(section.getDouble("offsetY", 0));
        builder.offsetZ(section.getDouble("offsetZ", 0));
        builder.extra(section.getDouble("extra", 0));
        builder.data(section.get("data"));

        return builder;
    }
}