package com.keygenqt.mylibrary.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.FragmentTitle
import com.keygenqt.mylibrary.ui.chat.FragmentChat
import com.keygenqt.mylibrary.ui.local.FragmentLocal
import com.keygenqt.mylibrary.ui.online.FragmentOnline
import com.keygenqt.mylibrary.ui.other.FragmentLogin
import kotlin.reflect.full.findAnnotation

abstract class BaseActivity(@LayoutRes val contentId: Int, @NavigationRes val graphId: Int): AppCompatActivity() {

    lateinit var controller: NavController

    private lateinit var info: Snackbar

    abstract fun onCreate()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        controller.removeOnDestinationChangedListener(listener)
        super.onPause()
    }
}