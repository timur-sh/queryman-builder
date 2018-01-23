/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * This class provides API to manipulate abstract syntax tree of SQL. Each node must
 * start with {@link #startNode(Node)} and end with {@link #endNode(Node)}
 *
 * Each leaf added at the end of parent node.
 *
 * @author Timur Shaidullin
 */
public interface AbstractSyntaxTree {
    /**
     * Each node must have a {@code node object}, that contains metadata.
     */
    AbstractSyntaxTree startNode(Node node);

    /**
     * Each node must closed.
     */
    AbstractSyntaxTree endNode(Node node);

    /**
     * Add leaf into current node. Kinds of {@link Leaf} are SQL key words,
     * identifiers, constants, expressions etc.
     */
    AbstractSyntaxTree addLeaf(Leaf leaf);

    /**
     * Add leaf into current node. Kinds of {@link Leaf} are SQL key words,
     * identifiers, constants, expressions etc.
     */
    AbstractSyntaxTree addLeaf(String leaf);

    /**
     * Insert child node into the parent node.
     */
    AbstractSyntaxTree addChildNode(Leaf leaf);
}
