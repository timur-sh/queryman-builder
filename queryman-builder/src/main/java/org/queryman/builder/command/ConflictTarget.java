/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command;

import org.queryman.builder.Keywords;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.nodeMetadata;
import static org.queryman.builder.ast.NodesMetadata.EMPTY;
import static org.queryman.builder.ast.NodesMetadata.EMPTY_GROUPED;

/**
 * Conflict target belongs to
 * INSERT .. ON CONFLICT ( { index_column_name | ( index_expression ) } [ COLLATE collation ] [ opclass ] [, ...] )
 *
 * @author Timur Shaidullin
 */
public class ConflictTarget implements AstVisitor {
    private final Expression name;
    private       Expression collation;
    private       Expression opclass;

    private boolean indexExpression;
    private boolean indexColumn = true;

    public ConflictTarget(String name, String collation, String opclass) {
        this.name = asName(name);
        this.collation = collation != null ? asName(collation) : null;
        this.opclass = opclass != null ? asName(opclass) : null;
    }

    public ConflictTarget markAsExpression() {
        indexExpression = true;
        indexColumn = false;
        return this;
    }

    public ConflictTarget markAsColumn() {
        indexColumn = true;
        indexExpression = false;
        return this;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(EMPTY);

        if (indexExpression)
            tree.startNode(EMPTY_GROUPED)
               .addLeaf(name)
               .endNode();
        else {
            tree.addLeaf(name);
        }

        if (collation != null)
            tree.startNode(nodeMetadata(Keywords.COLLATE))
               .addLeaf(collation)
               .endNode();

        if (opclass != null)
            tree.startNode(EMPTY)
               .addLeaf(opclass)
               .endNode();

        tree.endNode();
    }
}
