/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.PostgreSQL;
import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * INSERT INTO .. ON CONFLICT .. WHERE .. step.
 *
 * @author Timur Shaidullin
 */
public interface InsertOnConflictWhereFirstStep extends InsertConflictActionStep {
    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .sql()
     * </code>
     */
    InsertOnConflictWhereManyStep where(String left, String operator, String right);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE "id" = 1
     * select("*")
     *  .from("book")
     *  .where(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
     */
    InsertOnConflictWhereManyStep where(Expression left, Operator operator, Expression right);

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
    InsertOnConflictWhereManyStep where(Expression field, Operator operator, Query query);

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
     * // SELECT * FROM book WHERE id BETWEEN 1 AND 10
     * select("*")
     *  .from("book")
     *  .where(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // SELECT * FROM book WHERE (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * select("*")
     *  .from("book")
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
    InsertOnConflictWhereManyStep where(Conditions conditions);

    /**
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE EXISTS (SELECT * FROM author)
     * select("*")
     *  .from("book")
     *  .whereExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return itself
     */
    InsertOnConflictWhereManyStep whereExists(Query query);
}
