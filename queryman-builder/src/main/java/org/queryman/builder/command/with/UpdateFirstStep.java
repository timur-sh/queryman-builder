/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.with;

import org.queryman.builder.command.update.UpdateAsStep;
import org.queryman.builder.token.Expression;

/**
 * @author Timur Shaidullin
 */
public interface UpdateFirstStep {

    /**
     * UPDATE statement.
     *
     * <code>
     *     // UPDATE book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     update("book")
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
     *
     * @param name name of the table ot update
     * @return update as step
     */
    UpdateAsStep update(String name);

    /**
     * UPDATE statement.
     *
     * <code>
     *     // UPDATE book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     update(asName("book"))
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
     *
     * @param name name of the table ot update
     * @return update as step
     */
    UpdateAsStep update(Expression name);

    /**
     * UPDATE statement.
     *
     * <code>
     *     // UPDATE ONLY book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     updateOnly("book")
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
     *
     * @param name name of the table ot update
     * @return update as step
     */
    UpdateAsStep updateOnly(String name);

    /**
     * UPDATE statement.
     *
     * <code>
     *     // UPDATE ONLY book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price
     *     updateOnly(asName("book"))
     *      .as("b")
     *      .set("author", asConstant("Andrew"))
     *      .where("b.id", "=", 1)
     *      .returning(asName("max(price)").as("price"))
     *      .sql();
     * </code>
     *
     * @param name name of the table ot update
     * @return update as step
     */
    UpdateAsStep updateOnly(Expression name);
}
