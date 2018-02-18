/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.AstVisitor;

/**
 * Provides a methods for working with sql query.
 *
 * @author Timur Shaidullin
 */
public interface Query extends AstVisitor {
    /**
     * Assembles tree from query parts then return its string representation.
     *
     * @return SQL as string
     */
    String sql();
}
