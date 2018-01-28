/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.command.where.WhereGroup;

/**
 * @author Timur Shaidullin
 */
public interface SelectWhereStep extends SelectFinalStep {
    SelectWhereStep andWhere(String left, String operator, String right);

    SelectWhereStep orWhere(String left, String operator, String right);

    SelectWhereStep andWhere(WhereGroup whereGroup);

    SelectWhereStep orWhere(WhereGroup whereGroup);
}
