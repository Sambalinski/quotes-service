package com.epam.quotesservice.repository

import com.epam.quotesservice.TestEntities.ISIN
import com.epam.quotesservice.jooq.tables.records.QuotesHistoryRecord
import io.kotest.matchers.shouldNotBe
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockConnection
import org.junit.jupiter.api.Test

internal class QuotesRepositoryImplTest {

    @Test
    fun `should find quote`() {
        val connection = MockConnection(QuoteMockDataProvider())
        val context = DSL.using(connection, SQLDialect.POSTGRES)
        val quote = QuotesRepositoryImpl(context).findQuote(ISIN)
        quote shouldNotBe null
    }

    @Test
    fun `should find all quotes`() {
        val connection = MockConnection(QuoteHistoryMockDataProvider())
        val context = DSL.using(connection, SQLDialect.POSTGRES)
        val quotes = QuotesRepositoryImpl(context).findAllQuotes(ISIN)
        quotes shouldNotBe null
        quotes shouldNotBe emptyList<QuotesHistoryRecord>()
    }
}
