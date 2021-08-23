package com.epam.quotesservice.dto

data class Quote(
    val isin: String,
    val ask: Double,
    val bid: Double?,
)
