/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.NodesMetadata;

/**
 * @author Timur Shaidullin
 */
final class OrderBy implements AstVisitor {
    private final String name;
    private final String sorting;
    private final String nulls;

    OrderBy(String name) {
        this(name, null, null);
    }

    OrderBy(String name, String sorting) {
        this(name, sorting, null);
    }

    OrderBy(String name, String sorting, String nulls) {
        this.name = name;
        this.sorting = sorting;
        this.nulls = nulls;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
//        tree.startNode(NodesMetadata.EMPTY)
//           .addLeaf(name);
//
//        if (sorting != null)
//            tree.addLeaf(sorting);
//
//        if (nulls != null)
//            tree.addLeaf(nulls);
//
//        tree.endNode();
    }
}
