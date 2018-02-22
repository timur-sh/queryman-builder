/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Operator;

/**
 * For full information see the {@link Conditions} interface.
 *
 * @author Timur Shaidullin
 */
public interface SelectJoinOnStepsStep extends SelectJoinManyStepsStep {
    /**
     * For full information see the {@link Conditions#and(String, String, String)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep and(String left, String operator, String right);

    /**
     * For full information see the {@link Conditions#and(Expression, Operator, Expression)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep and(Expression left, Operator operator, Expression right);

    /**
     * For full information see the {@link Conditions#and(Expression, Operator, Query)} method.
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep and(Expression field, Operator operator, Query query);

    /**
     * For full information see the {@link Conditions#and(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectJoinOnStepsStep and(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep andExists(Query query);


    /**
     * For full information see the {@link Conditions#andNot(String, String, String)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep andNot(String left, String operator, String right);

    /**
     * For full information see the {@link Conditions#andNot(Expression, Operator, Expression)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep andNot(Expression left, Operator operator, Expression right);

    /**
     * For full information see the {@link Conditions#andNot(Expression, Operator, Query)} method.
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep andNot(Expression field, Operator operator, Query query);

    /**
     * For full information see the {@link Conditions#andNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectJoinOnStepsStep andNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#andNotExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep andNotExists(Query query);


    /**
     * For full information see the {@link Conditions#or(String, String, String)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep or(String left, String operator, String right);

    /**
     * For full information see the {@link Conditions#or(Expression, Operator, Expression)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep or(Expression left, Operator operator, Expression right);

    /**
     * For full information see the {@link Conditions#or(Expression, Operator, Query)} method.
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep or(Expression field, Operator operator, Query query);

    /**
     * For full information see the {@link Conditions#or(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectJoinOnStepsStep or(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orExists(Query)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep orExists(Query query);


    /**
     * For full information see the {@link Conditions#orNot(String, String, String)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep orNot(String left, String operator, String right);

    /**
     * For full information see the {@link Conditions#orNot(Expression, Operator, Expression)} method.
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @return itself
     */
    SelectJoinOnStepsStep orNot(Expression left, Operator operator, Expression right);

    /**
     * For full information see the {@link Conditions#orNot(Expression, Operator, Query)} method.
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep orNot(Expression field, Operator operator, Query query);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param conditions conditions
     * @return itself
     */
    SelectJoinOnStepsStep orNot(Conditions conditions);

    /**
     * For full information see the {@link Conditions#orNot(Conditions)} method.
     *
     * @param query subquery
     * @return itself
     */
    SelectJoinOnStepsStep orNotExists(Query query);
}
