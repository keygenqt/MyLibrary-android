package com.keygenqt.mylibrary.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.ui.books.FragmentBooks
import com.keygenqt.mylibrary.ui.chat.FragmentChat
import com.keygenqt.mylibrary.ui.other.FragmentLogin
import org.koin.android.ext.android.inject

abstract class BaseActivity(@LayoutRes val contentId: Int, @NavigationRes val graphId: Int) : AppCompatActivity() {

    private val sharedPreferences: BaseSharedPreferences by inject()

    lateinit var controller: NavController

    protected lateinit var info: Snackbar

    abstract fun onCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    fun getCurrentFragment(): BaseFragment? {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            return it.childFragmentManager.fragments[0] as? BaseFragment
        }
        return null
    }
}