/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.event

import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.data.DataAttachments
import me.gurkz.superdupercontent.data.DeathData
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent

@EventBusSubscriber(modid = SuperDuperContent.MOD_ID)
object DeathEventListener {
    @SubscribeEvent
    fun onPlayerDeath(event: LivingDeathEvent) {
        val player = event.entity as? ServerPlayer ?: return

        val deathData = DeathData(
            pos = player.blockPosition(),
            dimension = player.level().dimension(),
            yRot = player.yRot,
            xRot = player.xRot
        )

        player.setData(DataAttachments.LAST_DEATH, deathData)
    }
}