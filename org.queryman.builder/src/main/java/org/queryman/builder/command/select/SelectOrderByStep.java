/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

/**
 * @author Timur Shaidullin
 */
public interface SelectOrderByStep extends SelectLimitStep {
    SelectLimitStep orderBy(String column);

    SelectLimitStep orderBy(String column, String sorting);

    SelectLimitStep orderBy(String column, String sorting, String nulls);
}
