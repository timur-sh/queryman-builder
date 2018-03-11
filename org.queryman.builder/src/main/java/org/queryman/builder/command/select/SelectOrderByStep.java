/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.command.clause.OrderBy;

/**
 * @author Timur Shaidullin
 */
public interface SelectOrderByStep extends SelectLimitStep {

    /**
     * Specifies an ordering of output
     *
     * @param column column name or expression
     * @return SELECT .. LIMIT .. step
     */
    SelectLimitStep orderBy(String column);

    /**
     * Specifies an ordering of output
     *
     * @param column column name or expression
     * @param sorting sorting constant: ASC | DESC | USING constant
     * @return SELECT .. LIMIT .. step
     */
    SelectLimitStep orderBy(String column, String sorting);

    /**
     * Specifies an ordering of output.
     *
     * @param column column name or expression
     * @param sorting sorting constant: ASC | DESC | USING constant
     * @param nulls sets a position a row with null values for {@code name} column.
     *              It may be FIRST | LAST
     * @return SELECT .. LIMIT .. step
     */
    SelectLimitStep orderBy(String column, String sorting, String nulls);

    /**
     * Specifies an ordering of output.
     *
     * @param ordersBy list of {@link OrderBy} objects.
     * @return SELECT .. LIMIT .. step
     *
     * @see org.queryman.builder.PostgreSQL#orderBy(String)
     */
    SelectLimitStep orderBy(OrderBy... ordersBy);
}
