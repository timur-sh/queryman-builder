/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.update;

import org.queryman.builder.token.Expression;

/**
 * UPDATE .. RETURNING .. clause.
 *
 * @author Timur Shaidullin
 */
public interface UpdateReturningStep extends UpdateFinalStep {
    /**
     * Set list of expressions that to be computed after each row is updated.
     *
     * @param output list of output expressions
     * @return final step
     *
     * @see #returning(Expression...)
     */
    @SuppressWarnings("unchecked")
    <T> UpdateFinalStep returning(T... output);

    /**
     * Set list of expressions that to be computed after each row is updated.
     *
     * @param output list of output expressions
     * @return final step
     */
    UpdateFinalStep returning(Expression... output);
}
