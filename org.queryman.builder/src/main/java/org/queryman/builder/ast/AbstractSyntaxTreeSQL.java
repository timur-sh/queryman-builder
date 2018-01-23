/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * This class provides API to build abstract syntax tree of SQL. Each node must
 * start with {@link #startNode(NodeMetadata)} and end with {@link #endNode(NodeMetadata)}
 *
 * @author Timur Shaidullin
 */
public interface AbstractSyntaxTreeSQL {
    /**
     * Each node must have a {@code node object}, that contains metadata.
     */
    AbstractSyntaxTreeSQL startNode(NodeMetadata node);

    /**
     * Each node must closed.
     */
    AbstractSyntaxTreeSQL endNode(NodeMetadata node);

    /**
     * Add leaf into current node. Kinds of {@link Leaf} are SQL key words,
     * identifier, constants, expressions etc.
     */
    AbstractSyntaxTreeSQL addLeaf(Leaf leaf);
}
