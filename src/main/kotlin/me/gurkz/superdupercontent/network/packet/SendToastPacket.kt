/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.network.packet

import io.netty.buffer.ByteBuf
import me.gurkz.superdupercontent.SuperDuperContent
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class SendToastPacket(val header: String, val body: String) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<SendToastPacket> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<SendToastPacket> =
            CustomPacketPayload.Type(SuperDuperContent.id("send_toast"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SendToastPacket> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                SendToastPacket::header,
                ByteBufCodecs.STRING_UTF8,
                SendToastPacket::body,
                ::SendToastPacket,
            )
    }
}
