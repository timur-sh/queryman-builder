/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.command.select.Select;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectInitialStep;

import static org.queryman.builder.ast.NodeMetadata.SELECT;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl extends AbstractQuery implements
   SelectInitialStep,
   SelectFinalStep,
   Select {

    private final String[] columnsSelect;

    public SelectImpl(
       AbstractSyntaxTree ast,
       String... columnsSelect
    ) {
        super(ast);
        this.columnsSelect = columnsSelect;
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
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(SELECT, ", ");

        for (String column : columnsSelect) {
            tree.addLeaf(column);
        }

        tree.endNode();

    }
}
