/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent

import me.gurkz.superdupercontent.SuperDuperContent.MOD_ID
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

@Mod(value = MOD_ID, dist = [Dist.CLIENT])
@EventBusSubscriber(modid = MOD_ID, value = [Dist.CLIENT])
object SuperDuperContentClient {
    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        SuperDuperContent.LOGGER.info("hello from superdupercontent client")
    }
}