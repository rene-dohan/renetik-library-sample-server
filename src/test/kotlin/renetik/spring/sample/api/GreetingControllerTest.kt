package renetik.spring.sample.api

import org.hamcrest.Matchers.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@WebMvcTest(GreetingController::class)
@WithMockUser(value = "username")
class GreetingControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @Test
    fun testGreeting() {
        mvc?.perform(MockMvcRequestBuilders.get("/api/greeting"))?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.message").value(startsWith("Hello, World")))
                ?.andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
                ?.andExpect(header().string("Pragma", "no-cache"))
                ?.andDo(print())
    }

    @Test
    fun testPing() {
        mvc?.perform(MockMvcRequestBuilders.get("/api/ping"))?.andExpect(status().isOk)
                ?.andExpect(jsonPath("$.success").value(true))
                ?.andExpect(jsonPath("$.message").doesNotExist())
                ?.andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
                ?.andExpect(header().string("Pragma", "no-cache"))
                ?.andDo(print())
    }
}