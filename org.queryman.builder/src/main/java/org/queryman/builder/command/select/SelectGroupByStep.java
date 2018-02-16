/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.token.Expression;

/**
 * @author Timur Shaidullin
 */
public interface SelectGroupByStep extends SelectHavingFirstStep {
    SelectHavingFirstStep groupBy(String... expressions);

    SelectHavingFirstStep groupBy(Expression... expressions);
}
