/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.with;

import org.queryman.builder.command.select.SelectFromStep;
import org.queryman.builder.token.Expression;

/**
 * @author Timur Shaidullin
 */
public interface SelectFirstStep {
    /**
     * SELECT statement.
     * Example:
     * <code>
     *     select("id", "name", 3); // SELECT id, name, 3
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    <T> SelectFromStep select(T... columns);

    /**
     * SELECT statement.
     * Example:
     * <code>
     *     select(asName("id"), asName("name")); // SELECT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    SelectFromStep select(Expression... columns);

    /**
     * SELECT ALL statement.
     * Example:
     * <code>
     *     selectAll("id", "name"); // SELECT ALL id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    <T> SelectFromStep selectAll(T... columns);

    /**
     * SELECT ALL statement.
     * Example:
     * <code>
     *     select(asName("id"), asName("name")); // SELECT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    SelectFromStep selectAll(Expression... columns);

    /**
     * SELECT DISTINCT statement.
     * Example:
     * <code>
     *     selectDistinct("id", "name"); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    <T> SelectFromStep selectDistinct(T... columns);

    /**
     * SELECT DISTINCT statement.
     * Example:
     * <code>
     *     selectDistinct(asName("id"), asName("name")); // SELECT DISTINCT id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    SelectFromStep selectDistinct(Expression... columns);

    /**
     * SELECT DISTINCT ON (..) .. statement.
     * Example:
     * <code>
     *     String[] distinct = {"id"};
     *     selectDistinctOn(distinct, "id", "name"); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    <T> SelectFromStep selectDistinctOn(String[] distinct, T... columns);

    /**
     * SELECT DISTINCT ON (..) .. statement.
     * Example:
     * <code>
     *     Expression[] distinct = {"id"};
     *     selectDistinctOn(distinct, asName("id"), asName("name")); // SELECT DISTINCT ON (id) id, name
     * </code>
     *
     * @param columns output columns
     *
     * @return select from step
     */
    SelectFromStep selectDistinctOn(Expression[] distinct, Expression... columns) ;
}
