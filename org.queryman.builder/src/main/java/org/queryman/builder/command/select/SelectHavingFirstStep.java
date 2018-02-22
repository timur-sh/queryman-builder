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
 * The first step of having condition.
 * HAVING eliminates group rows that do not satisfy condition.
 *
 * @author Timur Shaidullin
 */
public interface SelectHavingFirstStep extends SelectCombiningQueryStep {
    /**
     * Example:
     * <code>
     * // SELECT * FROM book HAVING year > 2010 AND id = 1
     * select("year", "id")
     *  .from("book")
     *  .having("year", ">", "2010")
     *  .and("id", "=", "1")
     *  .sql()
     * </code>
     */
    SelectHavingStep having(String left, String operator, String right);

    /**
     */
    SelectHavingStep having(Expression left, Operator operator, Expression right);

    /**
     * Subquery condition.
     * Example:
     * <code>
     *
     * // SELECT price FROM book ... HAVING price <= (SELECT MAX(total) FROM order)
     * select("price")
     *  .from("book")
     *  ...
     *  .having(asName("price"), operator("<="), select(max("total")).from("order"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     *
     * @see org.queryman.builder.PostgreSQL#max(String)
     * @see org.queryman.builder.PostgreSQL#asName(String)
     * @see org.queryman.builder.PostgreSQL#operator(String)
     */
    SelectHavingStep having(Expression field, Operator operator, Query query);

    /**
     * HAVING eliminates group rows that do not satisfy condition.
     */
    SelectHavingStep having(Conditions conditions);

    /**
     * HAVING eliminates group rows that do not satisfy condition.
     */
    SelectHavingStep havingExists(Query query);
}