/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.from;

import org.queryman.builder.ast.AstVisitor;

/**
 * Interface marker to denote the FROM statement.
 *
 * @author Timur Shaidullin
 */
public interface From extends AstVisitor {
}
