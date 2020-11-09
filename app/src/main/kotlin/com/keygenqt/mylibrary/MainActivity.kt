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

import android.os.*
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import androidx.navigation.*
import androidx.navigation.fragment.*
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.*
import com.google.android.material.snackbar.*
import com.keygenqt.mylibrary.annotations.*
import com.keygenqt.mylibrary.ui.chat.*
import com.keygenqt.mylibrary.ui.local.*
import com.keygenqt.mylibrary.ui.online.*
import kotlin.reflect.full.*

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
        findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            val destinationClassName = (controller.currentDestination as FragmentNavigator.Destination).className
            Class.forName(destinationClassName).kotlin.findAnnotation<FragmentTitle>()?.let { annotation ->
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

        info = Snackbar.make(findViewById(R.id.contentMain),
            resources.getString(R.string.exit_info), Snackbar.LENGTH_SHORT)

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