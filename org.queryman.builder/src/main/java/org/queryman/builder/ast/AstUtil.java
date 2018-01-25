/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Timur Shaidullin
 */
public final class AstUtil {
    public static final Node node(String name) {
        return new NodeImpl(name);
    }

    public static String treeToString(Node node) {
        List<String> list = new LinkedList<>();
        list.add(node.getNodeName());
        list.add(String.join(node.getDelimiter(), node.getLeaves()));

        if (!node.isEmpty()) {
            for (Node n : node.getNodes()) {
                list.add(treeToString(n));
            }
        }

        return String.join(" ", list);
    }
}
