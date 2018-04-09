/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;

/**
 * For full information see the {@link Conditions} interface.
 *
 * @author Timur Shaidullin
 */
public interface InsertDoUpdateWhereManySteps extends InsertReturningStep {
    /**
     * For full information see the {@link Conditions#and(Object, Object, Object)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     *
     * @return itself
     */
    <T> InsertDoUpdateWhereManySteps and(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#and(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertDoUpdateWhereManySteps and(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertDoUpdateWhereManySteps andExists(Query query);


    /**
     * For full information see the {@link Conditions#andNot(Object, Object, Object)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     *
     * @return itself
     */
    <T> InsertDoUpdateWhereManySteps andNot(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#andNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertDoUpdateWhereManySteps andNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andNotExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertDoUpdateWhereManySteps andNotExists(Query query);


    /**
     * For full information see the {@link Conditions#or(Object, Object, Object)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     *
     * @return itself
     */
    <T> InsertDoUpdateWhereManySteps or(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#or(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertDoUpdateWhereManySteps or(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertDoUpdateWhereManySteps orExists(Query query);


    /**
     * For full information see the {@link Conditions#orNot(Object, Object, Object)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     *
     * @return itself
     */
    <T> InsertDoUpdateWhereManySteps orNot(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertDoUpdateWhereManySteps orNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertDoUpdateWhereManySteps orNotExists(Query query);
}