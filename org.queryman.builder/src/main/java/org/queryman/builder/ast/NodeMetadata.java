/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * Represents metadata of node.
 *
 * @author Timur Shaidullin
 */
final class NodeMetadata {
    private final String name;

    NodeMetadata(String name) {
        this.name = name;
    }
}
