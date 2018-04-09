/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

/**
 * INSERT INTO .. DEFAULT VALUES.. step.
 *
 * @author Timur Shaidullin
 */
public interface InsertDefaultValuesStep extends InsertOnConflictStep {
    InsertOnConflictStep defaultValues();
}
