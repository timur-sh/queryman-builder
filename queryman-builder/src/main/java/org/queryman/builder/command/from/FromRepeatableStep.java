/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.from;

/**
 * REPEATABLE step is followed by TABLESAMPLE step.
 *
 * @author Timur Shaidullin
 */
public interface FromRepeatableStep extends FromFinalStep {
    /**
     * Specify {@code seed} to use for generating random numbers within the
     * sampling method.
     *
     * @param seed
     */
    FromFinalStep repeatable(Number seed);
}
