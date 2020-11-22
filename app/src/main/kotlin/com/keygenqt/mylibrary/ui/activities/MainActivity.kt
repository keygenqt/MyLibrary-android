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

package com.keygenqt.mylibrary.ui.activities

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.BaseActivity
import com.keygenqt.mylibrary.ui.books.FragmentBooks
import com.keygenqt.mylibrary.ui.chat.FragmentChat
import com.keygenqt.mylibrary.ui.other.FragmentLogin
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : BaseActivity(R.layout.activity_main, R.navigation.nav_graph_app) {
    override fun onCreate() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.FragmentBooks,
                R.id.FragmentLogin,
                R.id.FragmentChat
            )
        )
        toolbar.setupWithNavController(controller, appBarConfiguration)
        bottomNavigationView.setupWithNavController(controller)
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            when (it.childFragmentManager.fragments[0]) {
                is FragmentBooks -> if (info.isShown) finishAffinity() else info.show()
                is FragmentChat -> if (info.isShown) finishAffinity() else info.show()
                is FragmentLogin -> if (info.isShown) finishAffinity() else info.show()
                else -> super.onBackPressed()
            }
        }
    }
}