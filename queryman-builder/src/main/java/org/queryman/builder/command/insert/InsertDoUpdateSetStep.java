/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.Query;
import org.queryman.builder.token.Expression;

/**
 * INSERT INTO .. DO UPDATE SET .. step.
 *
 * @author Timur Shaidullin
 */
public interface InsertDoUpdateSetStep extends InsertDoUpdateWhereFirstStep {

    /**
     * Set a column name and an expression that is be assigned to the column.
     *
     * @param column column name
     * @param value expression value
     * @return itself
     */
    <T> InsertDoUpdateSetStep set(String column, T value);

    /**
     * Set a columns names and an expression list that are be assigned to the columns.
     *
     * @param listColumns columns names
     * @param listValues expressions list
     * @return itself
     */
    InsertDoUpdateSetStep set(Expression listColumns, Expression listValues);

    /**
     * Set a columns names and a sub-select that is be assigned to the columns.
     *
     * @param listColumns columns names
     * @param subSelect sub-select
     * @return itself
     */
    InsertDoUpdateSetStep set(Expression listColumns, Query subSelect);
}
