/*
 * Copyright 2026 Gurkan
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package me.gurkz.superdupercontent.config

import me.fzzyhmstrs.fzzy_config.annotations.Comment
import me.fzzyhmstrs.fzzy_config.api.SaveType
import me.fzzyhmstrs.fzzy_config.config.Config
import me.gurkz.superdupercontent.SuperDuperContent

class SuperDuperConfig : Config(SuperDuperContent.id("super_duper_config")) {
    @Comment("how long you will have to wait before petting your pet again :(")
    var pettingCooldownSeconds: Int = 5

    override fun saveType(): SaveType {
        return SaveType.SEPARATE
    }
}