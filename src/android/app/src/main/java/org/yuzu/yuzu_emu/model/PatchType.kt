// SPDX-FileCopyrightText: Copyright 2026 Eden Emulator Project
// SPDX-License-Identifier: GPL-3.0-or-later

// SPDX-FileCopyrightText: 2024 yuzu Emulator Project
// SPDX-License-Identifier: GPL-2.0-or-later

package org.yuzu.yuzu_emu.model

enum class PatchType(val int: Int) {
    Update(0),
    DLC(1),
    Mod(2),
    Cheat(3);

    companion object {
        fun from(int: Int): PatchType = entries.firstOrNull { it.int == int } ?: Update
    }
}
