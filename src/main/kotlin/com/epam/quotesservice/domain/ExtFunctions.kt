package com.epam.quotesservice.domain

import com.epam.quotesservice.domain.ValidationConstants.ISIN_LENGTH
import com.epam.quotesservice.domain.exceptions.QuoteValidateException
import com.epam.quotesservice.dto.Quote
import com.epam.quotesservice.dto.QuoteView
import com.epam.quotesservice.jooq.tables.pojos.Quotes
import com.epam.quotesservice.jooq.tables.pojos.QuotesHistory
import com.epam.quotesservice.jooq.tables.records.QuotesHistoryRecord

fun Quote.validate(): Quote {
    // Валидация isin
    if (isin.length != ISIN_LENGTH) throw QuoteValidateException("Isin's length should be exactly 12 symbols")
    // По условиям задачи
    if ((bid != null) &&
        (bid >= ask)
    ) throw QuoteValidateException("Bid should be less then Ask")

    return this
}

object ValidationConstants {
    const val ISIN_LENGTH = 12
}

fun QuotesHistoryRecord.toView(): QuoteView =
    QuoteView(
        id,
        isin,
        ask.toDouble(),
        bid?.toDouble(),
        elvl.toDouble(),
    )

fun Quotes.toView(): QuoteView =
    QuoteView(
        id,
        isin,
        ask.toDouble(),
        bid?.toDouble(),
        elvl.toDouble(),
    )

fun QuotesHistory.toView(): QuoteView =
    QuoteView(
        id,
        isin,
        ask.toDouble(),
        bid?.toDouble(),
        elvl.toDouble(),
    )
