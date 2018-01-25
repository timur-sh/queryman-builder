/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;

/**
 * @author Timur Shaidullin
 */
public interface SelectWhereStep extends Query {
    SelectFinalStep andWhere(String left, String operator, String right);

    SelectFinalStep orWhere(String left, String operator, String right);
}
