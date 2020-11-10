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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.BottomNavigationEnable
import com.keygenqt.mylibrary.hal.*
import kotlinx.coroutines.launch
import kotlin.reflect.full.findAnnotation

/**
 * BaseFragment is a class that makes it easier to work with
 * fragments by removing duplicate functionality from custom
 * fragments
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
abstract class BaseFragment(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    annotation class ObserveInit

    /**
     * Hidden field for can using initView in onCreateView
     */
    private var _view: View? = null

    /**
     * Infix method for initialization toolbar view
     */
    infix fun BaseFragment.initToolbar(delegate: Toolbar.() -> Unit) = viewLifecycleOwner.lifecycleScope.launch {
        delegate.invoke(requireActivity().findViewById(R.id.toolbar))
    }

    /**
     * Infix method for initialization view
     */
    infix fun BaseFragment.initView(delegate: View.() -> Unit) = viewLifecycleOwner.lifecycleScope.launch {
        view?.let {
            _view = null
            delegate.invoke(it)
        } ?: run {
            delegate.invoke(_view!!)
        }
    }

    /**
     * Method for override in custom fragment
     */
    abstract fun onCreateView()

    /**
     * Base inner method for generate view in initialization view
     *
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this::class.java.kotlin.findAnnotation<ActionBarEnable>()?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
        } ?: run {
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        }
        this::class.java.kotlin.findAnnotation<BottomNavigationEnable>()?.let {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.nav_view).visibility = View.VISIBLE
        } ?: run {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.nav_view).visibility = View.GONE
        }
        _view = inflater.inflate(layoutId, container, false)
        setHasOptionsMenu(true)
        onCreateView()
        observeInit()
        return _view
    }

    private fun observeInit() {
        this::class.java.declaredMethods.forEach { method ->
            method.getAnnotation(ObserveInit::class.java) ?: return@forEach
            method.isAccessible = true
            method.invoke(this)
        }
    }
}