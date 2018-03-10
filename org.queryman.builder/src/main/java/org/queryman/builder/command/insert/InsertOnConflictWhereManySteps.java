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
public interface InsertOnConflictWhereManySteps extends InsertConflictActionStep {
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
    <T> InsertOnConflictWhereManySteps and(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#and(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertOnConflictWhereManySteps and(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertOnConflictWhereManySteps andExists(Query query);


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
    <T> InsertOnConflictWhereManySteps andNot(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#andNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertOnConflictWhereManySteps andNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andNotExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertOnConflictWhereManySteps andNotExists(Query query);


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
    <T> InsertOnConflictWhereManySteps or(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#or(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertOnConflictWhereManySteps or(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertOnConflictWhereManySteps orExists(Query query);


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
    <T> InsertOnConflictWhereManySteps orNot(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    InsertOnConflictWhereManySteps orNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param query subquery
     * @return itself
     */
    InsertOnConflictWhereManySteps orNotExists(Query query);
}