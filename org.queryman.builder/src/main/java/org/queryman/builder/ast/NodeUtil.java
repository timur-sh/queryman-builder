/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * @author Timur Shaidullin
 */
public final class NodeUtil {
    public static final Node node(String name) {
        return new NodeImpl(name);
    }

    public static String treeToString(Node node) {
        StringBuilder str = new StringBuilder(node.getNodeName());

        str.append(' ')
           .append(String.join(node.getDelimiter(), node.getLeaves()))
           .append(' ');

        if (!node.isEmpty()) {
            for (Node n : node.getNodes()) {
                str.append(treeToString(n));
            }
        }

        return str.toString();
    }
}
