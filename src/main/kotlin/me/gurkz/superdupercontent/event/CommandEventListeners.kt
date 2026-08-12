/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.event

import me.gurkz.superdupercontent.SuperDuperContent.MOD_ID
import me.gurkz.superdupercontent.command.lastDeathCommand
import me.gurkz.superdupercontent.command.sendToastCommand
import me.gurkz.superdupercontent.command.suicideCommand
import me.gurkz.superdupercontent.command.superDuperContentCommand
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

@EventBusSubscriber(modid = MOD_ID)
object CommandEventListeners {
    @SubscribeEvent
    fun onCommandRegistration(event: RegisterCommandsEvent) {
        event.dispatcher.register(lastDeathCommand)
        event.dispatcher.register(superDuperContentCommand)
        event.dispatcher.register(suicideCommand)
        event.dispatcher.register(sendToastCommand)
    }
}
