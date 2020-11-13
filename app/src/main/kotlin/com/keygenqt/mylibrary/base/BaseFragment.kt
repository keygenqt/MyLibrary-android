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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.BottomNavigationEnable
import com.keygenqt.mylibrary.base.response.BaseResponseError
import com.keygenqt.mylibrary.base.response.HttpException
import com.keygenqt.mylibrary.ui.activities.GuestActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.net.ConnectException
import kotlin.reflect.full.findAnnotation

abstract class BaseFragment(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    private val sharedPreferences: BaseSharedPreferences by inject()

    annotation class ObserveInit

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
        this::class.java.kotlin.findAnnotation<ActionBarEnable>()?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
        } ?: run {
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        }
        this::class.java.kotlin.findAnnotation<BottomNavigationEnable>()?.let {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.nav_view)?.visibility =
                View.VISIBLE
        } ?: run {
            (requireActivity() as AppCompatActivity).findViewById<View>(R.id.nav_view)?.visibility =
                View.GONE
        }
        _view = inflater.inflate(layoutId, container, false)
        setHasOptionsMenu(true)
        onCreateView()
        observeInit()
        listenErrorResponse()
        return _view
    }

    private fun observeInit() {
        this::class.java.declaredMethods.forEach { method ->
            method.getAnnotation(ObserveInit::class.java) ?: return@forEach
            method.isAccessible = true
            method.invoke(this)
        }
    }

    private fun listenErrorResponse() {
        if (!BaseResponseError.error.hasActiveObservers()) {
            BaseResponseError.error.observe(viewLifecycleOwner, { throwable ->
                when (throwable) {
                    is HttpException -> {
                        if (throwable.status == 403) {
                            sharedPreferences.token = null
                            context?.startActivity(Intent(context, GuestActivity::class.java))
                        }
                    }
                    is ConnectException -> {
                        Toast.makeText(context, "Failed to connect API", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

    }
}