package renetik.spring.sample.api

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import renetik.spring.sample.api.SampleListController.ListItemResponse
import kotlin.reflect.KClass


@RunWith(SpringRunner::class)
@WebMvcTest(SampleListController::class)
@WithMockUser(value = "username")
class SampleListControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @Test
    fun testSampleList() {
        getSampleListAndCheckIfFirstListItemNameIs("Name 1")
    }

    @Test
    fun testSampleListPage3() {
        mvc?.perform(get("/api/sampleList").param("pageNumber", "3"))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.list[0].id").value("41"))
                ?.andExpect(header().string("Cache-Control", "max-age=240"))
                ?.andDo(print())
    }

    @Test
    fun testSampleListAdd() {
        val listItem = ListItemAdd("new name", "new description")
        addItemToList(listItem)
        getSampleListAndCheckIfFirstListItemNameIs(listItem.name)
    }

    @Test
    fun testSampleListDeleteWrongId() {
        val itemId = "6786786"
        mvc?.perform(post("/api/sampleList/delete").param("id", itemId))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.message").value(containsString("id:$itemId")))
                ?.andExpect(header().string("Pragma", "no-cache"))
                ?.andDo(print())
    }

    @Test
    fun testSampleListDelete() {
        getSampleListAndCheckIfFirstListItemNameIs("Name 1")
        val item = ListItemAdd("new name", "new description")
        val response = addItemToList(item)!!
        getSampleListAndCheckIfFirstListItemNameIs(item.name)
        mvc?.perform(post("/api/sampleList/delete").param("id", "${response.value.id}"))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.message").doesNotExist())
                ?.andExpect(header().string("Pragma", "no-cache"))
                ?.andDo(print())
        getSampleListAndCheckIfFirstListItemNameIs("Name 1")
    }

    private fun addItemToList(listItem: ListItemAdd) =
            mvc?.perform(post("/api/sampleList/add")
                    .content(asJsonString(listItem)).contentType(APPLICATION_JSON))
                    ?.andExpect(status().isOk)
                    ?.andExpect(jsonPath("$.success").value(true))
                    ?.andExpect(jsonPath("$.message").doesNotExist())
                    ?.andExpect(header().string("Pragma", "no-cache"))
                    ?.andDo(print())
                    ?.jsonToObject(ListItemResponse::class)

    private fun getSampleListAndCheckIfFirstListItemNameIs(name: String) =
            mvc?.perform(get("/api/sampleList")
            )?.andExpect(status().isOk)
                    ?.andExpect(jsonPath("$.success").value(true))
                    ?.andExpect(jsonPath("$.list[0].name").value(name))
                    ?.andDo(print())

    @After
    fun cleanup() {
        model.resetToDefaults()
    }

}

private fun asJsonString(obj: Any): String = ObjectMapper().writeValueAsString(obj)

private fun <T : Any> ResultActions.jsonToObject(objectClass: KClass<T>) = try {
    val mapper = ObjectMapper()
    mapper.setSerializationInclusion(Include.NON_NULL)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
    mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false)
    mapper.readValue(andReturn().response.contentAsString, objectClass.java)
} catch (exception: Exception) {
    exception.printStackTrace()
    null
}