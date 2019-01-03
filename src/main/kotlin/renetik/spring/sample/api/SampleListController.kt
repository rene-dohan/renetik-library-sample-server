package renetik.spring.sample.api

import org.springframework.http.CacheControl.maxAge
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit.MINUTES

data class ListResponse(val success: Boolean, val list: List<ListItem>)
data class ListItem(val id: Long, val image: String, val name: String, val description: String)

@RestController
@RequestMapping("/api")
class SampleListController {

    @GetMapping("/sampleList")
    fun load(@RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int) = restOperation {
        val endIndex = pageNumber * 20
        cached(ListResponse(true, model.list.subList(endIndex - 20, endIndex)))
    }

    @PostMapping("/sampleList/add")
    fun add(@RequestBody item: ListItem) = restOperation {
        model.list.add(0, item)
        Response(true)
    }

    @PostMapping("/sampleList/delete")
    fun delete(@RequestParam(value = "id") id: Long) = restOperation {
        val removed = model.list.removeIf { it.id == id }
        Response(true, if (!removed) "Item with id:$id not found" else null)
    }

    private fun cached(data: Any): ResponseEntity<Any> = ok().cacheControl(maxAge(4, MINUTES)).body(data)
}

