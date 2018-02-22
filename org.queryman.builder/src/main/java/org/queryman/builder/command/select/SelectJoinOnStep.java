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
 * Compulsory step for all JOIN clauses. Exclusion is CROSS JOIN
 *
 * @see SelectJoinStep
 * @author Timur Shaidullin
 */
public interface SelectJoinOnStep extends SelectWhereFirstStep {
    SelectJoinManyStepsStep on(boolean all);

    SelectJoinOnStepsStep on(String left, String operator, String right);

    SelectJoinOnStepsStep on(Expression left, Operator operator, Expression right);

    /**
     * Subquery condition.
     * Example:
     * <code>
     *
     * // SELECT price FROM book JOIN order ON total < (SELECT MAX(price) FROM book)
     *
     * select("price")
     *  .from("book")
     *  .join("order")
     *  .on(asName("total"), operator("<"), select(max("price")).from("book"))
     *  .sql()
     * </code>
     *
     * @param field field
     * @param operator operator
     * @param query subquery
     * @return itself
     *
     * @see org.queryman.builder.Operators#GTE
     * @see org.queryman.builder.PostgreSQL#max(String)
     * @see org.queryman.builder.PostgreSQL#asName(String)
     * @see org.queryman.builder.PostgreSQL#operator(String)
     */
    SelectJoinOnStepsStep on(Expression field, Operator operator, Query query);

    SelectJoinOnStepsStep on(Conditions conditions);

    SelectJoinOnStepsStep onExists(Query query);

    SelectJoinManyStepsStep using(String... columns);

    SelectJoinManyStepsStep using(Expression... columns);



}
