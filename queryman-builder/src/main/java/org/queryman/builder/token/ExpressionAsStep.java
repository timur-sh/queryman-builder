/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * Expression AS step.
 *
 * @author Timur Shaidullin
 */
public interface ExpressionAsStep {
    /**
     * @param alias expression alias
     * @return itself
     */
    ExpressionAsStep as(String alias);

    /**
     * @param alias expression alias
     * @param columns aliases of output columns
     * @return itself
     */
    ExpressionAsStep as(String alias, String... columns);
}
