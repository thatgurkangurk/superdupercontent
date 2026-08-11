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
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.TamableAnimal
import snownee.jade.api.EntityAccessor
import snownee.jade.api.IEntityComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import kotlin.jvm.optionals.getOrNull

object PetCooldownClientProvider : IEntityComponentProvider {
    override fun getUid(): ResourceLocation = SuperDuperContent.id("pet_cooldown")

    private val audiences = MinecraftClientAudiences.of()

    private fun mm(input: String, vararg resolvers: TagResolver): Component = audiences.asNative(SuperDuperContent.mm.deserialize(input, *resolvers))

    override fun appendTooltip(tooltip: ITooltip, accessor: EntityAccessor, config: IPluginConfig) {
        val entity = accessor.entity as? TamableAnimal ?: return
        if (!entity.isTame) return

        val nextPetTick = PetCooldownServerProvider.decodeFromData(accessor).getOrNull() ?: 0L
        val currentTick: Long = accessor.level.gameTime

        if (currentTick < nextPetTick) {
            val secondsLeft: Long = (nextPetTick - currentTick + 19L) / 20L

            tooltip.add(
                mm(
                    "<gray>Next pet in </gray><white><seconds>s</white>",
                    Placeholder.unparsed("seconds", secondsLeft.toString())
                )
            )
        } else {
            tooltip.add(mm("<red>Ready to pet! ❤</red>"))
        }
    }
}