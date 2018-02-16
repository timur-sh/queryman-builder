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
 * The having conditions are followed by {@link SelectHavingFirstStep}.
 *
 * @author Timur Shaidullin
 */
public interface SelectHavingStep extends SelectCombiningQueryStep {
    SelectHavingStep and(String left, String operator, String right);

    SelectHavingStep and(Expression left, Operator operator, Expression right);

    SelectHavingStep and(Expression field, Operator operator, Query query);

    SelectHavingStep and(Conditions conditions);

    SelectHavingStep andExists(Query query);


    SelectHavingStep andNot(String left, String operator, String right);

    SelectHavingStep andNot(Expression left, Operator operator, Expression right);

    SelectHavingStep andNot(Expression field, Operator operator, Query query);

    SelectHavingStep andNot(Conditions conditions);

    SelectHavingStep andNotExists(Query query);


    SelectHavingStep or(String left, String operator, String right);

    SelectHavingStep or(Expression left, Operator operator, Expression right);

    SelectHavingStep or(Expression field, Operator operator, Query query);

    SelectHavingStep or(Conditions conditions);

    SelectHavingStep orExists(Query query);


    SelectHavingStep orNot(String left, String operator, String right);

    SelectHavingStep orNot(Expression left, Operator operator, Expression right);

    SelectHavingStep orNot(Expression field, Operator operator, Query query);

    SelectHavingStep orNot(Conditions conditions);

    SelectHavingStep orNotExists(Query query);
}
