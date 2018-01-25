/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.AbstractQuery;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.Select;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectFromStep;

import java.util.LinkedList;
import java.util.List;

import static org.queryman.builder.ast.NodeMetadata.FROM;
import static org.queryman.builder.ast.NodeMetadata.SELECT;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl extends AbstractQuery implements
   SelectFromStep,
   SelectFinalStep,
   Select {

    private final String[] columnsSelected;
    private final List<String> from = new LinkedList<>();


    public SelectImpl(
       AbstractSyntaxTree ast,
       String... columnsSelected
    ) {
        super(ast);
        this.columnsSelected = columnsSelected;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(SELECT, ", ")
           .addLeaves(columnsSelected);

        if (!from.isEmpty()) {
            tree.startNode(FROM, ", ")
               .addLeaves(from)
               .endNode();
        }

        tree.endNode();
    }

    //--
    // FROM API
    //--
    @Override
    public SelectImpl from(String table) {
        from.add(table);
        return this;
    }

    @Override
    public SelectImpl from(String... tables) {
        from.clear();
        from.addAll(List.of(tables));
        return this;
    }
}
