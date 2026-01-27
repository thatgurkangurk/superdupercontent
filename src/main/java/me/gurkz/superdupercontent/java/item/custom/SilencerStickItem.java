/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.java.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SilencerStickItem extends Item {
    public SilencerStickItem(Properties properties) {
        super(properties);
    }

    private @NotNull InteractionResult silenceEntity(
            @NotNull ItemStack stack,
            @NotNull LivingEntity entity,
            @NotNull Player user,
            @NotNull InteractionHand usedHand
    ) {
        Level level = user.level();

        if (!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            entity.setSilent(true);
            entity.setCustomName(Component.literal("silenced"));

            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 3 * 20, 0, false, false));

            Vec3 pos = entity.position();
            serverLevel.playSound(
                    null,
                    pos.x, pos.y, pos.z,
                    SoundEvents.AMETHYST_BLOCK_RESONATE,
                    SoundSource.AMBIENT,
                    0.8f,
                    2.0f
            );

            if (entity instanceof Mob mob) {
                mob.setPersistenceRequired();
            }

            stack.hurtAndBreak(1, user, LivingEntity.getSlotForHand(usedHand));
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        return this.silenceEntity(stack, interactionTarget, player, usedHand);
    }
}
