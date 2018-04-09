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
public interface SequenceStartStep extends SequenceCacheStep {
    /**
     * Set the start value.
     *
     * @param start initial value which is used when the sequence starts
     * @return the next step
     */
    SequenceCacheStep start(long start);
    /**
     * Set the start value.
     *
     * @param start initial value which is used when the sequence starts
     * @return the next step
     */
    SequenceCacheStep startWith(long start);
}
