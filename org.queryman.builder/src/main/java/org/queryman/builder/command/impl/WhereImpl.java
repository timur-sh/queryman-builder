/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.ast.ASTBuilder;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.command.where.WhereFinalStep;
import org.queryman.builder.command.where.WhereFirstStep;

import java.util.LinkedList;
import java.util.List;

/**
 * It represent {@code WHERE} statement.
 *
 * @author Timur Shaidullin
 */
public class WhereImpl implements
   ASTBuilder,
   WhereFirstStep,
   WhereFinalStep {
    private final String AND = "AND";
    private final String OR = "OR";

    private final List<Where> wheres = new LinkedList<>();

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        for (Where where : wheres) {
            if (where.token == null) {
                tree.addLeaves(where.leftValue, where.operator, where.rightValue);
            } else {
                tree.startNode(where.token)
                   .addLeaves(where.leftValue, where.operator, where.rightValue)
                   .endNode();
            }
        }
    }

    public boolean isEmpty() {
        return wheres.isEmpty();
    }

    @Override
    public WhereImpl where(String leftValue, String operator, String rightValue) {
        wheres.add(new Where(leftValue, operator, rightValue));
        return this;
    }

    @Override
    public WhereImpl andWhere(String leftValue, String operator, String rightValue) {
        wheres.add(new Where(AND, leftValue, operator, rightValue));
        return this;
    }

    @Override
    public WhereImpl orWhere(String leftValue, String operator, String rightValue) {
        wheres.add(new Where(OR, leftValue, operator, rightValue));
        return this;
    }

    private class Where {
        private String token;
        private String leftValue;
        private String rightValue;
        private String operator;

        private Where(String leftValue, String operator, String rightValue) {
            this(null, leftValue, operator, rightValue);
        }

        private Where(String token, String leftValue, String operator, String rightValue) {
            this.token = token;
            this.leftValue = leftValue;
            this.rightValue = rightValue;
            this.operator = operator;
        }


    }
}
