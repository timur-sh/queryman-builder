/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Shaidullin
 */
final class TreeFormatter {
    String treeToString(Node node) {
        List<String> leaves = node.getLeaves();

        List<String> list = new ArrayList<>(leaves.subList(0, node.getNodeMetadata().getPosition()));

        if (node.getNodeMetadata().getName().length() > 0) {
            list.add(node.getNodeMetadata().getName());
        }

        if (leaves.size() > 0) {
            List<String> tmp = leaves.subList(node.getNodeMetadata().getPosition(), leaves.size());
            list.add(String.join(node.getDelimiter(), tmp));
        }

        if (!node.isEmpty()) {
            for (Node n : node.getNodes()) {
                list.add(treeToString(n));
            }
        }

        return new Pipeline(node.getNodeMetadata())
           .process(list);
    }

    private class Pipeline {
        private final NodeMetadata metadata;

        private Pipeline(NodeMetadata metadata) {
            this.metadata = metadata;
        }

        private String process(List<String> list) {
            list = parentheses(list);

            return String.join(" ", list);
        }

        private List<String> parentheses(List<String> list) {
            if (metadata.isParentheses()) {
                List<String> newList = new ArrayList<>();
                newList.add("(" + String.join(" ", list) + ")");

                return newList;
            }

            return list;
        }
    }
}
