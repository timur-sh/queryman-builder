/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.command.Conditions;

/**
 * @author Timur Shaidullin
 */
public interface SelectWhereStep extends SelectWhereManySteps {
    SelectWhereStep and(String left, String operator, String right);

    SelectWhereStep andNot(String left, String operator, String right);

    SelectWhereStep or(String left, String operator, String right);

    SelectWhereStep orNot(String left, String operator, String right);

    SelectWhereStep and(Conditions conditions);

    SelectWhereStep andNot(Conditions conditions);

    SelectWhereStep or(Conditions conditions);

    SelectWhereStep orNot(Conditions conditions);
}
