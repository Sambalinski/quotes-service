package com.epam.quotesservice.controller

import com.epam.quotesservice.domain.exceptions.QuoteValidateException
import com.epam.quotesservice.service.QuotesService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
internal class ControllerAdviceTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @MockkBean
    lateinit var service: QuotesService


    @Test
    fun `should handle exception`() {

        val stringContent = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/new")
                .content(
                    """
                    {
                    	"isin": "RU000A0JX0J1",
                    	"ask": 100.1,
                    	"bid": 100.7
                    }
                """.trimIndent()
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError)
            .andReturn()
            .response.contentAsString

        objectMapper.readValue<QuoteValidateException>(stringContent).also {
            it.message shouldBe "Bid should be less then Ask"
        }
    }
}
