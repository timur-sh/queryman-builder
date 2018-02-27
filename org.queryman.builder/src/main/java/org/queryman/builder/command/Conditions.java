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
 * value (=, !=, IN, IS, IS NOT etc). Internally, any operand is being a {@link Expression}
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
     * // SELECT * FROM book WHERE year > 2010 AND id = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .and("id", "=", "1")
     *  .sql()
     * </code>
     */
    Conditions and(String leftField, String operator, String rightField);

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
     */
    Conditions and(Expression leftField, Operator operator, Expression rightField);

    /**
     * Subquery condition. It is used primarily by {@code IN} expression:
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 AND author_id IN (SELECT id FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .and(asName("author_id"), IN, select("id").from("authors"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     *
     * @see org.queryman.builder.Operators#IN
     */
    Conditions and(Expression field, Operator operator, Query query);

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
     * @see PostgreSQL#condition(String, String, Query)
     * @see PostgreSQL#condition(String, String, String)
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
     */
    Conditions andNot(Expression leftField, Operator operator, Expression rightField);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 AND NOT id = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .andNot("id", "=", "1")
     *  .sql()
     * </code>
     */
    Conditions andNot(String leftField, String operator, String rightField);

    /**
     * Subquery condition. It is used primarily by {@code IN} expression:
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 AND NOT author_id IN (SELECT id FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .andNot(asName("author_id"), IN, select("id").from("authors"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     *
     * @see org.queryman.builder.Operators#IN
     */
    Conditions andNot(Expression field, Operator operator, Query query);

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
     * @see PostgreSQL#condition(String, String, Query)
     * @see PostgreSQL#condition(String, String, String)
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
     */
    Conditions or(String leftField, String operator, String rightField);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 OR "id" = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .or(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
     */
    Conditions or(Expression leftField, Operator operator, Expression rightField);

    /**
     * Subquery condition. It is used primarily by {@code IN} expression:
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 OR author_id IN (SELECT id FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .or(asName("author_id"), IN, select("id").from("author"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     *
     * @see org.queryman.builder.Operators#IN
     */
    Conditions or(Expression field, Operator operator, Query query);

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
     * @see PostgreSQL#condition(String, String, Query)
     * @see PostgreSQL#condition(String, String, String)
     */
    Conditions or(Conditions conditions);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 OR NOT "id" = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orNot(asQuotedName("id"), operator("="), asNumber(1))
     *  .sql()
     * </code>
     */
    Conditions orNot(Expression leftField, Operator operator, Expression rightField);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book WHERE year > 2010 OR NOT id = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orNot("id", "=", "1")
     *  .sql()
     * </code>
     */
    Conditions orNot(String leftField, String operator, String rightField);

    /**
     * Subquery condition. It is used primarily by {@code IN} expression:
     * Example:
     * <code>
     *
     * // SELECT * FROM book WHERE year > 2010 OR NOT author_id IN (SELECT id FROM author)
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .orNot(asName("author_id"), IN, select("id").from("authors"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     *
     * @see org.queryman.builder.Operators#IN
     */
    Conditions orNot(Expression field, Operator operator, Query query);

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
     * @see PostgreSQL#condition(String, String, Query)
     * @see PostgreSQL#condition(String, String, String)
     */
    Conditions orNot(Conditions conditions);


    Node getNode();
}
