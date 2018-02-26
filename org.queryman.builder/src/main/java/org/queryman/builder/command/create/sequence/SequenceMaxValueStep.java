/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.create.sequence;

/**
 * @author Timur Shaidullin
 */
public interface SequenceMaxValueStep extends SequenceStartStep {
    /**
     * @param maxvalue max value of sequence.
     * @return the next step
     */
    SequenceStartStep maxvalue(long maxvalue);

    /**
     * Set NO MAXVALUE
     * @return the next step
     */
    SequenceStartStep noMaxvalue();
}
