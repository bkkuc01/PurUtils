package pl.bkkuc.purutils.builders.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EquipmentBuilder {

    @Nullable LivingEntity livingEntity;

    @Nullable ItemStack helmet;
    @Nullable ItemStack chestplate;
    @Nullable ItemStack leggings;
    @Nullable ItemStack boots;
    @Nullable ItemStack mainHand;
    @Nullable ItemStack offHand;

    public EquipmentBuilder() {}

    public EquipmentBuilder(@Nullable LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public EquipmentBuilder livingEntity(@Nullable LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        return this;
    }

    public @Nullable LivingEntity livingEntity() { return this.livingEntity; }

    public EquipmentBuilder helmet(@Nullable ItemStack helmet){
        this.helmet = helmet;
        return this;
    }

    public @Nullable ItemStack helmet() { return this.helmet; }

    public EquipmentBuilder chestplate(@Nullable ItemStack chestplate){
        this.chestplate = chestplate;
        return this;
    }

    public @Nullable ItemStack chestplate() { return this.chestplate; }

    public EquipmentBuilder leggings(@Nullable ItemStack leggings){
        this.leggings = leggings;
        return this;
    }

    public @Nullable ItemStack leggings() { return this.leggings; }

    public EquipmentBuilder boots(@Nullable ItemStack boots){
        this.boots = boots;
        return this;
    }

    public @Nullable ItemStack boots() { return this.boots; }

    public EquipmentBuilder mainHand(@Nullable ItemStack mainHand){
        this.mainHand = mainHand;
        return this;
    }

    public @Nullable ItemStack mainHand() { return this.mainHand; }

    public EquipmentBuilder offHand(@Nullable ItemStack offHand){
        this.offHand = offHand;
        return this;
    }

    public @Nullable ItemStack offHand() { return this.offHand; }


    /**
     * Apply equipments to living entity.
     * @param livingEntity Entity which get equipments
     */
    public void apply(@Nullable LivingEntity livingEntity) {
        @Nullable LivingEntity toApply = livingEntity == null ? this.livingEntity == null ? null : this.livingEntity : livingEntity;
        if(toApply == null) {
            throw new NullPointerException("Entity cannot be a null");
        }
        EntityEquipment equipment = toApply.getEquipment();
        if(equipment == null) return;

        if(helmet != null) equipment.setHelmet(helmet);
        if(chestplate != null) equipment.setChestplate(chestplate);
        if(leggings != null) equipment.setLeggings(leggings);
        if(boots != null) equipment.setBoots(boots);
        if(mainHand != null) equipment.setItemInMainHand(mainHand);
        if(offHand != null) equipment.setItemInOffHand(offHand);
    }

    public void apply() throws NullPointerException {
        if(this.livingEntity == null) {
            throw new NullPointerException("Entity to apply it's null.");
        }

        apply(this.livingEntity);
    }
}
