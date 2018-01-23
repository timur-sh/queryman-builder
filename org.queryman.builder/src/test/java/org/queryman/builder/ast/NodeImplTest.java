package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.queryman.builder.ast.NodeUtil.node;

class NodeImplTest {
    @Test
    void simpleTree() {
        Node node = node("select").setSeparator(',');

        node.addLeaf("id")
           .addLeaf("name")
           .addChildNode(
              node("from")
                 .addLeaf("table1")
                 .addChildNode(
                    node("left join on")
                       .addLeaf("id=id")
                 )
           )
           .addChildNode(node("where").addLeaf("asd"));

        System.out.println(printNode(node));
    }

    private static String printNode(Node node) {
        StringBuilder str = new StringBuilder(node.getNodeName());
        str.append(' ');
//        String result = node.getNodeName();

//        List<String> str = new ArrayList<>();

        for (String leaf : node.getLeaves()) {
            str.append(leaf).append(node.getSeparator());
        }

//        result += String.join(node.getSeparator() + "", str);

        if (!node.isEmpty()) {
            for (Node n : node.getNodes()) {
                 str.append(printNode(n));
            }
        }

        return str.toString();
    }
}