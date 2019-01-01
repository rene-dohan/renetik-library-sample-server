package renetik.spring.sample.api

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@WebMvcTest(SampleListController::class)
class SampleListControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @WithMockUser(value = "username")
    @Test
    fun testSampleList() {
        mvc?.perform(MockMvcRequestBuilders.get("/api/sample-list"))?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.list[0].name").value("Name 1"))
                ?.andDo(print())
    }
}