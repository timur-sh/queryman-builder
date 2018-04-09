/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.create_sequence;

/**
 * @author Timur Shaidullin
 */
public interface SequenceMinValueStep extends SequenceMaxValueStep {
    /**
     * @param minvalue min value of sequence.
     * @return the next step
     */
    SequenceMaxValueStep minvalue(long minvalue);

    /**
     * Set NO MINVALUE
     * @return the next step
     */
    SequenceMaxValueStep noMinvalue();
}
