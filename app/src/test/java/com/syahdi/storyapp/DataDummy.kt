package com.syahdi.storyapp

import com.syahdi.storyapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "https://media.licdn.com/dms/image/D5603AQGrxsv843c_6Q/profile-displayphoto-shrink_200_200/0/1685629999168?e=1691625600&v=beta&t=8eQVHt9QXZv_vzoRSYVG-L6-WRj9oW4G-KMIM4f2qlw",
                "date $i",
                "user $i",
                "description $i"
            )
            items.add(story)
        }
        return items
    }

}