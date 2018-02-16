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
 * The first step of having condition.
 *
 * @author Timur Shaidullin
 */
public interface SelectHavingFirstStep extends SelectCombiningQueryStep {
    /**
     * HAVING eliminates group rows that do not satisfy condition.
     */
    SelectHavingStep having(String left, String operator, String right);

    /**
     * HAVING eliminates group rows that do not satisfy condition.
     */
    SelectHavingStep having(Expression left, Operator operator, Expression right);

    /**
     * HAVING eliminates group rows that do not satisfy condition.
     */
    SelectHavingStep having(Expression field, Operator operator, Query query);

    /**
     * HAVING eliminates group rows that do not satisfy condition.
     */
    SelectHavingStep having(Conditions conditions);

    /**
     * HAVING eliminates group rows that do not satisfy condition.
     */
    SelectHavingStep havingExists(Query query);
}