/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.event

import me.gurkz.superdupercontent.SuperDuperContent.MOD_ID
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.BlockEvent

@EventBusSubscriber(modid = MOD_ID)
object FarmlandTrampleListeners {
    @SubscribeEvent
    fun onFarmlandTrample(event: BlockEvent.FarmlandTrampleEvent) {
        val entity = event.entity as? LivingEntity ?: return

        val featherFallingHolder =
            entity
                .level()
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FEATHER_FALLING)

        val featherFallingLevel =
            EnchantmentHelper.getEnchantmentLevel(featherFallingHolder, entity)

        if (featherFallingLevel > 0 || entity.hasEffect(MobEffects.SLOW_FALLING)) {
            event.isCanceled = true
        }
    }
}
