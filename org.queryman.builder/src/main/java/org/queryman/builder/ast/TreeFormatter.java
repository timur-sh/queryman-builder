/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

import org.queryman.builder.token.Expression;
import org.queryman.builder.token.PreparedExpression;
import org.queryman.builder.token.Token;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Timur Shaidullin
 */
final class TreeFormatter {
    private final Map<Integer, Expression> parameters = new Hashtable<>();

    String buildSQL(Node node) {
        return buildSQL(node, false);
    }

    String buildSQL(Node node, boolean prepare) {
        Objects.requireNonNull(node);

        NodeMetadata metadata = node.getNodeMetadata();

        List<String> list   = new ArrayList<>();
        List<String> leaves = leavesToStrings(node, prepare);
        Token        token  = metadata.getToken();

        if (metadata.getPosition() == 0) {
            if (token.getName().length() > 0)
                list.add(token.getName());

            if (leaves.size() > 0)
                list.add(String.join(node.getDelimiter(), leaves));
        } else if (leaves.size() != 0) {
            list.addAll(leaves);
        }

        if (!node.isEmpty()) {
            for (Node n : node.getNodes()) {
                list.add(buildSQL(n, prepare));
            }
        }

        if (metadata.getPosition() != 0) {
            if (list.size() == 0) {
                list.add(metadata.getPosition(), token.getName());
            } else {
                list.add(metadata.getPosition(), token.getName());
            }
        }

        if (metadata.isJoinNodes() && node.getNodes().size() > 0) {
            String[] joining = list.stream()
               .skip(list.size() - node.getNodes().size())
               .toArray(String[]::new);

            list = list.stream()
               .limit(list.size() - node.getNodes().size())
               .collect(Collectors.toList());

            list.add(String.join(node.getDelimiter(), joining));
        }

        return new Pipeline(metadata)
           .process(list);
    }

    private List<String> leavesToStrings(Node node, boolean prepare) {
        List<String> list = new ArrayList<>();

        for (Token t : node.getLeaves()) {
            if (prepare && t instanceof PreparedExpression) {
                PreparedExpression expression = (PreparedExpression) t;
                list.add(expression.getPlaceholder());

                synchronized (parameters) {
                    parameters.put(parameters.size(), expression);
                }

            } else {
                list.add(t.getName());
            }
        }

        return list;
    }

    public Map<Integer, Expression> getParameters() {
        return parameters;
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
