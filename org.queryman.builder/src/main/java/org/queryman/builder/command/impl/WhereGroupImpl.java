/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.command.where.WhereGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * It represents {@code WHERE} statement.
 *
 * @author Timur Shaidullin
 */
public final class WhereGroupImpl implements
   WhereGroup {

    private final List<Where> WHERE = new LinkedList<>();

    public WhereGroupImpl(String leftValue, String operator, String rightValue) {
        WHERE.add(CommandUtils.where(leftValue, operator, rightValue));
    }

    @Override
    public WhereGroupImpl andWhere(String leftValue, String operator, String rightValue) {
        WHERE.add(CommandUtils.andWhere(leftValue, operator, rightValue));
        return this;
    }

    @Override
    public WhereGroupImpl orWhere(String leftValue, String operator, String rightValue) {
        WHERE.add(CommandUtils.orWhere(leftValue, operator, rightValue));
        return this;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(NodesMetadata.WHERE_GROUPED);

        for (Where where : WHERE) {
            if (where.getToken() == null) {
                tree.addLeaves(where.getLeftValue(), where.getOperator(), where.getRightValue());

            } else {
                tree.startNode(where.getToken())
                   .addLeaves(where.getLeftValue(), where.getOperator(), where.getRightValue())
                   .endNode();
            }
        }
        tree.endNode();
    }
}
