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

package com.keygenqt.mylibrary.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.keygenqt.mylibrary.R
import org.koin.android.ext.android.inject

abstract class BaseActivity(@LayoutRes val contentId: Int, @NavigationRes val graphId: Int) : AppCompatActivity() {

    private val sharedPreferences: BaseSharedPreferences by inject()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    lateinit var controller: NavController

    protected lateinit var info: Snackbar

    abstract fun onCreate()

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        // analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.ITEM_NAME, when (val title = destination.label.toString()) {
                "{title}" -> "Book View"
                else -> title
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = Firebase.analytics

        when {
            sharedPreferences.darkTheme -> {
                setTheme(R.style.DarkTheme)
            }
            sharedPreferences.grayTheme -> {
                setTheme(R.style.DarkThemeGray)
            }
            else -> {
                setTheme(R.style.AppTheme)
            }
        }

        setContentView(contentId)
        setSupportActionBar(findViewById(R.id.toolbar))

        info = Snackbar.make(
            findViewById(R.id.contentMain),
            resources.getString(R.string.exit_info), Snackbar.LENGTH_SHORT
        )

        controller = findNavController(R.id.nav_host_fragment)
        controller.setGraph(graphId)

        setupActionBarWithNavController(controller)

        onCreate()
    }

    override fun onSupportNavigateUp(): Boolean {
        findNavController(R.id.nav_host_fragment).navigateUp()
        return super.onSupportNavigateUp()
    }

    fun getCurrentFragment(): BaseFragment<*>? {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            return it.childFragmentManager.fragments[0] as? BaseFragment<*>
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        controller.removeOnDestinationChangedListener(listener)
        super.onPause()
    }
}