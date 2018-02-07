/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command;

import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.Node;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * It supplies a search condition.
 *
 * {@link Conditions} must be used only as a part of query: {@code SELECT},
 * {@code UPDATE} etc.
 *
 * Example:
 * <p><code>
 * PostgreSQL.conditions("id", "=", "1")
 *    .and("name", "=", "Alan")
 *    .and(condition("age", ">", "29")
 *       .and("gender", "!=", "male")
 *       .or("code", "=", "3")
 *    );
 *
 * PostgreSQL.conditions(asQuotedName("id"), operator("="), asNumber("1"))
 *    .and(asQualifiedName("users.name"), "=", asString("Alan"))
 *    .and(condition(asQualifiedName("users.age"), ">", "29")
 *       .and(asName("gender"), "!=", asString("male"))
 *       .or(asName("code"), "=", asNumber("3"))
 *    );
 * </code></p>
 *
 *
 * @see org.queryman.builder.PostgreSQL#asName(String)
 * @see org.queryman.builder.PostgreSQL#asConstant(String)
 * @see org.queryman.builder.PostgreSQL#asQualifiedName(String)
 * @see org.queryman.builder.PostgreSQL#asQuotedName(String)
 *
 * @author Timur Shaidullin
 */
public interface Conditions extends AstVisitor {
    /**
     * {@code AND} condition.
     *
     * Example:
     * <p>
     * conditions.and("id", "=", "1")
     * ...
     * </p>
     */
    Conditions and(String leftField, String operator, String rightField);

    /**
     * {@code AND} condition.
     *
     * Example:
     * <p>
     * conditions.and(asName("id"), "=", asNumber("1"))
     * ...
     * </p>
     */
    Conditions and(Expression leftField, String operator, Expression rightField);

    /**
     * {@code AND} condition.
     *
     * Example:
     * <p>
     * conditions.and(asName("id"), operator("="), asNumber("1"))
     * ...
     * </p>
     */
    Conditions and(Expression leftField, Operator operator, Expression rightField);

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

    /**
     * {@code AND NOT} condition.
     *
     * Example:
     * <p>
     * conditions.andNot(asName("id"), "=", asNumber("1"))
     * ...
     * </p>
     */
    Conditions andNot(Expression leftField, String operator, Expression rightField);

    /**
     * {@code AND NOT} condition.
     *
     * Example:
     * <p>
     * conditions.andNot(asName("id"), operator("="), asNumber("1"))
     * ...
     * </p>
     */
    Conditions andNot(Expression leftField, Operator operator, Expression rightField);

    /**
     * {@code AND NOT} condition.
     *
     * Example:
     * <p>
     * conditions.andNot("id", "=", "1")
     * ...
     * </p>
     */
    Conditions andNot(String leftField, String operator, String rightField);

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
     * {@code OR} condition.
     *
     * Example:
     * <p>
     * conditions.or("id", "=", "1")
     * ...
     * </p>
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
    Conditions or(Expression leftField, String operator, Expression rightField);

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
     * {@code OR NOT} condition.
     *
     * Example:
     * <p>
     * conditions.orNot(asName("id"), "=", asNumber("1"))
     * ...
     * </p>
     */
    Conditions orNot(Expression leftField, String operator, Expression rightField);

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
     * {@code OR NOT} condition.
     *
     * Example:
     * <p>
     * conditions.or("id", "=", "1")
     * ...
     * </p>
     */
    Conditions orNot(String leftField, String operator, String rightField);

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
