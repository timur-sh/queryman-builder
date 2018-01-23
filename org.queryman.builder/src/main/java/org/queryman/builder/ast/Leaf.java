/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * This interface must be implemented each SQL part.
 *
 * @author Timur Shaidullin
 */
public interface Leaf {
    /**
     * One node is to be assembled this method.
     */
    void lookup(AbstractSyntaxTree tree);
}
