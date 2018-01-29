/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.where;

import org.queryman.builder.ast.AstVisitor;

import java.util.List;

/**
 * @author Timur Shaidullin
 */
public interface Conditions extends AstVisitor {
    Conditions and(String leftValue, String operator, String rightValue);

    Conditions or(String leftValue, String operator, String rightValue);

    //todo refactor this
    List<?> conditions();
}
