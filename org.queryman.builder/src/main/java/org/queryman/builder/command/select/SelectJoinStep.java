/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.token.Expression;

/**
 * Any JOIN clause start here.
 *
 * @author Timur Shaidullin
 */
public interface SelectJoinStep extends SelectWhereFirstStep {
    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     * @see #join(Expression)
     */
    SelectJoinOnStep join(String name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     */
    SelectJoinOnStep join(Expression name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     * @see #innerJoin(Expression)
     */
    SelectJoinOnStep innerJoin(String name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     */
    SelectJoinOnStep innerJoin(Expression name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     * @see #leftJoin(Expression)
     */
    SelectJoinOnStep leftJoin(String name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     */
    SelectJoinOnStep leftJoin(Expression name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     * @see #rightJoin(Expression)
     */
    SelectJoinOnStep rightJoin(String name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     */
    SelectJoinOnStep rightJoin(Expression name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     * @see #fullJoin(Expression)
     */
    SelectJoinOnStep fullJoin(String name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     */
    SelectJoinOnStep fullJoin(Expression name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     * @see #crossJoin(Expression)
     */
    SelectJoinManyStepsStep crossJoin(String name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     */
    SelectJoinManyStepsStep crossJoin(Expression name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     * @see #naturalJoin(Expression)
     */
    SelectJoinManyStepsStep naturalJoin(String name);

    /**
     * @param name is a table name. actually it can be {@code ROW},
     *             or {@code VALUES} expression
     */
    SelectJoinManyStepsStep naturalJoin(Expression name);

}
