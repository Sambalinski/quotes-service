package com.epam.quotesservice.repository

import com.epam.quotesservice.dto.Quote
import com.epam.quotesservice.jooq.Tables
import com.epam.quotesservice.jooq.tables.pojos.Quotes
import com.epam.quotesservice.jooq.tables.pojos.QuotesHistory
import com.epam.quotesservice.jooq.tables.records.QuotesHistoryRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * Quotes repository - репозиторий для работы с котировками
 */
interface QuotesRepository {
    fun saveNewQuote(quote: Quote, elvl: Double): QuotesHistoryRecord
    fun findQuote(isin: String): Quotes?
    fun findAllQuotes(isin: String): List<QuotesHistory>
}

@Repository
class QuotesRepositoryImpl(
    private val dslContext: DSLContext
) : QuotesRepository {

    companion object {
        private val q = Tables.QUOTES
        private val qh = Tables.QUOTES_HISTORY
    }

    @Transactional
    override fun saveNewQuote(quote: Quote, elvl: Double): QuotesHistoryRecord =
        dslContext.transactionResult { cfg ->
            upsertQuotes(quote, elvl, cfg.dsl())
            saveQuotesHistory(quote, elvl, cfg.dsl())
        }

    private fun upsertQuotes(quote: Quote, elvl: Double, ctx: DSLContext) {
        ctx.insertInto(q)
            .set(q.ISIN, quote.isin)
            .set(q.ASK, BigDecimal.valueOf(quote.ask))
            .set(q.BID, quote.bid?.let { BigDecimal.valueOf(it) })
            .set(q.ELVL, BigDecimal.valueOf(elvl))
            .onConflict(q.ISIN)
            .doUpdate()
            .set(q.ASK, BigDecimal.valueOf(quote.ask))
            .set(q.BID, quote.bid?.let { BigDecimal.valueOf(it) })
            .set(q.ELVL, BigDecimal.valueOf(elvl))
            .where(q.ISIN.eq(quote.isin))
            .execute()
    }

    private fun saveQuotesHistory(quote: Quote, elvl: Double, ctx: DSLContext): QuotesHistoryRecord =
        ctx.newRecord(qh)
            .apply {
                isin = quote.isin
                ask = BigDecimal.valueOf(quote.ask)
                bid = quote.bid?.let { BigDecimal.valueOf(it) }
                this.elvl = BigDecimal.valueOf(elvl)
            }.also { it.store() }

    override fun findQuote(isin: String) =
        dslContext.selectFrom(q)
            .where(q.ISIN.eq(isin))
            .fetchOneInto(Quotes::class.java)

    override fun findAllQuotes(isin: String): List<QuotesHistory> =
        dslContext.selectFrom(qh)
            .where(qh.ISIN.eq(isin))
            .fetchInto(QuotesHistory::class.java)
}
