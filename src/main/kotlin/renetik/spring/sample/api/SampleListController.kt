package renetik.spring.sample.api

import org.springframework.http.CacheControl.maxAge
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import renetik.spring.sample.webServerBaseURL
import java.util.concurrent.TimeUnit.MINUTES

data class ListResponse(val success: Boolean, val list: List<ListItem>)
data class ListItem(val id: Long, val image: String, val name: String, val description: String)

@RestController
@RequestMapping("/api")
class SampleListController {

    @GetMapping("/sample-list")
    fun sampleList(@RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int): ResponseEntity<Any> {
        val endIndex = pageNumber * 20
        return cached(ListResponse(true, model.list.subList(endIndex - 20, endIndex)))
    }

    @PostMapping("/sample-list/add")
    fun sampleList(@RequestBody item: ListItem): Response {
        model.list.add(0, item)
        return Response(true)
    }

    private fun cached(data: Any) = ok().cacheControl(maxAge(4, MINUTES)).body(data)

}