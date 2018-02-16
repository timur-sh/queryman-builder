/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.command.from.From;
import org.queryman.builder.token.Expression;

/**
 * This step is used to set a table name or table names.
 *
 * {@code FROM} clause is a part of {@code SELECT} statement.
 *
 * @author Timur Shaidullin
 */
public interface SelectFromStep extends SelectCombiningQueryStep {
    /**
     * Specify table name that be used to retrieve rows. If several names are
     * provided, they will be cross-joined together.
     *
     * It you'd like to use an alias, it must be followed by table name:
     * <code>select("id").from("books as b")</code>
     * <code>select("id").from("books as b (id, name)")</code>
     *
     * @see #from(Expression...)
     * @see #from(From...)
     */
    SelectJoinStep from(String... tables);

    /**
     * Specify table name that be used to retrieve rows. If several names are
     * provided, they will be cross-joined together.
     *
     * It you'd like to use an alias, it must be followed by table name:
     * <code>
     *     PostgreSQL.asName("books").as("b"); // books AS b
     *     PostgreSQL.asName("books").as("b", "id", "name"); // books AS b(id, name)
     * </code>
     *
     * @see #from(String...)
     * @see #from(From...)
     * @see Expression
     */
    SelectJoinStep from(Expression... tables);

    /**
     * Specify table name that be used to retrieve rows. If several names are
     * provided, they will be cross-joined together.
     *
     * It you'd like to use an alias, it must be followed by table name:
     * <code>
     *     PostgreSQL.from("books").as("b"); // books AS b
     *     PostgreSQL.from("books").as("b", "id", "name"); // books AS b(id, name)
     *     PostgreSQL.fromOnly("books"); // ONLY books
     * </code>
     *
     * @see #from(Expression...)
     * @see #from(String...)
     * @see org.queryman.builder.PostgreSQL#from(String)
     * @see org.queryman.builder.PostgreSQL#from(Expression)
     * @see org.queryman.builder.PostgreSQL#fromOnly(String)
     * @see org.queryman.builder.PostgreSQL#fromOnly(Expression)
     */
    SelectJoinStep from(From... tables);
}
