/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * @author Timur Shaidullin
 */
public interface SelectWhereFirstStep extends SelectGroupByStep {
    SelectWhereStep where(String left, String operator, String right);

    SelectWhereStep where(Expression left, Operator operator, Expression right);

    /**
     * Subquery condition.
     * Example:
     * <code>
     *
     * // SELECT price FROM book WHERE price < (SELECT MAX(total) FROM order)
     *
     * select("price")
     *  .from("book")
     *  .where("year", "<=", select(max("total")).from("order"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     *
     * @see org.queryman.builder.Operators#GTE
     * @see org.queryman.builder.PostgreSQL#max(String)
     * @see org.queryman.builder.PostgreSQL#asName(String)
     * @see org.queryman.builder.PostgreSQL#operator(String)
     */
    SelectWhereStep where(Expression field, Operator operator, Query query);

    SelectWhereStep where(Conditions conditions);

    SelectWhereStep whereExists(Query query);
}