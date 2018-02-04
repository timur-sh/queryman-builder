/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * @author Timur Shaidullin
 */
public class Operator extends Keyword {
//    private boolean binary = true;
//
//    /**
//     * If {@link #binary} equals {@code false}, the {@code prefix} specifies
//     * where the operator will locate,
//     */
//    private boolean prefix = true;

    public Operator(String name) {
        super(name);
    }
}
