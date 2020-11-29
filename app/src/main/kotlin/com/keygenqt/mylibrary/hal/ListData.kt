package com.keygenqt.mylibrary.hal

import com.google.gson.annotations.SerializedName

open class ListData<T>(
    @SerializedName("_embedded")
    open var embedded: HashMap<String, List<T>> = hashMapOf(),

    @SerializedName("_links")
    open var links: HashMap<String, Link> = hashMapOf(),

    @SerializedName("page")
    var page: Page? = null
) {

    var itemsAny: List<Any>
        get() {
            embedded.forEach { return it.value.map { m -> m as Any } }
            return emptyList()
        }
        set(value) {
            if (value.isNotEmpty()) {
                embedded[embedded.keys.first()] = value.map { m -> m as T }
            }
        }

    var items: List<T>
        get() {
            embedded.forEach { return it.value }
            return emptyList()
        }
        set(value) {
            embedded[embedded.keys.first()] = value
        }

    val linkFirst: Link?
        get() {
            if (links.containsKey("first")) {
                return links["first"]
            }
            return null
        }

    val linkSelf: Link?
        get() {
            if (links.containsKey("self")) {
                return links["self"]
            }
            return null
        }

    val linkNext: Link?
        get() {
            if (links.containsKey("next")) {
                return links["next"]
            }
            return null
        }

    val linkLast: Link?
        get() {
            if (links.containsKey("last")) {
                return links["last"]
            }
            return null
        }

    val linkProfile: Link?
        get() {
            if (links.containsKey("profile")) {
                return links["profile"]
            }
            return null
        }

    fun mergeItems(linkSearch: LinkListSearch): ListData<T> {
        linkSearch.items.addAll(itemsAny)
        itemsAny = linkSearch.items
        return this
    }

    fun mergeItems(linkSearch: LinkList): ListData<T> {
        linkSearch.items.addAll(itemsAny)
        itemsAny = linkSearch.items
        return this
    }

    fun updateItem(index: Int, item: T) {
        val list = ArrayList(items)
        list[index] = item
        items = list
    }
}