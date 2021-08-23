package com.epam.quotesservice.dto

data class QuoteView(
    val id: Long,
    val isin: String,
    val ask: Double,
    val bid: Double?,
    val elvl: Double?,
)
