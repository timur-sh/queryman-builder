/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.ast.AbstractSyntaxTreeSQL;

/**
 * @author Timur Shaidullin
 */
public interface ASTBuilder {
    void buildTree(AbstractSyntaxTreeSQL tree);
}
