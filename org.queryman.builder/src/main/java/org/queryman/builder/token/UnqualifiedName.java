/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * Represents unqualified name.
 * Examples:
 * <p>
 * <code>PostgreSQL.qualifiedName("books");</code> equals "books"
 * </p>
 *
 * @author Timur Shaidullin
 */
public class UnqualifiedName extends AbstractToken implements Name {
    public UnqualifiedName(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return isNonEmpty() ? "\"" + name + "\"" : null;
    }
}