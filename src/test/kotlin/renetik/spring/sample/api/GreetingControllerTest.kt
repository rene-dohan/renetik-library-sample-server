package renetik.spring.sample.api

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@WebMvcTest(GreetingController::class)
class GreetingControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @WithMockUser(value = "username")
    @Test
    fun testGreeting() {
        mvc?.perform(MockMvcRequestBuilders.get("/api/greeting"))?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.id").value(1))
                ?.andExpect(jsonPath("$.content").value("Hello, World"))
                ?.andDo(print())
    }
}