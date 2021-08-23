package com.epam.quotesservice.service

import com.epam.quotesservice.TestEntities.ELVL
import com.epam.quotesservice.TestEntities.ISIN
import com.epam.quotesservice.TestEntities.quote
import com.epam.quotesservice.TestEntities.quotes
import com.epam.quotesservice.TestEntities.quotesHistory
import com.epam.quotesservice.TestEntities.quotesHistoryRecord
import com.epam.quotesservice.dto.QuoteView
import com.epam.quotesservice.repository.QuotesRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class QuotesServiceImplTest {

    private val repository = mockk<QuotesRepository>()

    private val service: QuotesService by lazy {
        QuotesServiceImpl(repository)
    }

    @Test
    fun `should process new quote - elvl does not exists`() {
        val quote = quote()
        every { repository.findQuote(any()) } returns quotes(quote.ask, quote.bid, null)
        every { repository.saveNewQuote(any(), any()) } returns quotesHistoryRecord(quote.ask, quote.bid, quote.bid)

        service.processNewQuote(quote).also {
            it.elvl shouldBe quote.bid
        }
    }

    @Test
    fun `should process new quote - bid is null`() {
        val quote = quote(bid = null)
        every { repository.findQuote(any()) } returns quotes(quote.ask, quote.bid, ELVL)
        every { repository.saveNewQuote(any(), any()) } returns
            quotesHistoryRecord(quote.ask, quote.bid, quote.ask)

        service.processNewQuote(quote).also {
            it.elvl shouldBe quote.ask
        }
    }

    @Test
    fun `should process new quote - elvl less then bid`() {
        val quote = quote()
        every { repository.findQuote(any()) } returns quotes(quote.ask, quote.bid, 100.0)
        every { repository.saveNewQuote(any(), any()) } returns
            quotesHistoryRecord(quote.ask, quote.bid, quote.bid)

        service.processNewQuote(quote).also {
            it.elvl shouldBe quote.bid
        }
    }

    @Test
    fun `should process new quote - elvl more then ask`() {
        val quote = quote()
        every { repository.findQuote(any()) } returns quotes(quote.ask, quote.bid, 101.8)
        every { repository.saveNewQuote(any(), any()) } returns
            quotesHistoryRecord(quote.ask, quote.bid, quote.ask)

        service.processNewQuote(quote).also {
            it.elvl shouldBe quote.ask
        }
    }

    @Test
    fun `should get all quotes`() {
        every { repository.findAllQuotes(ISIN) } returns listOf(quotesHistory())

        service.getAllQuotes(ISIN).also {
            it shouldNotBe null
            it shouldNotBe emptyList<QuoteView>()
        }
    }

    @Test
    fun `should get quote`() {
        every { repository.findQuote(ISIN) } returns quotes()

        service.getQuote(ISIN).also {
            it shouldNotBe null
            it?.isin shouldBe ISIN
            it?.elvl shouldBe ELVL
        }
    }
}