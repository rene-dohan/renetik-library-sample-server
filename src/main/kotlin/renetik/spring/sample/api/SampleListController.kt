package renetik.spring.sample.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import renetik.spring.sample.webServerBaseURL


data class ListResponse(val success: Boolean, val list: List<ListItem>)
data class ListItem(val id: Long, val image: String, val name: String, val description: String)

@RestController
@RequestMapping("/api")
class SampleListController {

    val list by lazy {
        mutableListOf<ListItem>().apply {
            var imageNumber = 1
            for (number in 1..500L) {
                add(ListItem(number, "$webServerBaseURL/images/flowers/flower$imageNumber.jpg", "Name $number", "Description $number"))
                if (++imageNumber == 5) imageNumber = 1
            }
        }
    }

    @GetMapping("/sample-list")
    fun sampleList(@RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int): ListResponse {
        val endIndex = pageNumber * 20
        return ListResponse(true, list.subList(endIndex - 20, endIndex))
    }


}