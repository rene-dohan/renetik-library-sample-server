package renetik.spring.sample.api

import renetik.spring.sample.applicationUrl

val model by lazy { Model() }

class Model {

    val list by lazy { createList() }

    private fun createList() = mutableListOf<ListItem>().apply {
        var imageNumber = 1
        for (number in 1..500L) {
            add(ListItem(number, imageUrl(imageNumber), "Name $number", "Description $number"))
            if (++imageNumber == 5) imageNumber = 1
        }
    }

    private fun imageUrl(imageNumber: Int) = "$applicationUrl/images/flowers/flower$imageNumber.jpg"

    fun resetToDefaults() {
        list.clear()
        list.addAll(createList())
    }

    fun addListItem(name: String, description: String) =
            ListItem(89080, imageUrl(2), name, description).apply { list.add(0, this) }

}