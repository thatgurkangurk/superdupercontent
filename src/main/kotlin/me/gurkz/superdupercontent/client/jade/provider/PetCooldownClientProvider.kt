/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.client.jade.provider

import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.jade.provider.PetCooldownServerProvider
import net.kyori.adventure.platform.modcommon.MinecraftAudiences
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.TamableAnimal
import snownee.jade.api.EntityAccessor
import snownee.jade.api.IEntityComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object PetCooldownClientProvider : IEntityComponentProvider {
    override fun getUid(): ResourceLocation = SuperDuperContent.id("pet_cooldown")

    private val audiences = MinecraftClientAudiences.of()

    private fun mm(input: String): Component = audiences.asNative(SuperDuperContent.mm.deserialize(input))

    override fun appendTooltip(tooltip: ITooltip, accessor: EntityAccessor, config: IPluginConfig) {
        val entity = accessor.entity as? TamableAnimal ?: return

        if (!entity.isTame) return

        val data = PetCooldownServerProvider.decodeFromData(accessor)
        if (data.isPresent) {
            val currentTime = System.currentTimeMillis()

            if (currentTime < data.get()) {
                val secondsLeft = (data.get() - currentTime) / 1000

                tooltip.add(mm("<gray>Next pet in </gray><white>${secondsLeft}s</white>"))
            } else {
                tooltip.add(mm("<red>Ready to pet! \u2764</red>"))
            }
        } else {
            tooltip.add(mm("<red>Ready to pet! \u2764</red>"))
        }
    }
}