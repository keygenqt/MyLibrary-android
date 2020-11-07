package com.keygenqt.mylibrary.base

import androidx.annotation.*

/**
 * Annotation FragmentTitle for set title before change fragment
 * It fix delay twitches
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
@Target(AnnotationTarget.CLASS)
annotation class FragmentTitle(val title: String)