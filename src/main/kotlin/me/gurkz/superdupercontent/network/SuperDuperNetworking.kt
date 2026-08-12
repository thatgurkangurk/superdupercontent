/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.network

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.gurkz.superdupercontent.client.network.ClientPacketReceiver
import me.gurkz.superdupercontent.network.packet.SendToastPacket

object SuperDuperNetworking {
    fun initialise() {
        ConfigApi.network()
            .registerS2C(
                SendToastPacket.TYPE,
                SendToastPacket.STREAM_CODEC,
                ClientPacketReceiver::handleToastPacket,
            )
    }
}
