package pl.bkkuc.purutils.builders.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EffectBuilder {

    private final LivingEntity target;

    private final List<Effect> effects;

    public EffectBuilder(@NotNull LivingEntity livingEntity){
        this.target = livingEntity;
        this.effects = new ArrayList<>();
    }

    public EffectBuilder addPotionEffect(PotionEffectType potionEffectType, int durationInSeconds, int power) {
        power--;
        if(power <= 0) power = 0;

        effects.add(new Effect(potionEffectType, durationInSeconds, power));
        return this;
    }

    public void giveEffects() {
        effects.forEach(effect -> target.addPotionEffect(new PotionEffect(effect.getPotionEffectType(), effect.getDuration() * 20, effect.getPower())));
    }

    public static class Effect {

        private final PotionEffectType potionEffectType;
        private final int duration;
        private final int power;

        public Effect(PotionEffectType potionEffectType, int duration, int power) {
            this.potionEffectType = potionEffectType;
            this.duration = duration;
            this.power = power;
        }

        public PotionEffectType getPotionEffectType() {
            return potionEffectType;
        }

        public int getDuration() {
            return duration;
        }

        public int getPower() {
            return power;
        }
    }
}
