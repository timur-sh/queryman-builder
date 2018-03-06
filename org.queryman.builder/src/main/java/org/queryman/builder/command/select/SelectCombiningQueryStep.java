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
     * Combine main query with {@code select} query using INTERSECT.
     */
    SelectCombiningQueryStep intersect(SelectFinalStep select);

    /**
     * Combine main query with {@code select} query using INTERSECT ALL.
     */
    SelectCombiningQueryStep intersectAll(SelectFinalStep select);

    /**
     * Combine main query with {@code select} query using EXCEPT.
     */
    SelectCombiningQueryStep except(SelectFinalStep select);

    /**
     * Combine main query with {@code select} query using EXCEPT ALL.
     */
    SelectCombiningQueryStep exceptAll(SelectFinalStep select);
}
