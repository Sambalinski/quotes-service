package com.epam.quotesservice.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface LoggerTrait {
    fun logger(): Logger = LoggerFactory.getLogger(this::class.java)
}
