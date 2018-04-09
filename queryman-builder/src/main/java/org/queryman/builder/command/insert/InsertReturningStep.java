/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.token.Expression;

/**
 * INSERT INTO .. RETURNING clause.
 *
 * @author Timur Shaidullin
 */
public interface InsertReturningStep extends InsertFinalStep {
    /**
     * Set list of expressions that to be computed after each row is inserted.
     *
     * @param output list of output expressions
     * @return final step
     *
     * @see #returning(Expression...)
     */
    @SuppressWarnings("unchecked")
    <T> InsertFinalStep returning(T... output);

    /**
     * Set list of expressions that to be computed after each row is inserted.
     *
     * @param output list of output expressions
     * @return final step
     */
    InsertFinalStep returning(Expression... output);
}
