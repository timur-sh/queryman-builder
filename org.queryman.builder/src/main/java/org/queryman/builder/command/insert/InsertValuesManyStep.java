/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.Query;

/**
 * @author Timur Shaidullin
 */
public interface InsertValuesManyStep extends InsertValuesStep {
    /**
     * Set query which output values are used to insert.
     *
     * @param query sub select
     * @return insert on conflict step
     */
    InsertOnConflictStep query(Query query);
}
