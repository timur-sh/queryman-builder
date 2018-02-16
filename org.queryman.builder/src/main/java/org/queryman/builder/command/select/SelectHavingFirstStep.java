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
 * @author Timur Shaidullin
 */
public interface SelectHavingFirstStep extends SelectCombiningQueryStep {
    SelectHavingStep having(String left, String operator, String right);

    SelectHavingStep having(Expression left, Operator operator, Expression right);

    SelectHavingStep having(Expression field, Operator operator, Query query);

    SelectHavingStep having(Conditions conditions);

    SelectHavingStep havingExists(Query query);

    SelectHavingStep havingBetween(String field, String value1, String value2);

    SelectHavingStep havingBetween(Expression field, Expression value1, Expression value2);
}