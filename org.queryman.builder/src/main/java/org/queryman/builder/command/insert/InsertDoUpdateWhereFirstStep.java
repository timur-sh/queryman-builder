/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.Query;
import org.queryman.builder.Queryman;
import org.queryman.builder.command.Conditions;

/**
 * INSERT INTO .. DO UPDATE .. WHERE .. step.
 *
 * @author Timur Shaidullin
 */
public interface InsertDoUpdateWhereFirstStep extends InsertReturningStep {

    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year &gt; 2010
     * select("*")
     *  .from("book")
     *  .where("year", "&gt;", "2010")
     *  .sql()
     * </code>
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     */
    <T> InsertDoUpdateWhereManySteps where(T left, T operator, T right);

    /**
     * This function useful in a few case:
     * <ul>
     *     <li>
     *         When the {@code conditions} is a special case of condition,
     *         like {@link Queryman#conditionBetween(Object, Object, Object)}, or
     *         {@link Queryman#conditionSome(Object, Object, Query)} etc.
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
     * @see Queryman#condition(Object, Object, Object)
     */
    InsertDoUpdateWhereManySteps where(Conditions conditions);

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
    InsertDoUpdateWhereManySteps whereExists(Query query);
}
