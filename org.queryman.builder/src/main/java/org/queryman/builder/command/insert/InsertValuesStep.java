/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.token.Expression;

/**
 * INSERT INTO .. VALUES .. step.
 *
 * @author Timur Shaidullin
 */
public interface InsertValuesStep extends InsertOnConflictStep {
    @SuppressWarnings("unchecked")
    <T> InsertValuesStep values(T... values);

    InsertValuesStep values(Expression... values);
}
