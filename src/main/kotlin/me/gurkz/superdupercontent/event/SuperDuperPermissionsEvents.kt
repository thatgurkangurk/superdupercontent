/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.event

import me.gurkz.superdupercontent.PermissionNodes
import me.gurkz.superdupercontent.SuperDuperContent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent

object SuperDuperPermissionsEvents {
    @SubscribeEvent
    fun registerPermissions(event: PermissionGatherEvent.Nodes) {
        SuperDuperContent.LOGGER.info("registering permission nodes")
        event.addNodes(PermissionNodes.SUICIDE_COMMAND)
    }
}