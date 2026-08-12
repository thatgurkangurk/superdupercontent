/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

data class DeathData(
    val pos: BlockPos,
    val dimension: ResourceKey<Level>,
    val yRot: Float = 0f,
    val xRot: Float = 0f,
) {
    companion object {
        val CODEC: Codec<DeathData> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(DeathData::pos),
                    ResourceKey.codec(Registries.DIMENSION)
                        .fieldOf("dimension")
                        .forGetter(DeathData::dimension),
                    Codec.FLOAT.optionalFieldOf("yRot", 0f).forGetter(DeathData::yRot),
                    Codec.FLOAT.optionalFieldOf("xRot", 0f).forGetter(DeathData::xRot),
                )
                .apply(instance, ::DeathData)
        }
    }
}
