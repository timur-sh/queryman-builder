/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.insert;

import org.queryman.builder.command.ConflictTarget;

/**
 * INSERT INTO .. ON CONFLICT .. step.
 *
 * @author Timur Shaidullin
 */
public interface InsertOnConflictStep extends InsertConflictActionStep, InsertReturningStep {
    InsertOnConstraintStep onConflict();

    InsertOnConflictWhereFirstStep onConflict(ConflictTarget... targets);

}
