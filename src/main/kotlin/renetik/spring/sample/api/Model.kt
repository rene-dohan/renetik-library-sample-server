package renetik.spring.sample.api

import renetik.spring.sample.applicationUrl

val model by lazy { Model() }

class Model {

    val list by lazy { createList() }

    private fun createList() = mutableListOf<ListItem>().apply {
        var imageNumber = 1
        for (number in 1..500L) {
            add(ListItem(number, "$applicationUrl/images/flowers/flower$imageNumber.jpg", "Name $number", "Description $number"))
            if (++imageNumber == 5) imageNumber = 1
        }
    }

    fun resetToDefaults() {
        list.clear()
        list.addAll(createList())
    }
}