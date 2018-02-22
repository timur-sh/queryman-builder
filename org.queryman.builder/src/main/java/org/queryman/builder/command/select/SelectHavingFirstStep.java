/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.PostgreSQL;
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
     * // SELECT * FROM book HAVING year > 2010
     * select("year", "id")
     *  .from("book")
     *  .having("year", ">", "2010")
     *  .sql()
     * </code>
     */
    SelectHavingStep having(String left, String operator, String right);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE "id" = 1
     * select("*")
     *  .from("book")
     *  .having(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
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
     * This function useful in a few case:
     * <ul>
     *     <li>
     *         When the {@code conditions} is a special case of condition,
     *         like {@link PostgreSQL#conditionBetween(String, String, String)}, or
     *         {@link PostgreSQL#conditionSome(Expression, Operator, Query)} etc.
     *         See the first example.
     *     </li>
     *     <li>
     *         When the {@code conditions} is conditions are joined by AND, AND NOT,
     *         OR and OR NOT operators. These conditions is being a grouped condition,
     *         and will be surrounded by parentheses.
     *         See the second example.
     *      </li>
     * </ul>
     *
     * The first example:
     * <code>
     * // SELECT * FROM book HAVING id BETWEEN 1 AND 10
     * select("*")
     *  .from("book")
     *  .having(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // SELECT * FROM book HAVING (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * select("*")
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
     * @see PostgreSQL#condition(String, String, Query)
     * @see PostgreSQL#condition(String, String, String)
     */
    SelectHavingStep having(Conditions conditions);

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
    SelectHavingStep havingExists(Query query);
}