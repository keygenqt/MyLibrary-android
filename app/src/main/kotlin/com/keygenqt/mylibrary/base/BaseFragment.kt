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

import android.graphics.Color.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.LinearInterpolator
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.ActionBarSearchEnable
import com.keygenqt.mylibrary.annotations.BottomNavigationEnable
import com.keygenqt.mylibrary.annotations.SpawnAnimation
import com.keygenqt.mylibrary.extensions.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.reflect.full.findAnnotation

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment() {

    annotation class OnCreateAfter

    private val preferences: BaseSharedPreferences by inject()

    private var _view: View? = null

    var timer = Timer()

    fun searchDelay(delegate: () -> Unit) {
        timer.cancel()
        timer = Timer()
        timer.schedule(200) {
            Handler(Looper.getMainLooper()).post {
                delegate.invoke()
            }
            timer.cancel()
        }
    }

    infix fun BaseFragment.initToolbar(delegate: Toolbar.() -> Unit) =
        activity?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                delegate.invoke(it.findViewById(R.id.toolbar))
            }
        }

    infix fun BaseFragment.initView(delegate: View.() -> Unit) =
        activity?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                view?.let {
                    _view = null
                    delegate.invoke(it)
                } ?: run {
                    delegate.invoke(_view!!)
                }
            }
        }

    open fun isSpawnAnimation(): Boolean {
        return this::class.java.kotlin.findAnnotation<SpawnAnimation>() != null
    }

    abstract fun onCreateView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let { activity ->
            if (isSpawnAnimation()) {
                (activity as AppCompatActivity).spawnAnimation.apply {
                    visibility = View.VISIBLE
                    alpha = 1f
                    animate().apply {
                        interpolator = LinearInterpolator()
                        duration = 100
                        alpha(0f)
                        startDelay = 150
                        start()
                    }
                }
            } else {
                (activity as AppCompatActivity).spawnAnimation.visibility = View.GONE
            }
            this::class.java.kotlin.findAnnotation<ActionBarEnable>()?.let {
                activity.supportActionBar?.show()
            } ?: run {
                activity.supportActionBar?.hide()
            }
            this::class.java.kotlin.findAnnotation<BottomNavigationEnable>()?.let {
                activity.findViewById<View>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
            } ?: run {
                activity.findViewById<View>(R.id.bottomNavigationView)?.visibility = View.GONE
            }
            this::class.java.kotlin.findAnnotation<ActionBarSearchEnable>()?.let {
                activity.floatingSearchView.visibility = View.VISIBLE
            } ?: run {
                activity.floatingSearchView.visibility = View.INVISIBLE
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }

            activity.currentFocus?.hideKeyboard()

            _view = inflater.inflate(layoutId, container, false)

            statusProgressPage(false)
            setHasOptionsMenu(true)
            statusProgress(false)
            onCreateView()
            callOnCreate()
        }

        return _view
    }

    private fun callOnCreate() {
        this::class.java.declaredMethods.forEach { method ->
            method.getAnnotation(OnCreateAfter::class.java) ?: return@forEach
            method.isAccessible = true
            method.invoke(this)
        }
    }

    fun statusProgress(status: Boolean) {
        activity?.let {
            it.progressBar.visibility = if (status) View.VISIBLE else View.GONE
        }
    }

    fun statusProgressPage(status: Boolean) {
        activity?.let {
            it.loading.visibility = if (status) View.VISIBLE else View.GONE
            it.ivMoon.visibility = if (status && (preferences.grayTheme || preferences.darkTheme)) View.VISIBLE else View.GONE
            if (status) {
                it.lottieAnimationView.playAnimation()
            } else {
                it.lottieAnimationView.resumeAnimation()
            }
        }

    }

    open fun onCreateOptionsMenu(): Int? {
        return null
    }

    open fun onOptionsItemSelected(id: Int) {}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        onCreateOptionsMenu()?.let { id ->
            inflater.inflate(id, menu)
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onOptionsItemSelected(item.itemId)
        return true
    }
}