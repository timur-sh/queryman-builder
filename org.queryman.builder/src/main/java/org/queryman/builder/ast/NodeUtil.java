/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Keyword;

/**
 * @author Timur Shaidullin
 */
class NodeUtil {
    static Node node(Keyword name) {
        return new NodeImpl(new NodeMetadata(name));
    }

    static Node node(NodeMetadata metadata) {
        return new NodeImpl(metadata);
    }
}
