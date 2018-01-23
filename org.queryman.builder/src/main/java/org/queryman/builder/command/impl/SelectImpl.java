/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.Keywords;
import org.queryman.builder.command.select.Select;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectInitialStep;
import org.queryman.builder.token.Identifier;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl extends AbstractQuery implements
    SelectInitialStep,
    SelectFinalStep,
    Select {

    private final boolean distinct;
    private final boolean on;
    private final Identifier[] columnsSelect;
    private final Identifier[] columnsDistinct;

    public SelectImpl(
        AbstractSyntaxTree ast,
        boolean distinct,
        boolean on,
        Identifier[] columnsDistinct,
        Identifier... columnsSelect
    ) {
        super(ast);
        this.distinct = distinct;
        this.on = on;
        this.columnsSelect = columnsSelect;
        this.columnsDistinct = columnsDistinct;
    }

    @Override
    public Select select() {
        return null;
    }

    @Override
    public Select selectDistinct() {
        return null;
    }

    @Override
    public Select selectAll() {
        return null;
    }

    @Override
    public Select selectOn() {
        return null;
    }

    @Override
    public void buildTree(AbstractSyntaxTree tree) {
//        tree.addLeaf(Keywords.SELECT);
    }
}
