/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent

import net.neoforged.neoforge.server.permission.nodes.PermissionNode
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes

object PermissionNodes {
    private fun requiresLevel(level: Int) = PermissionNode.PermissionResolver { player, _, _ ->
        level <= 0 || player != null && player.hasPermissions(level)
    }

    private fun createNode(nodeName: String, requiredLevel: Int = 2): PermissionNode<Boolean> {
        return PermissionNode(
            SuperDuperContent.MOD_ID,
            nodeName,
            PermissionTypes.BOOLEAN,
            requiresLevel(requiredLevel)
        )
    }

    val SUICIDE_COMMAND = createNode("command.suicide", requiredLevel = 2)
}