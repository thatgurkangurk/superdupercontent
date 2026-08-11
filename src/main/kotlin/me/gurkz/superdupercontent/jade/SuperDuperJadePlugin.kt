/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.jade

import me.gurkz.superdupercontent.jade.provider.PetCooldownServerProvider
import snownee.jade.api.IWailaPlugin
import net.minecraft.world.entity.TamableAnimal
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.WailaPlugin

@WailaPlugin
class SuperDuperJadePlugin : IWailaPlugin {
    override fun register(registration: IWailaCommonRegistration) {
        registration.registerEntityDataProvider(PetCooldownServerProvider, TamableAnimal::class.java)
    }
}