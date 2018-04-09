/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Token;

import java.util.List;

/**
 * Element of tree.
 *
 * @author Timur Shaidullin
 */
public interface Node {
    /**
     * Add children node.
     */
    Node addChildNode(Node node);

    /**
     * Add leaf.
     */
    Node addLeaf(Token value);

    /**
     * Get leaves.
     */
    List<Token> getLeaves();

    /**
     * Get children nodes.
     */
    List<Node> getNodes();

    /**
     * Return true if {@code this} does not contain other nodes.
     */
    boolean isEmpty();

    /**
     * Return count of child nodes.
     */
    int count();

    /**
     * Leaves is separated by delimiter.
     */
    Node setDelimiter(String c);

    /**
     * Leaves is separated by delimiter.
     */
    String getDelimiter();

    /**
     * Get node metadata.
     */
    NodeMetadata getNodeMetadata();

    /**
     * Set node metadata.
     */
    NodeMetadata setNodeMetadata(NodeMetadata nodeMetadata);

    /**
     * Delete all nodes and leaves.
     */
    void clear();
}
