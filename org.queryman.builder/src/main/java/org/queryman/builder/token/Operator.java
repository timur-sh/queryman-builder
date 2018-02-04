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
    private int position = 1;

    public Operator(String name) {
        super(name);
    }

    public int getPosition() {
        return position;
    }

    public Operator setPosition(int position) {
        this.position = position;

        return this;
    }
}
