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

package com.keygenqt.mylibrary.extensions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import androidx.core.view.forEach
import com.google.android.material.textfield.TextInputLayout
import kotlin.reflect.KClass

fun View.toggleKeyboard() {
    Handler(Looper.getMainLooper()).post {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm is InputMethodManager) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE)
    if (imm is InputMethodManager) {
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

fun View.requestFocusTextInputLayoutError(scrollView: ScrollView) {
    findView<TextInputLayout>().forEach { layout ->
        if (layout.isErrorEnabled) {
            layout.editText?.let { editText ->
                if (editText.isFocusable) {
                    editText.requestFocus()
                    Handler(Looper.getMainLooper()).postDelayed({
                        scrollView.smoothScrollTo(0, layout.top)
                    }, 10)
                    return
                }
            }
        }
    }
}

inline fun <reified T> View.findView(): List<T> {
    return findView(T::class).map { it as T }
}

fun View.findView(clazz: KClass<*>, view: ViewGroup? = null): List<View> {
    val result = mutableListOf<View>()
    val viewGroup = view ?: this as ViewGroup
    viewGroup.forEach { child ->
        if (child::class == clazz) {
            result.add(child)
        } else if (child is ViewGroup) {
            result.addAll(findView(clazz, child))
        }
    }
    return result
}