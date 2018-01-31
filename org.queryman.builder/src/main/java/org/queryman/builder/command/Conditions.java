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
 * @author Timur Shaidullin
 */
public interface Conditions extends AstVisitor {
    /**
     * Example:
     * <p>
     * conditions.and("id", "=", "1")
     * ...
     * </p>
     */
    Conditions and(String leftValue, String operator, String rightValue);

    /**
     * Example:
     * <p>
     * conditions.and("id", "=", "1")
     *  .and(condition("id", "=", "2")
     *      .and("id", "=", "3")
     *      .or("id", "=", "3")
     *  )
     * ...
     * </p>
     */
    Conditions and(Conditions conditions);

    /**
     * Example:
     * <p>
     * conditions.andNot("id", "=", "1")
     * ...
     * </p>
     */
    Conditions andNot(String leftValue, String operator, String rightValue);

    Conditions andNot(Conditions conditions);

    /**
     * Example:
     * <p>
     * conditions.or("id", "=", "1")
     * ...
     * </p>
     */
    Conditions or(String leftValue, String operator, String rightValue);

    Conditions or(Conditions conditions);

    /**
     * Example:
     * <p>
     * conditions.orNot("id", "=", "1")
     * ...
     * </p>
     */
    Conditions orNot(String leftValue, String operator, String rightValue);

    Conditions orNot(Conditions conditions);
}
