package com.keygenqt.mylibrary.interfaces

/**
 * Interface for ViewModel with pagination
 *
 * @author      Vitaliy Zarubin
 * @version     %I%, %G%
 * @since       1.0
 */
interface ViewModelPage {
    fun updateList()
    fun updateList(page: Int)
}