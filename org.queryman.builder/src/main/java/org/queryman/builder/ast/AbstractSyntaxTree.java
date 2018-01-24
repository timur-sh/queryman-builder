/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * This class provides API to manipulate abstract syntax tree of SQL. Each node must
 * start with {@link #startNode(String)} and end with {@link #endNode()}
 *
 * Each leaf added at the end of parent node.
 *
 * @author Timur Shaidullin
 */
public interface AbstractSyntaxTree {
    /**
     * Each node must have a {@code node object}, that contains metadata.
     */
    AbstractSyntaxTree startNode(String name);

    /**
     * Each node must have a {@code node object}, that contains metadata. Set
     * custom delimiter. See {@link #setDelimiter(String)}.
     */
    AbstractSyntaxTree startNode(String name, String delimiter);

    /**
     * Each leaves must separated by {@code delimiter} token. If it is not
     * specified, space {code " "} is used as default delimiter.
     */
    AbstractSyntaxTree setDelimiter(String delimiter);

    /**
     * Each node must closed.
     */
    AbstractSyntaxTree endNode();

    /**
     * Add leaf into current node. Kinds of {@link Node} are SQL key words,
     * identifiers, constants, expressions etc.
     */
    AbstractSyntaxTree addLeaf(String node);

    /**
     * Insert child node into the parent node.
     */
    AbstractSyntaxTree addChildNode(Node node);
}
