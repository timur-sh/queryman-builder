/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.command.where.Conditions;

/**
 * @author Timur Shaidullin
 */
public interface SelectWhereStep extends SelectGroupByStep {
    SelectWhereStep and(String left, String operator, String right);

    SelectWhereStep or(String left, String operator, String right);

    SelectWhereStep and(Conditions conditions);

    SelectWhereStep or(Conditions conditions);
}
