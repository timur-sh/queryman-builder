/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command;

import org.queryman.builder.ast.AstVisitor;

/**
 * @author Timur Shaidullin
 */
public interface Conditions extends AstVisitor {
    Conditions and(String leftValue, String operator, String rightValue);

    Conditions andNot(String leftValue, String operator, String rightValue);

    Conditions or(String leftValue, String operator, String rightValue);

    Conditions orNot(String leftValue, String operator, String rightValue);
}
