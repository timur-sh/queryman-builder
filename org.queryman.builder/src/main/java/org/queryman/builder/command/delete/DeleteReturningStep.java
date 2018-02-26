/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.delete;

import org.queryman.builder.token.Expression;

/**
 * DELETE .. RETURNING clause.
 *
 * @author Timur Shaidullin
 */
public interface DeleteReturningStep extends DeleteFinalStep {
    /**
     * Set list of expressions that to be computed after each row is deleted.
     *
     * @param output list of output expressions
     * @return final step
     *
     * @see #returning(Expression...)
     */
    DeleteFinalStep returning(String... output);

    /**
     * Set list of expressions that to be computed after each row is deleted.
     *
     * @param output list of output expressions
     * @return final step
     */
    DeleteFinalStep returning(Expression... output);
}
