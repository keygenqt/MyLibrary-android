/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary.ui.settings

import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.*
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.ui.settings.utils.DotIndicatorPagerAdapter
import com.keygenqt.mylibrary.ui.settings.utils.ZoomOutPageTransformer
import com.keygenqt.mylibrary.utils.AVATARS
import kotlinx.android.synthetic.main.fragment_edit_profile.view.dotsIndicator
import kotlinx.android.synthetic.main.fragment_edit_profile.view.viewPager

@ActionBarEnable
@FragmentTitle("Edit Profile")
class FragmentEditProfile : BaseFragment(R.layout.fragment_edit_profile) {

    private var keyAvatar = "avatar_6"

    override fun onCreateView() {
        initToolbar {
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
        initView {
            dotsIndicator.setViewPager(viewPager.apply {
                adapter = DotIndicatorPagerAdapter(AVATARS)
                currentItem = keyAvatar.replace("avatar_", "").toInt()
                setPageTransformer(true, ZoomOutPageTransformer())
                addOnPageChangeListener(object : OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        keyAvatar = "avatar_${position + 1}"
                    }
                })
            })
        }
    }
}