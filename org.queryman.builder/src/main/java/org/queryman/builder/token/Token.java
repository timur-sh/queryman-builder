/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.token;

/**
 * PostgreSQL token can be a key word, an identifier(that is splitted on unqualifiedName
 * and qualifiedName), a constant, an operator. Any of above tokens must be based
 * on this interface.
 *
 * @see Field
 * @see Keyword
 * @see Operator
 * @see UnqualifiedName
 * @see QualifiedName
 *
 * @author Timur Shaidullin
 */
public interface Token {
    String getName();

    boolean isNonEmpty();

    boolean isEmpty();
}