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
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

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
     * // SELECT * FROM book WHERE year > 2010 AND id = 1
     * select("*")
     *  .from("book")
     *  .where("year", ">", "2010")
     *  .and(asName("id"), operator("="), asNumber(1))
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


    Conditions andExists(Query query);

    /**
     * {@code AND} condition. This is useful to group expressions.
     *
     * Example:
     * <p>
     * conditions.and(condition("id", "=", "1")
     *      .and("id2", "=", "3")
     *      .or("id3", "=", "2")
     *  )
     * ...
     * </p>
     */
    Conditions and(Conditions conditions);

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

    Conditions andNotExists(Query query);

    /**
     * {@code AND NOT} condition. This is useful to group expressions.
     *
     * Example:
     * <p>
     * conditions.andNot(condition("id", "=", "1")
     *      .and("id2", "=", "2")
     *      .or("id3", "=", "3")
     *  )
     * ...
     * </p>
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
     * {@code OR} condition.
     *
     * Example:
     * <p>
     * conditions.or(asName("id"), operator("="), asNumber("1"))
     * ...
     * </p>
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

    Conditions orExists(Query query);

    /**
     * {@code OR} condition. This is useful to group expressions.
     *
     * Example:
     * <p>
     * conditions.or(condition("id", "=", "2")
     *      .and("id2", "=", "2")
     *      .or("id3", "=", "3")
     *  )
     * ...
     * </p>
     */
    Conditions or(Conditions conditions);

    /**
     * {@code OR} condition.
     *
     * Example:
     * <p>
     * conditions.orNot(asName("id"), operator("="), asNumber("1"))
     * ...
     * </p>
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

    Conditions orNotExists(Query query);

    /**
     * {@code OR NOT} condition. This is useful to group expressions.
     *
     * Example:
     * <p>
     * conditions.orNot(condition("id", "=", "1")
     *      .and("id2", "=", "2")
     *      .or("id3", "=", "3")
     *  )
     * ...
     * </p>
     */
    Conditions orNot(Conditions conditions);


    Node getNode();
}
