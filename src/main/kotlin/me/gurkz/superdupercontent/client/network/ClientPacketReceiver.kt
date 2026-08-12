/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.client.network

import me.fzzyhmstrs.fzzy_config.networking.api.ClientPlayNetworkContext
import me.gurkz.superdupercontent.network.packet.SendToastPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.toasts.SystemToast
import net.minecraft.network.chat.Component

object ClientPacketReceiver {
    fun handleToastPacket(payload: SendToastPacket, context: ClientPlayNetworkContext) {
        val header = payload.header
        val body = payload.body

        val titleComponent = Component.literal(header)
        val contentComponent = Component.literal(body)

        Minecraft.getInstance()
            .toasts
            .addToast(
                SystemToast.multiline(
                    Minecraft.getInstance(),
                    SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                    titleComponent,
                    contentComponent,
                )
            )
    }
}
