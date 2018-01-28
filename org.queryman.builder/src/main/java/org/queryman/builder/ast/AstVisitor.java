/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * Each parts of query must assemble {@code tree} by yourself.
 * See {@link AbstractSyntaxTreeImpl}
 *
 * @author Timur Shaidullin
 */
public interface AstVisitor {
    /**
     * @param tree - abstract syntax tree
     */
    void assemble(AbstractSyntaxTree tree);
}
