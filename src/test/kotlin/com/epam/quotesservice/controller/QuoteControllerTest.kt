package com.epam.quotesservice.controller

import com.epam.quotesservice.TestEntities.ISIN
import com.epam.quotesservice.TestEntities.quoteView
import com.epam.quotesservice.service.QuotesService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class QuoteControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var service: QuotesService

    @Test
    fun `should process new quote`() {
        every { service.processNewQuote(any()) } returns quoteView()

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                    	"isin": "RU000A0JX0J1",
                    	"ask": 100.7,
                    	"bid": 100.1
                    }
                """.trimIndent()
                )
        ).andExpect(status().isOk)
    }

    @Test
    fun `should return quote`() {
        every { service.getQuote(any()) } returns quoteView()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/quote")
                .accept(MediaType.APPLICATION_JSON)
                .queryParam("isin", ISIN)
        ).andExpect(status().isOk)
    }
}