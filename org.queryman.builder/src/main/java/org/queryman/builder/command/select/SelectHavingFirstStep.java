/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.Queryman;
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
     * // SELECT year FROM book HAVING year > 2010
     * select("year", "id")
     *  .from("book")
     *  .having("year", ">", "2010")
     *  .sql()
     * </code>
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     *
     */
    <T> SelectHavingManySteps having(T left, T operator, T right);

    /**
     * This function useful in a few case:
     * <ul>
     *     <li>
     *         When the {@code conditions} is a special case of condition,
     *         like {@link Queryman#conditionBetween(String, String, String)}, or
     *         {@link Queryman#conditionSome(Expression, Operator, Query)} etc.
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
     * // SELECT id FROM book HAVING id BETWEEN 1 AND 10
     * select("id")
     *  .from("book")
     *  .having(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // SELECT id, name FROM book HAVING (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * select("id", "name")
     *  .from("book")
     *  .having(
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
    SelectHavingManySteps having(Conditions conditions);

    /**
     * Example:
     * <code>
     *
     * // SELECT * FROM book HAVING EXISTS (SELECT * FROM author)
     * select("*")
     *  .from("book")
     *  .havingExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return itself
     */
    SelectHavingManySteps havingExists(Query query);
}