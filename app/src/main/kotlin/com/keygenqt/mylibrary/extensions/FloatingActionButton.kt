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

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.keygenqt.mylibrary.R

fun FloatingActionButton.showWithPadding(rv: RecyclerView) {
    this.show()
    val paddingTop = context.resources.getDimension(R.dimen.padding_list).toInt()
    val paddingBottom = context.resources.getDimension(R.dimen.padding_list_floating).toInt()
    rv.setPadding(0, paddingTop, 0, paddingBottom)
}

fun FloatingActionButton.hideWithPadding(rv: RecyclerView) {
    this.hide()
    val padding = context.resources.getDimension(R.dimen.padding_list).toInt()
    rv.setPadding(0, padding, 0, padding)
}