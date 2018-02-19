/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.boot.Metadata;

/**
 * Encapsulates an initialization of {@link AbstractSyntaxTreeImpl}. By default
 * it uses a default {@code metadata};
 *
 * @author Timur Shaidullin
 */
public class TreeFactory {
    private Metadata metadata;

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public AbstractSyntaxTree getTree() {
        return new AbstractSyntaxTreeImpl();
    }
}
