/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.update;

import org.queryman.builder.Query;
import org.queryman.builder.ast.AstVisitor;

/**
 * @author Timur Shaidullin
 */
public interface UpdateFinalStep extends Query, AstVisitor {
}
