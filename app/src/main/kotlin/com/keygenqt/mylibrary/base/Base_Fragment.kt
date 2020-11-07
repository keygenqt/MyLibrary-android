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

import android.os.*
import android.view.*
import androidx.annotation.*
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import com.keygenqt.mylibrary.*
import com.keygenqt.mylibrary.R
import kotlinx.coroutines.*

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
     * Method for show/hide ActionBar. If you need hide ActionBar
     * override this method and return false
     *
     * @return <code>true</code>
     */
    open fun isActionBar(): Boolean {
        return true
    }

    /**
     * Method for show/hide BottomNavigation. If you need show BottomNavigation
     * override this method and return true
     *
     * @return <code>false</code>
     */
    open fun isBottomNavigation(): Boolean {
        return false
    }

    /**
     * Base inner method for generate view in initialization view
     *
     * @return View?
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (isActionBar()) {
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        }
        if (isBottomNavigation()) {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.nav_view).visibility = View.VISIBLE
        } else {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.nav_view).visibility = View.GONE
        }
        _view = inflater.inflate(layoutId, container, false)
        setHasOptionsMenu(true)
        onCreateView()
        return _view
    }
}