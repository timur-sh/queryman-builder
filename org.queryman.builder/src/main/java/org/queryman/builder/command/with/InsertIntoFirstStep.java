/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.with;

import org.queryman.builder.command.insert.InsertAsStep;
import org.queryman.builder.token.Expression;

/**
 * @author Timur Shaidullin
 */
public interface InsertIntoFirstStep {

    /**
     * INSERT INTO statement.
     * <code>
     *     insertInto("book")
     *      .as("b")
     *      .columns("id", "name")
     *      .overridingSystemValue()
     *      .values(1, "test")
     *      .onConflict()
     *      .onConstraint("index_name")
     *      .doNothing()
     *      .sql()
     * </code>
     *
     * @param table target table name
     * @return insert AS step
     */
    InsertAsStep insertInto(String table);

    /**
     * INSERT INTO statement.
     * <code>
     *     insertInto(asName("book"))
     *      .as("b")
     *      .columns("id", "name")
     *      .overridingSystemValue()
     *      .values(1, "test")
     *      .onConflict()
     *      .onConstraint("index_name")
     *      .doNothing()
     *      .sql()
     * </code>
     *
     * @param table target table name
     * @return insert AS step
     */
    InsertAsStep insertInto(Expression table);
}
