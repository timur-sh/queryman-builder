/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.sequence;

/**
 * The first step of SEQUENCE statement.
 *
 * @author Timur Shaidullin
 */
public interface SequenceFirstStep extends SequenceIncrementStep {
    /**
     * Specifies a data type of sequence. Valid types are smallint, integer,
     * and bigint.
     *
     * Note. This is an optional step.
     *
     * @param dataType one of valid types : smallint, integer, bigint.
     * @return the next step
     */
    SequenceIncrementStep as(String dataType);
}
