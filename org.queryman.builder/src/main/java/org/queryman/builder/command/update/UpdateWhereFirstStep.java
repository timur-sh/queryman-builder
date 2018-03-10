/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.update;

import org.queryman.builder.PostgreSQL;
import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * UPDATE .. WHERE .. | UPDATE .. WHERE CURRENT OF .. clause.
 *
 * @author Timur Shaidullin
 */
public interface UpdateWhereFirstStep extends UpdateReturningStep {
    /**
     * Example:
     * <code>
     * // UPDATE book AS b SET year = 2003 WHERE b.year > 1
     * update("book")
     *  .as("b")
     *  .set("year", 2003)
     *  .where("b.year", ">", "2010")
     *  .sql()
     * </code>
     */
    UpdateWhereManyStep where(String left, String operator, String right);

    /**
     * Example:
     * <code>
     * // UPDATE book SET year = 2003 WHERE "id" = 1
     * update("book")
     *  .set("year", 2003)
     *  .where(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
     */
    UpdateWhereManyStep where(Expression left, Operator operator, Expression right);

    /**
     * Subquery condition.
     * Example:
     * <code>
     *
     * // UPDATE book SET year = 2003 WHERE price < (SELECT MAX(total) FROM order)
     *
     * update("book")
     *  .set("year", 2003)
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
     * @see PostgreSQL#max(String)
     * @see PostgreSQL#asName(String)
     * @see PostgreSQL#operator(Object)
     */
    UpdateWhereManyStep where(Expression field, Operator operator, Query query);

    /**
     * This function useful in a few case:
     * <ul>
     *     <li>
     *         When the {@code conditions} is a special case of condition,
     *         like {@link PostgreSQL#conditionBetween(String, String, String)}, or
     *         {@link PostgreSQL#conditionSome(Expression, Operator, Query)} etc.
     *         See the first example.
     *     </li>
     *     <li>
     *         When the {@code conditions} is a group of conditions are joined by AND, AND NOT,
     *         OR and OR NOT operators. These conditions are being a grouped condition,
     *         and will be surrounded by parentheses.
     *         See the second example.
     *      </li>
     * </ul>
     *
     * The first example:
     * <code>
     * // UPDATE book SET year = 2003 WHERE id BETWEEN 1 AND 10
     * update("book")
     *  .set("year", 2003)
     *  .where(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // UPDATE book SET year = 2003 WHERE (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * update("book")
     *  .set("year", 2003)
     *  .where(
     *      conditionBetween("id", "1", "10")
     *      .and(asName("name"), operator("="), asString("Advanced SQL"))
     *  )
     *  .sql()
     * </code>
     *
     * @param conditions condition
     * @return itself
     *
     * Kind of conditions:
     * @see PostgreSQL#condition(String, String, Query)
     * @see PostgreSQL#condition(String, String, String)
     */
    UpdateWhereManyStep where(Conditions conditions);

    /**
     * Example:
     * <code>
     *
     * // UPDATE book SET year = 2003 WHERE EXISTS (SELECT * FROM author)
     * update("book")
     *  .set("year", 2003)
     *  .whereExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return itself
     */
    UpdateWhereManyStep whereExists(Query query);

    /**
     * Set a cursor name created by DECLARE statement.
     *
     * @param cursorName cursor name.
     * @return update returning step
     *
     * @see PostgreSQL#declare(String)
     */
    UpdateReturningStep whereCurrentOf(String cursorName);
}
