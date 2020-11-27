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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.BottomNavigationEnable
import com.keygenqt.mylibrary.annotations.SpawnAnimation
import com.keygenqt.mylibrary.extensions.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.progressBar
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.reflect.full.findAnnotation

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment() {

    annotation class CallOnCreate

    private val preferences: BaseSharedPreferences by inject()

    private var _view: View? = null

    infix fun BaseFragment.initToolbar(delegate: Toolbar.() -> Unit) =
        viewLifecycleOwner.lifecycleScope.launch {
            delegate.invoke(requireActivity().findViewById(R.id.toolbar))
        }

    infix fun BaseFragment.initView(delegate: View.() -> Unit) =
        viewLifecycleOwner.lifecycleScope.launch {
            view?.let {
                _view = null
                delegate.invoke(it)
            } ?: run {
                delegate.invoke(_view!!)
            }
        }

    abstract fun onCreateView()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this::class.java.kotlin.findAnnotation<SpawnAnimation>()?.let {
            (requireActivity() as AppCompatActivity).spawnAnimation.apply {
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
        } ?: run {
            (requireActivity() as AppCompatActivity).spawnAnimation.visibility = View.GONE
        }
        this::class.java.kotlin.findAnnotation<ActionBarEnable>()?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
        } ?: run {
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        }
        this::class.java.kotlin.findAnnotation<BottomNavigationEnable>()?.let {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.bottomNavigationView)?.visibility =
                View.VISIBLE
        } ?: run {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.bottomNavigationView)?.visibility =
                View.GONE
        }

        requireActivity().currentFocus?.hideKeyboard()

        _view = inflater.inflate(layoutId, container, false)

        statusProgressPage(false)
        setHasOptionsMenu(true)
        statusProgress(false)
        onCreateView()
        callOnCreate()
        return _view
    }

    private fun callOnCreate() {
        this::class.java.declaredMethods.forEach { method ->
            method.getAnnotation(CallOnCreate::class.java) ?: return@forEach
            method.isAccessible = true
            method.invoke(this)
        }
    }

    fun statusProgress(status: Boolean) {
        requireActivity().appBarLayout.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    fun statusProgressPage(status: Boolean) {
        requireActivity().loading.visibility = if (status) View.VISIBLE else View.GONE
        requireActivity().ivMoon.visibility = if (status && (preferences.grayTheme || preferences.darkTheme)) View.VISIBLE else View.GONE
        if (status) {
            requireActivity().lottieAnimationView.playAnimation()
        } else {
            requireActivity().lottieAnimationView.resumeAnimation()
        }
    }
}