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

/**
 * Compulsory step for all JOIN clauses. Exclusion is CROSS JOIN
 *
 * @see SelectJoinStep
 * @author Timur Shaidullin
 */
public interface SelectJoinOnFirstStep extends SelectWhereFirstStep {

    /**
     * This form of JOIN ON condition is equivalent to CROSS JOIN.
     *
     * Example:
     * <code>
     * // SELECT * FROM book AS b JOIN author a ON (true)
     * select("*")
     *  .from(asName("book").as("b"))
     *  .join("author a")
     *  .on(true)
     *  .sql()
     * </code>
     */
    SelectJoinManyStepsStep on(boolean all);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book AS b JOIN author a ON a.id = b.author_id
     * select("*")
     *  .from(asName("book").as("b"))
     *  .join("author a")
     *  .on("a.id", "=", "b.author_id")
     *  .sql()
     * </code>
     *
     * @param left left operand
     * @param operator operator
     * @param right right operand
     * @param <T> String, Expression, Operator or Query object
     *
     */
    <T> SelectJoinOnManySteps on(T left, T operator, T right);

    SelectJoinOnManySteps on(Conditions conditions);

    SelectJoinOnManySteps onExists(Query query);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book JOIN author USING (id)
     * select("*")
     *  .from("book")
     *  .join("author")
     *  .using("id")
     *  .sql()
     * </code>
     */
    SelectJoinManyStepsStep using(String... columns);

    /**
     * Example:
     * <code>
     * // SELECT * FROM book JOIN author USING (id)
     * select("*")
     *  .from(asName("book"))
     *  .join("author")
     *  .using(asName("id"))
     *  .sql()
     * </code>
     */
    SelectJoinManyStepsStep using(Expression... columns);

}
