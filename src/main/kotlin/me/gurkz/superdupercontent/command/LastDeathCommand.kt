/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.gurkz.superdupercontent.PermissionNodes
import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.data.DataAttachments
import me.gurkz.superdupercontent.util.Adventure
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.server.permission.PermissionAPI

internal val lastDeathCommand: LiteralArgumentBuilder<CommandSourceStack> =
    Commands.literal("lastdeath")
        .requires { source ->
            val player = source.player ?: return@requires true

            PermissionAPI.getPermission(player, PermissionNodes.LAST_DEATH_COMMAND)
        }
        .executes { context ->
            run {
                val audience = Adventure.audience(context.source)

                val player = context.source.player

                if (player == null) {
                    audience.sendFailure(
                        SuperDuperContent.mm.deserialize("only players can run /lastdeath")
                    )
                    return@executes 1
                }

                val deathData = player.getData(DataAttachments.LAST_DEATH)

                if (deathData == null) {
                    audience.sendFailure(
                        SuperDuperContent.mm.deserialize("you don't have a death location")
                    )
                    return@executes 1
                }

                val targetLevel: ServerLevel? = player.server.getLevel(deathData.dimension)

                if (targetLevel == null) {
                    audience.sendFailure(
                        SuperDuperContent.mm.deserialize(
                            "dimension <dimension> no longer exists!",
                            Placeholder.unparsed(
                                "dimension",
                                deathData.dimension.location().toString(),
                            ),
                        )
                    )
                    return@executes 0
                }

                val pos = deathData.pos

                player.teleportTo(
                    targetLevel,
                    pos.x + 0.5,
                    pos.y.toDouble(),
                    pos.z + 0.5,
                    deathData.yRot,
                    deathData.xRot,
                )

                audience.sendSuccess(
                    SuperDuperContent.mm.deserialize("<green>woosh</green>"),
                    false,
                )

                return@executes 0
            }
        }
