/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

/**
 * Step combining query: UNION, INTERSECT, EXCEPT
 *
 *
 * @author Timur Shaidullin
 */
public interface SelectCombiningQueryStep extends SelectOrderByStep {
    /**
     * Combine main query with {@code select} query using UNION.
     */
    SelectCombiningQueryStep union(SelectFinalStep select);

    /**
     * Combine main query with {@code select} query using UNION ALL.
     */
    SelectCombiningQueryStep unionAll(SelectFinalStep select);

    /**
     * Combine main query with {@code select} query using UNION DISTINCT.
     */
    SelectCombiningQueryStep unionDistinct(SelectFinalStep select);
}
