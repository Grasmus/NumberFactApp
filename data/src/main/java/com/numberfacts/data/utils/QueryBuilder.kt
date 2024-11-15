package com.numberfacts.data.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import com.numberfacts.domain.constants.OrderBy
import com.numberfacts.domain.constants.OrderDirection

class QueryBuilder(private var baseQuery: String) {

    fun applyFilter(from: Int?, till: Int?): QueryBuilder {
        from?.let {
            till?.let {
                baseQuery += " WHERE number BETWEEN $from AND $till"
            }
        }

        return this
    }

    fun orderBy(orderBy: OrderBy, orderDirection: OrderDirection): QueryBuilder {
        baseQuery += when (orderBy) {
            OrderBy.Date -> " ORDER BY date_time"
            OrderBy.Number -> " ORDER BY number"
        }

        baseQuery += when (orderDirection) {
            OrderDirection.Ascending -> " ASC"
            OrderDirection.Descending -> " DESC"
        }

        return this
    }

    fun build() = SimpleSQLiteQuery(baseQuery)
}
