/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.select;

import org.queryman.builder.Query;
import org.queryman.builder.Select;

/**
 * {@code FROM} clause is a part of {@code SELECT} statement.
 *
 * Usual rows are derived from {@code table}, but it is possible to retrieve
 * rows from subselect or other virtual table, created {@code VIEW}, etc..
 *
 * @author Timur Shaidullin
 */
public interface SelectFromStep extends Query, Select {
    /**
     * Specify table name that be used to retrieve rows
     */
    SelectFinalStep from(String table);

    /**
     * Specify several tables. They are cross joined together.
     */
    SelectFinalStep from(String... table);
}
