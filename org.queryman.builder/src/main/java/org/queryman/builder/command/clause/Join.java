/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.clause;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.command.Conditions;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.asList;
import static org.queryman.builder.ast.NodesMetadata.*;

/**
 * This {@code class} represents an JOIN clause.
 *
 * @author Timur Shaidullin
 */
public class Join implements AstVisitor {
    private final NodeMetadata type;
    private final Expression   name;

    private boolean      boolConditions;
    private Conditions   conditions;
    private Expression[] using;

    public Join(Expression name, NodeMetadata type) {
        this.name = name;
        this.type = type;
    }

    public Join using(Expression... columns) {
        this.using = columns;
        return this;
    }

    public Conditions getConditions() {
        return conditions;
    }

    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
    }

    public void setConditions(boolean conditions) {
        this.boolConditions = conditions;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(type)
           .addLeaf(name);

        if (boolConditions)
            tree.startNode(ON)
               .addLeaf(asList(boolConditions))
               .endNode();
        else if (using != null)
            tree.startNode(USING, ", ")
               .addLeaves(asList(using))
               .endNode();
        else if (conditions != null) {
            tree.startNode(ON);
            tree.peek(conditions);
            tree.endNode();
        }

        tree.endNode();
    }
}
