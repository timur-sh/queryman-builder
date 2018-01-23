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
     * N
     */
    void lookup(AbstractSyntaxTreeSQL tree);
}
