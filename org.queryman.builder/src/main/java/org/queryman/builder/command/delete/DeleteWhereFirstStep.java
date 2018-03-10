/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.delete;

import org.queryman.builder.PostgreSQL;
import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * DELETE FROM .. WHERE | DELETE FROM .. WHERE CURRENT OF .. clause.
 *
 * @author Timur Shaidullin
 */
public interface DeleteWhereFirstStep extends DeleteReturningStep {
    /**
     * Example:
     * <code>
     * // DELETE FROM book WHERE b.year > 1
     * deleteFrom("book")
     *  .as("b")
     *  .where("b.year", ">", "2010")
     *  .sql()
     * </code>
     */
    DeleteWhereManyStep where(String left, String operator, String right);

    /**
     * Example:
     * <code>
     * // DELETE FROM book WHERE "id" = 1
     * deleteFrom("book")
     *  .where(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
     */
    DeleteWhereManyStep where(Expression left, Operator operator, Expression right);

    /**
     * Subquery condition.
     * Example:
     * <code>
     *
     * // DELETE FROM book WHERE price < (SELECT MAX(total) FROM order)
     *
     * deleteFrom("book")
     *  .where("year", "<=", select(max("total")).from("order"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return delete where many steps
     *
     * @see org.queryman.builder.Operators#GTE
     * @see org.queryman.builder.PostgreSQL#max(String)
     * @see org.queryman.builder.PostgreSQL#asName(String)
     * @see org.queryman.builder.PostgreSQL#operator(Object)
     */
    DeleteWhereManyStep where(Expression field, Operator operator, Query query);

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
     * // DELETE FROM book WHERE id BETWEEN 1 AND 10
     * deleteFrom("book")
     *  .where(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // DELETE FROM book WHERE (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * deleteFrom("book")
     *  .where(
     *      conditionBetween("id", "1", "10")
     *      .and(asName("name"), operator("="), asString("Advanced SQL"))
     *  )
     *  .sql()
     * </code>
     *
     * @param conditions condition
     * @return delete where many steps
     *
     * Kind of conditions:
     * @see PostgreSQL#condition(String, String, Query)
     * @see PostgreSQL#condition(String, String, String)
     */
    DeleteWhereManyStep where(Conditions conditions);

    /**
     * Example:
     * <code>
     *
     * // DELETE FROM FROM book WHERE EXISTS (SELECT * FROM author)
     * deleteFrom("book")
     *  .whereExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return delete where many steps
     */
    DeleteWhereManyStep whereExists(Query query);

    /**
     * Set a cursor name created by DECLARE statement.
     *
     * @param cursorName cursor name.
     * @return delete returning step
     *
     * @see PostgreSQL#declare(String)
     */
    DeleteReturningStep whereCurrentOf(String cursorName);
}
