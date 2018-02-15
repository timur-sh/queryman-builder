/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.Query;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.token.Keyword;

/**
 * Type for UNION, INTERSECT, EXCEPT query combining.
 *
 * @author Timur Shaidullin
 */
public class CombiningQuery implements AstVisitor {
    private final Keyword type;
    private final Query query;

    public CombiningQuery(Keyword type, Query query) {
        this.type = type;
        this.query = query;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(new NodeMetadata(type));
        tree.peek(query);
        tree.endNode();
    }
}
