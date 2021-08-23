package com.epam.quotesservice.controller

import com.epam.quotesservice.domain.validate
import com.epam.quotesservice.dto.Quote
import com.epam.quotesservice.dto.QuoteView
import com.epam.quotesservice.service.QuotesService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Quotes controller - контроллер для работы с котировками
 */

@RestController
class QuotesController(
    private val service: QuotesService
) {
    @PostMapping("/new")
    fun newQuote(@RequestBody quote: Quote): ResponseEntity<Any> =
        service.processNewQuote(quote.validate())
            .let { ResponseEntity.ok(it) }

    @GetMapping("/quote")
    fun getQuote(
        @RequestParam("isin") isin: String
    ): ResponseEntity<QuoteView> =
        service.getQuote(isin).let { ResponseEntity.ok(it) }

    @GetMapping("/quotes")
    fun getAllQuotes(
        @RequestParam("isin") isin: String
    ): ResponseEntity<List<QuoteView>> =
        service.getAllQuotes(isin).let { ResponseEntity.ok(it) }
}
