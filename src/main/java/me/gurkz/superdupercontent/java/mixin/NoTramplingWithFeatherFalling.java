/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.java.mixin;

import me.gurkz.superdupercontent.mixin.FeatherFallingTramplingKt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
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
        FeatherFallingTramplingKt.disableTramplingWithFeatherFalling(level, entity, ci);
    }
}