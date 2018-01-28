/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.where;

import org.queryman.builder.ast.ASTBuilder;

/**
 * @author Timur Shaidullin
 */
public interface WhereGroup extends ASTBuilder {
    WhereGroup andWhere(String leftValue, String operator, String rightValue);

    WhereGroup orWhere(String leftValue, String operator, String rightValue);
}
