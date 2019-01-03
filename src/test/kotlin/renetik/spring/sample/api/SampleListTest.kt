package renetik.spring.sample.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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
        mvc?.perform(get("/api/sampleList").with(csrf()).param("pageNumber", "3"))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.list[0].id").value("41"))
                ?.andExpect(header().string("Cache-Control", "max-age=240"))
                ?.andDo(print())
    }

    @Test
    fun testSampleListAdd() {
        val listItem = ListItem(890890, "new image", "new name", "new description")
        addItemToList(listItem)
        getSampleListAndCheckIfFirstListItemNameIs(listItem.name)
    }

    @Test
    fun testSampleListDeleteWrongId() {
        val itemId = "6786786"
        mvc?.perform(post("/api/sampleList/delete").with(csrf()).param("id", itemId))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.message").value(containsString("id:$itemId")))
                ?.andExpect(MockMvcResultMatchers.header().string("Pragma", "no-cache"))
                ?.andDo(print())
    }

    @Test
    fun testSampleListDelete() {
        getSampleListAndCheckIfFirstListItemNameIs("Name 1")
        val listItem = ListItem(890890, "new image", "new name", "new description")
        addItemToList(listItem)
        getSampleListAndCheckIfFirstListItemNameIs(listItem.name)
        mvc?.perform(post("/api/sampleList/delete").with(csrf()).param("id", "${listItem.id}"))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.message").doesNotExist())
                ?.andExpect(MockMvcResultMatchers.header().string("Pragma", "no-cache"))
                ?.andDo(print())
        getSampleListAndCheckIfFirstListItemNameIs("Name 1")
    }

    private fun addItemToList(listItem: ListItem) =
        mvc?.perform(post("/api/sampleList/add").with(csrf())
                .content(asJsonString(listItem)).contentType(APPLICATION_JSON))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.message").doesNotExist())
                ?.andExpect(header().string("Pragma", "no-cache"))
                ?.andDo(print())

    private fun getSampleListAndCheckIfFirstListItemNameIs(name: String) =
        mvc?.perform(get("/api/sampleList").with(csrf()))
                ?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.list[0].name").value(name))
                ?.andDo(print())

    @After
    fun cleanup() {
        model.resetToDefaults()
    }
}

fun asJsonString(obj: Any): String = ObjectMapper().writeValueAsString(obj)