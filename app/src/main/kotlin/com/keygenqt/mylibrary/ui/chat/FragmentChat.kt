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

package com.keygenqt.mylibrary.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import com.keygenqt.mylibrary.annotations.ActionBarEnable
import com.keygenqt.mylibrary.annotations.BottomNavigationEnable
import com.keygenqt.mylibrary.base.BaseFragment
import com.keygenqt.mylibrary.databinding.FragmentChatBinding

@ActionBarEnable
@BottomNavigationEnable
class FragmentChat : BaseFragment<FragmentChatBinding>() {

    override fun onCreateBind(inflater: LayoutInflater, container: ViewGroup?): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun onCreateView() {}
}