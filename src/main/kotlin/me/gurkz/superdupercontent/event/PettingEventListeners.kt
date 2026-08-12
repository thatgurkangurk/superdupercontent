/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.event

import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.SuperDuperContent.MOD_ID
import me.gurkz.superdupercontent.data.DataAttachments.NEXT_PET_TIME
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.animal.Cat
import net.minecraft.world.entity.animal.Wolf
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = MOD_ID)
object PettingEventListeners {
    @SubscribeEvent
    fun onEntityInteract(event: PlayerInteractEvent.EntityInteract) {
        val player = event.entity
        val level = event.level
        val hand = event.hand
        val target = event.target

        if (hand != InteractionHand.MAIN_HAND || !player.isCrouching) {
            return
        }

        if (target is TamableAnimal && (target is Wolf || target is Cat) && target.isTame) {
            if (level.isClientSide) {
                event.cancellationResult = InteractionResult.SUCCESS
                event.isCanceled = true
                return
            }

            if (level is ServerLevel && handlePetPetting(target, level)) {
                event.cancellationResult = InteractionResult.SUCCESS
                event.isCanceled = true
            }
        }
    }

    private fun handlePetPetting(entity: TamableAnimal, level: ServerLevel): Boolean {
        val currentTick = level.gameTime
        val nextPetTick = entity.getData(NEXT_PET_TIME) ?: 0L

        if (currentTick < nextPetTick) {
            return false
        }

        // 1 second = 20 ticks
        val cooldownTicks = SuperDuperContent.config.pettingCooldownSeconds * 20L
        entity.setData(NEXT_PET_TIME, currentTick + cooldownTicks)

        level.sendParticles(
            ParticleTypes.HEART,
            entity.x,
            entity.y + 0.5,
            entity.z,
            3,
            0.3,
            0.3,
            0.3,
            0.0,
        )

        val soundEvent =
            when (entity) {
                is Wolf -> SoundEvents.WOLF_WHINE
                is Cat -> SoundEvents.CAT_PURREOW
                else -> null
            }

        soundEvent?.let { sound ->
            level.playSound(
                null,
                entity.blockPosition(),
                sound,
                SoundSource.NEUTRAL,
                1.0f,
                1.0f,
            )
        }

        return true
    }
}
