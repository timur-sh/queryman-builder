/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;

/**
 * For full information see the {@link Conditions} interface.
 *
 * @author Timur Shaidullin
 */
public interface SelectHavingManySteps extends SelectCombiningQueryStep {

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
    <T> SelectHavingManySteps and(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#and(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectHavingManySteps and(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectHavingManySteps andExists(Query query);


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
    <T> SelectHavingManySteps andNot(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#andNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectHavingManySteps andNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andNotExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectHavingManySteps andNotExists(Query query);


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
    <T> SelectHavingManySteps or(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#or(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectHavingManySteps or(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectHavingManySteps orExists(Query query);


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
    <T> SelectHavingManySteps orNot(T left, T operator, T right);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectHavingManySteps orNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectHavingManySteps orNotExists(Query query);

}
