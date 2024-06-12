package pl.bkkuc.purutils.builders.impl;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.purutils.ColorUtility;

public class EntityBuilder {

    @Nullable Location location;
    @Nullable Entity entity;
    EntityType entityType;

    @Nullable EquipmentBuilder equipmentBuilder;

    double health;
    double maxHealth;

    String customName;

    boolean glowing;
    boolean baby;

    public EntityBuilder(EntityType entityType){
        this.entityType = entityType;
    }

    public EntityBuilder location(Location location){
        this.location = location;
        return this;
    }

    public EntityBuilder type(EntityType entityType){
        this.entityType = entityType;
        return this;
    }

    public EntityBuilder health(double health) {
        if(health <= 0) health = 0;
        this.health = health;
        return this;
    }

    public EntityBuilder maxHealth(double maxHealth){
        if(maxHealth <= 0) maxHealth = 0;
        this.maxHealth = maxHealth;
        return this;
    }

    public EntityBuilder customName(String customName){
        this.customName = customName;
        return this;
    }

    public EntityBuilder glowing(boolean glowing){
        this.glowing = glowing;
        return this;
    }

    public EntityBuilder baby(boolean baby){
        this.baby = baby;
        return this;
    }

    public EntityBuilder equipmentBuilder(EquipmentBuilder equipmentBuilder){
        this.equipmentBuilder = equipmentBuilder;
        return this;
    }

    public Entity spawn(){
        if(location == null) {
            throw new NullPointerException("Entity or Location or EntityType cannot be a null");
        }
        Entity ent = location.getWorld().spawnEntity(location, entityType == null ? EntityType.PIG : entityType);

        if(customName != null){
            ent.setCustomNameVisible(true);
            ent.setCustomName(ColorUtility.colorize(customName));
        }

        ent.setGlowing(glowing);

        if(ent instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) ent;
            double finalHealth = maxHealth != 0 ? Math.min(health, maxHealth) : health;
            if (finalHealth > 0) {
                livingEntity.setMaxHealth(finalHealth);
                livingEntity.setHealth(finalHealth);
            }
            if(equipmentBuilder != null){
                equipmentBuilder.apply(livingEntity);
            }

            if (livingEntity instanceof Ageable) {
                Ageable ageable = (Ageable) livingEntity;
                if(baby){
                    ageable.setBaby();
                }
                else {
                    ageable.setAdult();
                }
            }
        }
        entity = ent;
        return ent;
    }

    public @Nullable Location location() {
        return this.location;
    }

    public @Nullable Entity entity() {
        return this.entity;
    }

    public @Nullable EntityType entityType() {
        return this.entityType;
    }

    public @Nullable EquipmentBuilder equipmentBuilder(){
        return this.equipmentBuilder;
    }

    public double health() {
        return this.health;
    }

    public double maxHealth() {
        return this.maxHealth;
    }

    public String customName() {
        return this.customName;
    }

    public boolean glowing() {
        return this.glowing;
    }

    public boolean baby() {
        return this.baby;
    }
}
