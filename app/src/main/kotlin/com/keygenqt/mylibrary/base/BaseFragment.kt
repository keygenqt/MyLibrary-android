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
import android.icu.lang.UCharacter.GraphemeClusterBreak.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.ActionBarSearchEnable
import com.keygenqt.mylibrary.annotations.BottomNavigationEnable
import com.keygenqt.mylibrary.annotations.SpawnAnimation
import com.keygenqt.mylibrary.extensions.hideKeyboard
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.concurrent.schedule
import kotlin.reflect.full.findAnnotation

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    annotation class OnCreateAfter
    annotation class UpStack

    private val preferences: BaseSharedPreferences by inject()

    private var _bind: ViewBinding? = null

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

    infix fun BaseFragment<T>.bind(delegate: T.() -> Unit) =
        activity?.let {
            _bind?.let {
                delegate.invoke(it as T)
            }
        }

    infix fun BaseFragment<T>.toolbar(delegate: Toolbar.() -> Unit) =
        activity?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                delegate.invoke(it.findViewById(R.id.toolbar))
            }
        }

    open fun isSpawnAnimation(): Boolean {
        return this::class.java.kotlin.findAnnotation<SpawnAnimation>() != null
    }

    abstract fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): T

    abstract fun onCreateView()

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        activity?.let { activity ->
            activity.findViewById<View>(R.id.spawnAnimation)?.let { spawnAnimation ->
                if (isSpawnAnimation()) {
                    spawnAnimation.apply {
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
                    spawnAnimation.visibility = View.GONE
                }
            }

            this::class.java.kotlin.findAnnotation<ActionBarEnable>()?.let {
                (activity as AppCompatActivity).supportActionBar?.show()
            } ?: run {
                (activity as AppCompatActivity).supportActionBar?.hide()
            }
            this::class.java.kotlin.findAnnotation<BottomNavigationEnable>()?.let {
                activity.findViewById<View>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
            } ?: run {
                activity.findViewById<View>(R.id.bottomNavigationView)?.visibility = View.GONE
            }
            this::class.java.kotlin.findAnnotation<ActionBarSearchEnable>()?.let {
                activity.findViewById<View>(R.id.floatingSearchView)?.visibility = View.VISIBLE
            } ?: run {
                activity.findViewById<View>(R.id.floatingSearchView)?.visibility = View.INVISIBLE
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }

            activity.currentFocus?.hideKeyboard()

            _bind = onCreateBind(inflater, container)

            statusProgressPage(false)
            setHasOptionsMenu(true)
            statusProgress(false)
            onCreateView()
            callOnCreate()
        }

        return _bind?.root
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
            it.findViewById<View>(R.id.progressBar)?.visibility = if (status) View.VISIBLE else View.GONE
        }
    }

    fun statusProgressPage(status: Boolean) {
        activity?.let {
            it.findViewById<View>(R.id.loading)?.visibility = if (status) View.VISIBLE else View.GONE
            it.findViewById<View>(R.id.ivMoon)?.let { ivMoon ->
                ivMoon.visibility = if (status && (preferences.grayTheme || preferences.darkTheme)) View.VISIBLE else View.GONE
            }
            it.findViewById<LottieAnimationView>(R.id.lottieAnimationView)?.let { lottieAnimationView ->
                if (status) {
                    lottieAnimationView.playAnimation()
                } else {
                    lottieAnimationView.resumeAnimation()
                }
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