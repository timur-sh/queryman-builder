/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * PostgreSQL operators.
 *
 * Immutable object.
 *
 * @author Timur Shaidullin
 */
public class Operator extends Keyword {
    private int position = 1;

    public Operator(String name) {
        super(name);
    }

    public Operator(String name, int position) {
        super(name);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    /**
     *
     * @param position position between operands where {@code operator} appears
     * @return itself
     */
    public Operator setPosition(int position) {
        return new Operator(name, position);
    }
}
