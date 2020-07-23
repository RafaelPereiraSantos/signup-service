package com.rafael.http

import com.rafael.config.ServiceConfiguration
import org.assertj.core.internal.Classes
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EligibleSearchController::class)
@ContextConfiguration(classes = [ServiceConfiguration::class])
class EligibleSearchControllerTest(
    @Autowired val mockMvc: MockMvc
) {

    @Test
    fun `test something`() {
        val email = "teste@user.com"
        mockMvc.perform(get("/eligibility?email=$email"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email", equalTo(email)))
    }


}
