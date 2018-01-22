/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.token.Identifier;
import org.queryman.builder.token.Keyword;
import org.queryman.builder.token.QuotedIdentifier;

/**
 * PostgreSQL token is sequence of key words, identifiers, constants.
 * This {@code class} provides all kinds of tokens.
 *
 * @author Timur Shaidullin
 */
public class Tokens {
    public static Identifier identifier(String name) {
        return new Identifier(name);
    }

    public static QuotedIdentifier quotedIdentifier(String name) {
        return new QuotedIdentifier(name);
    }

    public static Keyword keyword(String name) {
        return new Keyword(name);
    }
}
