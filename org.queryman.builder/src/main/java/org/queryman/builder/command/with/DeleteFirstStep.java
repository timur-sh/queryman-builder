/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.with;

import org.queryman.builder.command.delete.DeleteAsStep;
import org.queryman.builder.token.Expression;

/**
 * @author Timur Shaidullin
 */
public interface DeleteFirstStep {
    /**
     * DELETE statement.
     * <p>
     * <code>
     * // DELETE FROM book AS b USING author, order WHERE b.id = 1 RETURNING id
     * deleteFrom("book")
     * .as("b")
     * .using("author", "order")
     * .where("b.id", "=", "1")
     * .returning(asName("id")
     * .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    DeleteAsStep deleteFrom(String name);

    /**
     * DELETE statement.
     * <p>
     * <code>
     * // DELETE FROM book AS b USING author, order WHERE b.id = 1 RETURNING id
     * deleteFrom(asName("book"))
     * .as("b")
     * .using("author", "order")
     * .where("b.id", "=", "1")
     * .returning(asName("id")
     * .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    DeleteAsStep deleteFrom(Expression name);

    /**
     * DELETE statement.
     * <p>
     * <code>
     * // DELETE FROM ONLY book AS b USING author, order WHERE b.id = 1 RETURNING id
     * deleteFromOnly("book")
     * .as("b")
     * .using("author", "order")
     * .where("b.id", "=", "1")
     * .returning(asName("id")
     * .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    DeleteAsStep deleteFromOnly(String name);

    /**
     * DELETE statement.
     * <p>
     * <code>
     * // DELETE FROM ONLY book AS b USING author, order WHERE b.id = 1 RETURNING id
     * deleteFromOnly(asName("book"))
     * .as("b")
     * .using("author", "order")
     * .where("b.id", "=", "1")
     * .returning(asName("id")
     * .sql()
     * </code>
     *
     * @param name target table name
     * @return delete AS step
     */
    DeleteAsStep deleteFromOnly(Expression name);
}
