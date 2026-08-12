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
    private val _permissionNodes = mutableListOf<PermissionNode<*>>()
    val permissionNodes: List<PermissionNode<*>>
        get() = _permissionNodes

    private fun requiresLevel(level: Int) = PermissionNode.PermissionResolver { player, _, _ ->
        level <= 0 || player?.hasPermissions(level) == true
    }

    private fun createNode(nodeName: String, requiredLevel: Int = 2): PermissionNode<Boolean> {
        val node =
            PermissionNode(
                SuperDuperContent.MOD_ID,
                nodeName,
                PermissionTypes.BOOLEAN,
                requiresLevel(requiredLevel),
            )
        _permissionNodes.add(node)
        return node
    }

    val SUICIDE_COMMAND = createNode("command.suicide", requiredLevel = 0)
    val LAST_DEATH_COMMAND = createNode("command.lastdeath", requiredLevel = 1)
}
