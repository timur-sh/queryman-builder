/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command;

import org.queryman.builder.ast.AstVisitor;

/**
 * A search condition.
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
 *       .or("", "=", "3")
 *    )
 * </code></p>
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
    Conditions and(String leftValue, String operator, String rightValue);

    /**
     * {@code AND} condition.
     *
     * Example:
     * <p>
     * conditions.and(condition("id", "=", "2")
     *      .and("id", "=", "3")
     *      .or("id", "=", "3")
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
     * conditions.andNot("id", "=", "1")
     * ...
     * </p>
     */
    Conditions andNot(String leftValue, String operator, String rightValue);

    /**
     * {@code AND NOT} condition.
     *
     * Example:
     * <p>
     * conditions.andNot(condition("id", "=", "2")
     *      .and("id", "=", "3")
     *      .or("id", "=", "3")
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
    Conditions or(String leftValue, String operator, String rightValue);

    /**
     * {@code OR} condition.
     *
     * Example:
     * <p>
     * conditions.or(condition("id", "=", "2")
     *      .and("id", "=", "3")
     *      .or("id", "=", "3")
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
     * conditions.or("id", "=", "1")
     * ...
     * </p>
     */
    Conditions orNot(String leftValue, String operator, String rightValue);

    /**
     * {@code OR NOT} condition.
     *
     * Example:
     * <p>
     * conditions.orNot(condition("id", "=", "2")
     *      .and("id", "=", "3")
     *      .or("id", "=", "3")
     *  )
     * ...
     * </p>
     */
    Conditions orNot(Conditions conditions);
}
