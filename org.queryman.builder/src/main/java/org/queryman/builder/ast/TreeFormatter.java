/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Timur Shaidullin
 */
final class TreeFormatter {
    String treeToString(Node node) {
        Objects.requireNonNull(node);

        List<String> leaves = node.getLeaves()
           .stream()
           .map(Token::getName)
           .collect(Collectors.toList());

        NodeMetadata metadata = node.getNodeMetadata();
        List<String> list = new ArrayList<>(leaves.subList(0, metadata.getPosition()));

        Token token = metadata.getToken();
        if (token.isNonEmpty()) {
            if (list.size() > 0)
                list.add(metadata.getPosition(), token.getName());
            else
                list.add(token.getName());
        }

        if (leaves.size() > 0)
            list.add(String.join(node.getDelimiter(), leaves.subList(metadata.getPosition(), leaves.size())));

        if (!node.isEmpty()) {
            for (Node n : node.getNodes()) {
                list.add(treeToString(n));
            }
        }

        return new Pipeline(metadata)
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
