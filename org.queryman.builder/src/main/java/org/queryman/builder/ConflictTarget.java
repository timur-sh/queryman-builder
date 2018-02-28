/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.token.Expression;

import static org.queryman.builder.PostgreSQL.asConstant;

/**
 * @author Timur Shaidullin
 */
public class ConflictTarget implements AstVisitor {
    private final Expression NAME;
    private Expression collation;
    private Expression opclass;

    public ConflictTarget(String name, String collation, String opclass) {
        this.NAME = asConstant(name);
        this.collation = asConstant(collation);
        this.opclass = asConstant(opclass);
    }

    public ConflictTarget markAsExpression() {
        return this;
    }

    public ConflictTarget markAsColumn() {
        return this;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {

    }
}
