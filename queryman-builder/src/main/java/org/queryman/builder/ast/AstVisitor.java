/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * If any complex type represent itself a part of query or it must implements
 * this interface.
 *
 * @author Timur Shaidullin
 */
public interface AstVisitor {
    /**
     * Visits SQL query making a tree from its clauses.
     *
     * @param tree - abstract syntax tree
     */
    void assemble(AbstractSyntaxTree tree);
}
