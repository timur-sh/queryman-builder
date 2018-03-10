/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command;

import org.queryman.builder.Query;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.Node;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;
import org.queryman.builder.PostgreSQL;

/**
 * This supplies a search condition.
 * Conditions are used in different context, some of usual examples:
 * <ul>
 *     <li>SELECT .. FROM .. JOIN  .. ON <i>conditions</i></li>
 *     <li>SELECT .. WHERE <i>conditions</i></li>
 *     <li>SELECT .. HAVING <i>conditions</i></li>
 *     <li>DELETE .. WHERE <i>conditions</i></li>
 *     <li>UPDATE .. WHERE <i>conditions</i></li>
 * </ul>
 *
 * Each condition is built from operands and operator returning a boolean type
 * value (=, !=, IN, IS, IS NOT etc). Internally, any operator is being a {@link Expression}
 * object. And operator is being {@link Operator} object. For convenience sake
 * it's possible to use a {@code String} representation of them, which will
 * convert into needful type.
 *
 * Example:
 * <p><code>
 * PostgreSQL.conditions("id", "=", "1")
 *    .and("name", "=", "Alan")
 *    .and(condition("age", ">", "29")
 *       .and("gender", "!=", "male")
 *       .or("code", "=", "3")
 *    );
 * </code></p>
 *
 * Conditions can be grouped:
 * <p><code>
 * // "id" = 1 AND (users.name = 'Alan' AND gender != 'male' OR code = 3)
 * PostgreSQL.conditions(asQuotedName("id"), operator("="), asNumber("1"))
 *    .and(asName("users.name"), "=", asString("Alan")
 *       .and(asName("gender"), "!=", asString("male"))
 *       .or(asName("code"), "=", asNumber("3"))
 *    );
 * </code></p>
 *
 *
 * See conditions' methods in {@link org.queryman.builder.PostgreSQL}:
 * @see org.queryman.builder.PostgreSQL#condition(Expression, Operator, Expression)
 *
 * @author Timur Shaidullin
 */
public interface Conditions extends AstVisitor {
    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 AND "id" = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .and(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     */
    <T>Conditions and(T left, T operator, T right);

    /**
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 AND EXISTS (SELECT * FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .andExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return itself
     */
    Conditions andExists(Query query);

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
     * // SELECT * FROM book WHERE year > 2010 AND id BETWEEN 1 AND 10
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .and(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 AND (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .and(
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
     * @see PostgreSQL#condition(Object, Object, Object)
     */
    Conditions and(Conditions conditions);


    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 AND NOT "id" = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .andNot(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     */
    <T> Conditions andNot(T left, T operator, T right);

    /**
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 AND NOT EXISTS (SELECT * FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .andNotExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return itself
     */
    Conditions andNotExists(Query query);

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
     * // SELECT * FROM book WHERE year > 2010 AND NOT id BETWEEN 1 AND 10
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .andNot(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 AND NOT (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .andNot(
     *      conditionBetween("id", "1", "10")
     *      ..and(asName("name"), operator("="), asString("Advanced SQL"))
     *  )
     *  .sql()
     * </code>
     *
     * @param conditions condition
     * @return itself
     *
     * Kind of conditions:
     * @see PostgreSQL#condition(Object, Object, Object)
     */
    Conditions andNot(Conditions conditions);


    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 OR id = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .or("id", "=", "1")
     *  .sql()
     * </code>
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     */
    <T> Conditions or(T left, T operator, T right);

    /**
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 OR EXISTS (SELECT * FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return itself
     */
    Conditions orExists(Query query);

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
     * // SELECT * FROM book WHERE year > 2010 OR id BETWEEN 1 AND 10
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .or(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 OR (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .ir(
     *      conditionBetween("id", "1", "10")
     *      ..and(asName("name"), operator("="), asString("Advanced SQL"))
     *  )
     *  .sql()
     * </code>
     *
     * @param conditions condition
     * @return itself
     *
     * Kind of conditions:
     * @see PostgreSQL#condition(Object, Object, Object)
     */
    Conditions or(Conditions conditions);


    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 OR NOT "id" = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orNot(asQuotedName("id"), "=", 1)
     *  .sql()
     * </code>
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     */
    <T> Conditions orNot(T left, T operator, T right);

    /**
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 OR NOT EXISTS (SELECT * FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orNotExists(select("*").from("authors"))
     *  .sql()
     * </code>
     *
     * @param query subquery
     * @return itself
     */
    Conditions orNotExists(Query query);

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
     * // SELECT * FROM book WHERE year > 2010 OR NOT id BETWEEN 1 AND 10
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orNot(conditionBetween("id", "1", "10"))
     *  .sql()
     * </code>
     *
     * The second example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 OR NOT (id BETWEEN 1 AND 10 AND name = 'Advanced SQL')
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orNot(
     *      conditionBetween("id", "1", "10")
     *      ..and(asName("name"), operator("="), asString("Advanced SQL"))
     *  )
     *  .sql()
     * </code>
     *
     * @param conditions condition
     * @return itself
     *
     * Kind of conditions:
     * @see PostgreSQL#condition(Object, Object, Object)
     */
    Conditions orNot(Conditions conditions);


    Node getNode();
}
