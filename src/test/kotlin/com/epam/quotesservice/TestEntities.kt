package com.epam.quotesservice

import com.epam.quotesservice.dto.Quote
import com.epam.quotesservice.dto.QuoteView
import com.epam.quotesservice.jooq.tables.pojos.Quotes
import com.epam.quotesservice.jooq.tables.pojos.QuotesHistory
import com.epam.quotesservice.jooq.tables.records.QuotesHistoryRecord
import java.math.BigDecimal
import java.time.OffsetDateTime

object TestEntities {
    const val ISIN = "RU000A0JX0J2"

    private const val ASK = 101.7
    private const val BID = 100.1
    const val ELVL = 100.1

    fun quotes(ask: Double = ASK, bid: Double? = BID, elvl: Double? = ELVL) = Quotes(
        1,
        ISIN,
        BigDecimal.valueOf(ask),
        bid?.let { BigDecimal.valueOf(it) },
        elvl?.let { BigDecimal.valueOf(it) },
        OffsetDateTime.now()
    )

    fun quotesHistoryRecord(ask: Double, bid: Double?, elvl: Double?) = QuotesHistoryRecord(
        1,
        ISIN,
        BigDecimal.valueOf(ask),
        bid?.let { BigDecimal.valueOf(it) },
        elvl?.let { BigDecimal.valueOf(it) },
        OffsetDateTime.now()
    )

    fun quote(ask: Double = ASK, bid: Double? = BID) = Quote(
        ISIN,
        ask,
        bid
    )

    fun quotesHistory(ask: Double = ASK, bid: Double? = BID, elvl: Double? = ELVL) = QuotesHistory(
        1,
        ISIN,
        BigDecimal.valueOf(ask),
        bid?.let { BigDecimal.valueOf(it) },
        elvl?.let { BigDecimal.valueOf(it) },
        OffsetDateTime.now()
    )

    fun quoteView() = QuoteView(
        1,
        ISIN,
        ASK,
        BID,
        ELVL
    )
}
