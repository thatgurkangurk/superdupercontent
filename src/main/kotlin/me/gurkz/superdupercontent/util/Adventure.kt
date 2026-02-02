/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.util

import net.kyori.adventure.platform.modcommon.AdventureCommandSourceStack
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences
import net.minecraft.commands.CommandSourceStack
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent

object Adventure {
    @Volatile
    private var adventure: MinecraftServerAudiences? = null

    fun adventure(): MinecraftServerAudiences {
        checkNotNull(this.adventure) { "Tried to access Adventure without a running server!" }
        return this.adventure!!
    }

    fun audience(stack: CommandSourceStack): AdventureCommandSourceStack {
        return this.adventure().audience(stack)
    }

    fun register(event: ServerStartingEvent) {
        this.adventure = MinecraftServerAudiences.of(event.server)
    }

    fun deregister(event: ServerStoppedEvent) {
        this.adventure = null
    }
}