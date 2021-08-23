package com.epam.quotesservice.repository

import com.epam.quotesservice.TestEntities.ISIN
import com.epam.quotesservice.jooq.Tables.QUOTES
import com.epam.quotesservice.jooq.Tables.QUOTES_HISTORY
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.tools.jdbc.MockDataProvider
import org.jooq.tools.jdbc.MockExecuteContext
import org.jooq.tools.jdbc.MockResult
import java.math.BigDecimal
import java.time.OffsetDateTime

class QuoteMockDataProvider : MockDataProvider {
    override fun execute(ctx: MockExecuteContext?): Array<MockResult> = run {
        val context = DSL.using(SQLDialect.POSTGRES)
        val recordResult = context.newResult(QUOTES)
            .apply {
                add(
                    context
                        .newRecord(QUOTES)
                        .values(
                            1,
                            ISIN,
                            BigDecimal.valueOf(101.7),
                            BigDecimal.valueOf(100.1),
                            BigDecimal.valueOf(100.1),
                            OffsetDateTime.now()
                        )
                )
            }

        return arrayOf(MockResult(1, recordResult))
    }
}

class QuoteHistoryMockDataProvider : MockDataProvider {
    override fun execute(ctx: MockExecuteContext?): Array<MockResult> = run {
        val context = DSL.using(SQLDialect.POSTGRES)

        val recordResult1 = context.newResult(QUOTES_HISTORY)
            .apply {
                add(
                    context
                        .newRecord(QUOTES_HISTORY)
                        .values(
                            1,
                            ISIN,
                            BigDecimal.valueOf(101.7),
                            BigDecimal.valueOf(100.1),
                            BigDecimal.valueOf(100.1),
                            OffsetDateTime.now()
                        )
                )
            }

        return arrayOf(MockResult(1, recordResult1))
    }
}
