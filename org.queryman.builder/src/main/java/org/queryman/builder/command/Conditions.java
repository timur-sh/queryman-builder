/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command;

import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.token.Field;
import org.queryman.builder.token.Name;
import org.queryman.builder.token.Operator;
import org.queryman.builder.token.Token;

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
 *       .or("code", "=", "3")
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
    Conditions and(String leftField, String operator, String rightField);
//
//    /**
//     * {@code AND} condition.
//     *
//     * Example:
//     * <p>
//     * conditions.and("id", "=", "1")
//     * ...
//     * </p>
//     *
//     * @see org.queryman.builder.PostgreSQL#unqualifiedName(String)
//     * @see org.queryman.builder.PostgreSQL#qualifiedName(String)
//     *
//     * @see org.queryman.builder.PostgreSQL (constants)
//     */
//    Conditions and(Field leftField, Operator operator, Field rightField);

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
}
