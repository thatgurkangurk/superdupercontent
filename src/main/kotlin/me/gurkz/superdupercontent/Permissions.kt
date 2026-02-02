/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent

import me.gurkz.superdupercontent.java.lamp.actor.NeoForgeCommandActor
import me.gurkz.superdupercontent.java.lamp.annotation.CommandPermission
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.query.QueryOptions
import net.luckperms.api.util.Tristate
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.neoforged.fml.ModList
import net.neoforged.neoforge.common.util.TriState
import java.util.concurrent.TimeUnit
import java.util.function.BooleanSupplier
import kotlin.properties.Delegates

fun Tristate.toNeoTriState(): TriState = when (this) {
    Tristate.TRUE -> TriState.TRUE
    Tristate.FALSE -> TriState.FALSE
    Tristate.UNDEFINED -> TriState.DEFAULT
}

fun TriState.orElseGet(supplier: BooleanSupplier): Boolean =
    when (this) {
        TriState.TRUE -> true
        TriState.FALSE -> false
        TriState.DEFAULT -> supplier.asBoolean
    }

object Permissions {
    private var luckPermsLoaded by Delegates.notNull<Boolean>()
    private var luckPerms: LuckPerms? = null

    fun getPermissionValue(source: SharedSuggestionProvider, permission: String): TriState {
        val lp = luckPerms
        if (source is CommandSourceStack) {
            if (luckPermsLoaded && lp != null) {
                val player = source.player!!
                if (source.player == null) return TriState.DEFAULT
                var user = lp.userManager.getUser(player.uuid)

                if (user == null) {
                    try {
                        val userFuture = lp.userManager.loadUser(player.uuid)
                        user = userFuture.get(5, TimeUnit.SECONDS)
                    } catch (e: Exception) {
                        SuperDuperContent.LOGGER.debug("couldn't load user {} from luckperms: {}", player.uuid, e.message)
                        return TriState.DEFAULT
                    }
                }

                if (user == null) {
                    SuperDuperContent.LOGGER.debug("user {} was not found in luckperms", player.uuid)
                    return TriState.DEFAULT
                }

                val queryOptions = QueryOptions.defaultContextualOptions()
                val result = user.cachedData.getPermissionData(queryOptions).checkPermission(permission)

                return result.toNeoTriState()
            }
        }
        return TriState.DEFAULT
    }

    @JvmStatic
    fun check(actor: NeoForgeCommandActor, permissionAnn: CommandPermission): Boolean {
        val source = actor.source()

        if (source.hasPermission(permissionAnn.vanilla)) return true

        val result = getPermissionValue(source, permissionAnn.value).orElseGet { source.hasPermission(permissionAnn.vanilla) }

        //SuperDuperContent.LOGGER.debug("GOT RESULT: {}, DO THEY HAVE THE PERMISSION LEVEL?: {}. THE PERMISSION LEVEL IS {}", result, source.hasPermission(permissionAnn.vanilla), permissionAnn.vanilla)

        return result
    }

    fun register() {
        luckPermsLoaded = ModList.get().isLoaded("luckperms")
        if (luckPermsLoaded) {
            try {
                luckPerms = LuckPermsProvider.get()
            } catch (e: IllegalStateException) {
                SuperDuperContent.LOGGER.error("luckperms doesn't seem to be loaded...")
                luckPerms = null
            }
        }

        // PermissionCheckEvent.EVENT.register(::onPermissionCheck)
    }
}