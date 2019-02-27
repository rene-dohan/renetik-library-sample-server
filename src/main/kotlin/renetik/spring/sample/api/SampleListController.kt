package renetik.spring.sample.api

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.CacheControl.maxAge
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit.MINUTES


data class ListItem(@JsonProperty("id") val id: Long,
                    @JsonProperty("image") val image: String,
                    @JsonProperty("name") val name: String,
                    @JsonProperty("description") val description: String)

data class ListItemAdd(@JsonProperty("name") val name: String,
                       @JsonProperty("description") val description: String)

@RestController
@RequestMapping("/api")
class SampleListController {

    @GetMapping("/sampleList")
    fun load(@RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int) = restOperation {
        val endIndex = pageNumber * 20
        cached(ListResponse(true, model.list.subList(endIndex - 20, endIndex)))
    }

    data class ListItemResponse(@JsonProperty("success") val success: Boolean,
                                @JsonProperty("value") val value: ListItem)

    @PostMapping("/sampleList/add")
    fun add(@RequestBody item: ListItemAdd) = restOperation {
        val addedItem = model.addListItem(item.name, item.description)
        ListItemResponse(true, addedItem)
    }

    @PostMapping("/sampleList/delete")
    fun delete(@RequestParam(value = "id") id: Long) = restOperation {
        val removed = model.list.removeIf { it.id == id }
        Response(true, if (!removed) "Item with id:$id not found" else null)
    }
}

