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

import android.app.Activity
import android.app.Application.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.keygenqt.mylibrary.R
import com.keygenqt.mylibrary.base.exceptions.HttpException
import com.keygenqt.mylibrary.ui.other.FragmentSplash
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.ConnectException

class BaseExceptionHandler(private val sharedPreferences: BaseSharedPreferences) : ActivityLifecycleCallbacks {

    companion object {

        private val error: MutableLiveData<LiveDataEvent<Throwable>> = MutableLiveData()

        fun getExceptionHandler(delegate: (LiveDataEvent<Throwable>) -> Unit): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { _, throwable ->
                error.postValue(LiveDataEvent(throwable))
                delegate.invoke(LiveDataEvent(throwable))
            }
        }

        fun getExceptionHandler(error: MutableLiveData<LiveDataEvent<Throwable>>): CoroutineExceptionHandler {
            return getExceptionHandler {
                error.postValue(it)
            }
        }

        fun getExceptionHandler(): CoroutineExceptionHandler {
            return getExceptionHandler {}
        }
    }

    private fun init(activity: Activity) {
        error.observe(activity as LifecycleOwner, { event ->
            event?.peekContentHandled()?.let { throwable ->
                when (throwable) {
                    is HttpException -> {
                        if (throwable.status == 405) {
                            Toast.makeText(activity, "Method Not Allowed", Toast.LENGTH_SHORT).show()
                        }
                        if (throwable.status == 403) {
                            sharedPreferences.token = null
                            error.removeObservers(activity as LifecycleOwner)
                            activity.findNavController(R.id.nav_host_fragment)
                                .createDeepLink().setDestination(R.id.FragmentLogin).createPendingIntent().send()
                        }
                        Log.e("HttpException", throwable.toString())
                    }
                    is ConnectException -> {
                        val fragment = (activity as BaseActivity).getCurrentFragment()
                        if (fragment !is FragmentSplash) {
                            error.removeObservers(activity as LifecycleOwner)
                            activity.findNavController(R.id.nav_host_fragment)
                                .createDeepLink().setDestination(R.id.FragmentSplash).createPendingIntent().send()
                        } else {
                            fragment.statusProgress(false)
                            Toast.makeText(activity, "Failed to connect API", Toast.LENGTH_SHORT).show()
                        }
                        Log.e("ConnectException", throwable.stackTraceToString())
                    }
                    else -> {
                        Log.e("BaseExceptionHandler", throwable.stackTraceToString())
                    }
                }
            }
        })
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        init(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}