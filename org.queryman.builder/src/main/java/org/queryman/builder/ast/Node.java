/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.List;

/**
 * Element of tree.
 *
 * @author Timur Shaidullin
 */
interface Node {
    Node addChildNode(Node node);

    Node addLeaf(String value);

    List<String> getLeaves();

    List<Node> getNodes();

    boolean isEmpty();

    Node setSeparator(char c);

    char getSeparator();

    String getNodeName();
}
