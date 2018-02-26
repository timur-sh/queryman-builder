/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.create.sequence;

import org.queryman.builder.token.Expression;

/**
 * @author Timur Shaidullin
 */
public interface SequenceOwnerStep extends SequenceFinalStep {
    /**
     * Specifies a <i>table_name.column_name</i> associates to the sequence.
     * @param name column name
     * @return the next step
     * @see #ownedBy(Expression)
     */
    SequenceFinalStep ownedBy(String name);

    /**
     * Specifies a <i>table_name.column_name</i> associates to the sequence.
     * @param name column name
     * @return the next step
     */
    SequenceFinalStep ownedBy(Expression name);

    /**
     * Specifies that there is no such association.
     * @return the next step
     */
    SequenceFinalStep ownedByNone();
}
