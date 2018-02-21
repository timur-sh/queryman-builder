/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * @author Timur Shaidullin
 */
public interface ExpressionCastStep {
    /**
     * The type follows by <code>::</code> operator after expression
     * @param type cast type
     * @return itself
     */
    ExpressionAsStep cast(String type);
}
