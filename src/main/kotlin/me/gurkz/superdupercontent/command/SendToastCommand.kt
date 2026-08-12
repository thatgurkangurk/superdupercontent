/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import me.gurkz.superdupercontent.PermissionNodes
import me.gurkz.superdupercontent.SuperDuperContent
import me.gurkz.superdupercontent.network.packet.SendToastPacket
import me.gurkz.superdupercontent.util.Adventure
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.neoforged.neoforge.server.permission.PermissionAPI

internal val sendToastCommand: LiteralArgumentBuilder<CommandSourceStack> =
    Commands.literal("toast")
        .requires { source ->
            val player = source.player ?: return@requires true

            PermissionAPI.getPermission(player, PermissionNodes.SEND_TOAST_COMMAND)
        }
        .then(
            Commands.argument("targets", EntityArgument.players())
                .then(
                    Commands.argument("header", StringArgumentType.string())
                        .then(
                            Commands.argument("content", StringArgumentType.greedyString())
                                .executes { context ->
                                    val targets = EntityArgument.getPlayers(context, "targets")
                                    val header = StringArgumentType.getString(context, "header")
                                    val content = StringArgumentType.getString(context, "content")

                                    var sentCount = 0
                                    val packet = SendToastPacket(header, content)

                                    for (player in targets) {
                                        if (
                                            ConfigApi.network()
                                                .canSend(SendToastPacket.TYPE.id, player)
                                        ) {
                                            ConfigApi.network().send(packet, player)
                                            sentCount++
                                        }
                                    }

                                    val feedback =
                                        SuperDuperContent.mm.deserialize(
                                            "<green>sent toast to <white>$sentCount</white> player(s)!</green>"
                                        )
                                    Adventure.audience(context.source).sendMessage(feedback)

                                    sentCount
                                }
                        )
                )
        )
