/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.java.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public abstract class NoTramplingWithFeatherFalling {
    @Inject(
            method = "fallOn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void disableTramplingWithFeatherFalling(
            Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci
    ) {
        if (!(entity instanceof LivingEntity living)) return;

        Holder.Reference<Enchantment> featherFalling = level
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FEATHER_FALLING);

        int featherFallingLevel = EnchantmentHelper.getEnchantmentLevel(featherFalling, living);

        if (featherFallingLevel > 0 || living.hasEffect(MobEffects.SLOW_FALLING)) ci.cancel();
    }
}