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
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.keygenqt.mylibrary.base.response.HttpException
import com.keygenqt.mylibrary.ui.activities.GuestActivity
import com.keygenqt.mylibrary.ui.activities.MainActivity
import com.keygenqt.mylibrary.ui.other.FragmentSplash
import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.ConnectException

class BaseExceptionHandler(private val sharedPreferences: BaseSharedPreferences) : ActivityLifecycleCallbacks {

    companion object {

        private var error: MutableLiveData<Throwable> = MutableLiveData()

        fun getExceptionHandler(delegate: (Throwable) -> Unit): CoroutineExceptionHandler {
            return CoroutineExceptionHandler { _, throwable ->
                error.postValue(throwable)
                delegate.invoke(throwable)
            }
        }

        fun getExceptionHandler(error: MutableLiveData<Throwable>): CoroutineExceptionHandler {
            return getExceptionHandler {
                error.postValue(it)
            }
        }

        fun getExceptionHandler(): CoroutineExceptionHandler {
            return getExceptionHandler {}
        }
    }

    private fun init(activity: Activity) {
        error = MutableLiveData()
        error.observe(activity as LifecycleOwner, { throwable ->
            when (throwable) {
                is HttpException -> {
                    if (throwable.status == 403) {
                        sharedPreferences.token = null
                        error.removeObservers(activity as LifecycleOwner)
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(activity, GuestActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            activity.startActivity(intent)
                        }, 100)
                    }
                }
                is ConnectException -> {
                    if ((activity as BaseActivity).getCurrentFragment() !is FragmentSplash) {
                        error.removeObservers(activity as LifecycleOwner)
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            activity.startActivity(intent)
                        }, 100)
                    } else {
                        Toast.makeText(activity, "Failed to connect API", Toast.LENGTH_SHORT).show()
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