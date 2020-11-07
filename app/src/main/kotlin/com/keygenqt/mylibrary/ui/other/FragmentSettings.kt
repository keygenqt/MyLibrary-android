package com.keygenqt.mylibrary.ui.other

import androidx.navigation.fragment.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

@FragmentTitle("Settings")
class FragmentSettings : BaseFragment(R.layout.fragment_settings) {
    override fun onCreateView() {
        initToolbar {
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        initView {
            settingsBlock.setOnClickListener {
                settingsSwitch.isChecked = !settingsSwitch.isChecked
            }
            settingsBlock2.setOnClickListener {
                settingsSwitch2.isChecked = !settingsSwitch2.isChecked
            }
        }
    }
}