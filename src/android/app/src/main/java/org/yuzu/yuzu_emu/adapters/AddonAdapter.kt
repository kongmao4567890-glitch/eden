// SPDX-FileCopyrightText: Copyright 2026 Eden Emulator Project
// SPDX-License-Identifier: GPL-3.0-or-later

// SPDX-FileCopyrightText: 2023 yuzu Emulator Project
// SPDX-License-Identifier: GPL-2.0-or-later

package org.yuzu.yuzu_emu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import org.yuzu.yuzu_emu.databinding.ListItemAddonBinding
import org.yuzu.yuzu_emu.model.Patch
import org.yuzu.yuzu_emu.model.PatchType
import org.yuzu.yuzu_emu.model.AddonViewModel
import org.yuzu.yuzu_emu.viewholder.AbstractViewHolder

class AddonAdapter(val addonViewModel: AddonViewModel) :
    AbstractDiffAdapter<Patch, AddonAdapter.AddonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddonViewHolder {
        ListItemAddonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .also { return AddonViewHolder(it) }
    }

    inner class AddonViewHolder(val binding: ListItemAddonBinding) :
        AbstractViewHolder<Patch>(binding) {
        override fun bind(model: Patch) {
            val isCheat = model.isCheat()
            val isIncompatible = isCheat && model.cheatCompat == Patch.CHEAT_COMPAT_INCOMPATIBLE
            val indentPx = if (isCheat) {
                (32 * binding.root.context.resources.displayMetrics.density).toInt()
            } else {
                0
            }
            binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = indentPx
            }
            binding.addonCard.setOnClickListener(
                if (isIncompatible) null else { { binding.addonSwitch.performClick() } }
            )
            binding.title.text = model.name
            binding.version.text = model.version
            binding.addonSwitch.setOnCheckedChangeListener(null)
            binding.addonSwitch.isChecked = model.enabled
            binding.addonSwitch.isEnabled = !isIncompatible
            binding.addonSwitch.alpha = if (isIncompatible) 0.38f else 1.0f
            binding.addonSwitch.setOnCheckedChangeListener { _, checked ->
                if (PatchType.from(model.type) == PatchType.Update && checked) {
                    addonViewModel.enableOnlyThisUpdate(model)
                    notifyDataSetChanged()
                } else {
                    model.enabled = checked
                }
            }
            val canDelete = model.isRemovable && !isCheat
            binding.deleteCard.isVisible = canDelete
            binding.buttonDelete.isVisible = canDelete
            if (canDelete) {
                val deleteAction = {
                    addonViewModel.setAddonToDelete(model)
                }
                binding.deleteCard.setOnClickListener { deleteAction() }
                binding.buttonDelete.setOnClickListener { deleteAction() }
            } else {
                binding.deleteCard.setOnClickListener(null)
                binding.buttonDelete.setOnClickListener(null)
            }
        }
    }
}
