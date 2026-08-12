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
import me.gurkz.superdupercontent.util.Adventure
import me.gurkz.superdupercontent.util.FireworkUtil
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.level.GameType
import net.neoforged.neoforge.server.permission.PermissionAPI

private val suicideDamageType =
    ResourceKey.create(
        Registries.DAMAGE_TYPE,
        ResourceLocation.fromNamespaceAndPath(
            SuperDuperContent.MOD_ID,
            "suicide",
        ),
    )

internal val suicideCommand: LiteralArgumentBuilder<CommandSourceStack> =
    Commands.literal("suicide")
        .requires { source ->
            val player = source.player ?: return@requires true

            PermissionAPI.getPermission(player, PermissionNodes.SUICIDE_COMMAND)
        }
        .executes { context ->
            run {
                val audience = Adventure.audience(context.source)

                val player = context.source.player

                if (player == null) {
                    audience.sendFailure(
                        SuperDuperContent.mm.deserialize("only players can run /suicide")
                    )
                    return@executes 1
                }

                if (player.gameMode.gameModeForPlayer === GameType.CREATIVE) {
                    audience.sendFailure(
                        SuperDuperContent.mm.deserialize(
                            "only players can in survival run /suicide"
                        )
                    )
                    return@executes 1
                }

                val damageSource =
                    DamageSource(
                        player
                            .level()
                            .registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(suicideDamageType)
                    )

                val pos = player.position()

                FireworkUtil.summonFirework(
                    pos,
                    context.source.level,
                    FireworkUtil.createColour(0, 255, 0),
                    FireworkUtil.createColour(255, 0, 0),
                    2,
                    false,
                )

                player.hurt(damageSource, 20.0f)

                audience.sendSuccess(
                    SuperDuperContent.mm.deserialize("<green>committed suicide</green>"),
                    false,
                )

                return@executes 0
            }
        }
