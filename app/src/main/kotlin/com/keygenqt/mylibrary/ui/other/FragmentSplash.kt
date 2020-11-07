package com.keygenqt.mylibrary.ui.other

import android.os.*
import androidx.navigation.fragment.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.*

class FragmentSplash : BaseFragment(R.layout.fragment_splash) {

    override fun isActionBar(): Boolean {
        return false
    }

    override fun onCreateView() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(FragmentSplashDirections.actionFragmentSplashToFragmentLocal())
        }, 1000)
    }
}