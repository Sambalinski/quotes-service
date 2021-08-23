package com.epam.quotesservice.service

import com.epam.quotesservice.config.LoggerTrait
import com.epam.quotesservice.domain.toView
import com.epam.quotesservice.dto.Quote
import com.epam.quotesservice.dto.QuoteView
import com.epam.quotesservice.repository.QuotesRepository
import org.springframework.stereotype.Service

/**
 * Quotes service - сервис для работы с котировками
 */
interface QuotesService {

    /**
     * Process new quote - обработка котировки
     *
     * @param quote - новая котировка
     */
    fun processNewQuote(quote: Quote): QuoteView

    /**
     * Get all quotes - Получить все котировки
     *
     * @param isin - Международный идентификационный код ценной бумаги
     */
    fun getAllQuotes(isin: String): List<QuoteView>

    /**
     * Get quote - получение котировки по isin
     *
     * @param isin
     */
    fun getQuote(isin: String): QuoteView?
}

@Service
class QuotesServiceImpl(
    private val repository: QuotesRepository
) : QuotesService, LoggerTrait {
    override fun processNewQuote(quote: Quote) = with(quote) {
        logger().info("Received a new quote ${quote.isin}")

        bid?.let {
            repository.findQuote(isin)
                ?.elvl?.toDouble()
                ?.let {
                    when {
                        it < bid -> bid
                        it > ask -> ask
                        else -> it
                    }
                }?.let { elvl ->
                    repository.saveNewQuote(this, elvl).toView()
                } ?: let {
                // Если значение elvl для данной бумаги отсутствует, то elvl = bid
                repository.saveNewQuote(this, bid).toView()
            }
        } ?: let {
            // Если bid отсутствует, то elvl = ask
            repository.saveNewQuote(this, ask).toView()
        }
    }.also {
        logger().info("Quote ${quote.isin} successfully processed")
    }

    override fun getAllQuotes(isin: String): List<QuoteView> =
        repository.findAllQuotes(isin).map {
            it.toView()
        }

    override fun getQuote(isin: String): QuoteView? =
        repository.findQuote(isin)?.toView()
}
