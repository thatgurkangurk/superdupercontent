/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.data

import com.mojang.serialization.Codec
import java.util.function.Supplier
import me.gurkz.superdupercontent.SuperDuperContent
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object DataAttachments {
    val attachmentTypes: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SuperDuperContent.MOD_ID)

    val NEXT_PET_TIME: Supplier<AttachmentType<Long>> =
        attachmentTypes.register("next_pet_time") { _ ->
            AttachmentType.builder(Supplier { 0L })
                .serialize(Codec.LONG)
                .sync(ByteBufCodecs.VAR_LONG)
                .build()
        }

    val NO_FIREWORK_DAMAGE: Supplier<AttachmentType<Boolean>> =
        attachmentTypes.register("no_firework_damage") { _ ->
            AttachmentType.builder(Supplier { false }).serialize(Codec.BOOL).build()
        }

    val LAST_DEATH: Supplier<AttachmentType<DeathData>> =
        attachmentTypes.register("last_death") { ->
            AttachmentType.builder { -> DeathData(BlockPos.ZERO, Level.OVERWORLD) }
                .serialize(DeathData.CODEC)
                .copyOnDeath()
                .build()
        }

    fun register(eventBus: IEventBus) {
        attachmentTypes.register(eventBus)
    }
}
