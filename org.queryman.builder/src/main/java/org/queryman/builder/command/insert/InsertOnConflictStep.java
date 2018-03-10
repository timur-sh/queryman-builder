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
    /**
     * @return on constraint step
     */
    InsertOnConstraintStep onConflict();

    /**
     * Specifies a conflict target
     * @param targets target
     * @return on conflict where step
     *
     * @see org.queryman.builder.PostgreSQL#targetColumn(String)
     * @see org.queryman.builder.PostgreSQL#targetExpression(String)
     */
    InsertOnConflictWhereFirstStep onConflict(ConflictTarget... targets);

    /**
     * Specifies index column name of conflict target. If you want to choose an
     * index expression, looks at {@link #onConflict(ConflictTarget...)}
     * @param indexColumnName index column name
     * @return on conflict where step
     *
     * @see #onConflict(ConflictTarget...)
     * @see org.queryman.builder.PostgreSQL#targetColumn(String)
     * @see org.queryman.builder.PostgreSQL#targetExpression(String)
     */
    InsertOnConflictWhereFirstStep onConflict(String indexColumnName);

}
