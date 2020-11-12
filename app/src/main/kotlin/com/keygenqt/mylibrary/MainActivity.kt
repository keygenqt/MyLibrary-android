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

package com.keygenqt.mylibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.ui.chat.FragmentChat
import com.keygenqt.mylibrary.ui.local.FragmentLocal
import com.keygenqt.mylibrary.ui.online.FragmentOnline
import com.keygenqt.mylibrary.ui.other.FragmentLogin
import kotlin.reflect.full.findAnnotation

/**
 * MainActivity - base activity for navigation fragments
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
class MainActivity : AppCompatActivity() {

    /**
     * Navigation fragments
     */
    private lateinit var controller: NavController

    /**
     * Notification show for double click exit
     */
    private lateinit var info: Snackbar

    /**
     * Implementation of work with FragmentTitle
     * @see FragmentTitle
     */
    private val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
        // set title fragment without delay
        findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            val destinationClassName =
                (controller.currentDestination as FragmentNavigator.Destination).className
            Class.forName(destinationClassName).kotlin.findAnnotation<FragmentTitle>()
                ?.let { annotation ->
                    toolbar.title = annotation.title
                } ?: run {
                toolbar.title = ""
            }
        }
    }

    /**
     * Base method, like main in kotlin native
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        info = Snackbar.make(
            findViewById(R.id.contentMain),
            resources.getString(R.string.exit_info), Snackbar.LENGTH_SHORT
        )

        controller = findNavController(R.id.nav_host_fragment)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.FragmentLocal,
                R.id.FragmentOnline,
                R.id.FragmentChat
            )
        )
        setupActionBarWithNavController(controller, appBarConfiguration)
        navView.setupWithNavController(controller)
    }

    /**
     * Check back press
     */
    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            when (it.childFragmentManager.fragments[0]) {
                is FragmentLocal -> if (info.isShown) finishAffinity() else info.show()
                is FragmentOnline -> if (info.isShown) finishAffinity() else info.show()
                is FragmentChat -> if (info.isShown) finishAffinity() else info.show()
                is FragmentLogin -> if (info.isShown) finishAffinity() else info.show()
                else -> super.onBackPressed()
            }
        }
    }

    /**
     * Add for listener change fragment in navigation
     */
    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(listener)
    }

    /**
     * Remove for listener change fragment in navigation
     */
    override fun onPause() {
        controller.removeOnDestinationChangedListener(listener)
        super.onPause()
    }
}