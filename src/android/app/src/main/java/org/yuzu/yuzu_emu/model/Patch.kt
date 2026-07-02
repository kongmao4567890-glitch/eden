// SPDX-FileCopyrightText: Copyright 2026 Eden Emulator Project
// SPDX-License-Identifier: GPL-3.0-or-later

// SPDX-FileCopyrightText: 2023 yuzu Emulator Project
// SPDX-License-Identifier: GPL-2.0-or-later

package org.yuzu.yuzu_emu.model

import androidx.annotation.Keep

@Keep
data class Patch(
    var enabled: Boolean,
    val name: String,
    val version: String,
    val type: Int,
    val programId: String,
    val titleId: String,
    val numericVersion: Long = 0,
    val source: Int = 0,
    val parentName: String = "",  // For cheats: name of the mod folder containing them
    val cheatCompat: Int = CHEAT_COMPAT_COMPATIBLE
) {
    companion object {
        const val SOURCE_UNKNOWN = 0
        const val SOURCE_NAND = 1
        const val SOURCE_SDMC = 2
        const val SOURCE_EXTERNAL = 3
        const val SOURCE_PACKED = 4

        const val CHEAT_COMPAT_COMPATIBLE = 0
        const val CHEAT_COMPAT_INCOMPATIBLE = 1
    }

    val isRemovable: Boolean
        get() = source != SOURCE_EXTERNAL && source != SOURCE_PACKED

    /**
     * Returns the storage key used for saving enabled/disabled state.
     * For cheats with a parent, returns "ParentName::CheatName".
     */
    fun getStorageKey(): String {
        return if (parentName.isNotEmpty()) {
            "$parentName::$name"
        } else {
            name
        }
    }

    /**
     * Returns true if this patch is an individual cheat entry (not a cheat mod).
     * Individual cheats have type=Cheat and a parent mod name.
     */
    fun isCheat(): Boolean = type == PatchType.Cheat.int && parentName.isNotEmpty()

    fun isIncompatibleCheat(): Boolean = isCheat() && cheatCompat == CHEAT_COMPAT_INCOMPATIBLE
}
