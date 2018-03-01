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
 * @author Timur Shaidullin
 */
public interface InsertValuesManyStep extends InsertValuesStep {
    InsertOnConflictStep values(Query query);
}
