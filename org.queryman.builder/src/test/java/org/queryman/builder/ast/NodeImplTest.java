package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.queryman.builder.ast.NodeUtil.node;

class NodeImplTest {
    @Test
    void test() {
        Node node = node("select");

        node.addNode("id")
           .addNode("name")
           .addNode(
              node("from")
                 .addNode("table1")
                 .addNode(
                    node("left join on")
                       .addNode("id=id")
                 )
           )
           .addNode(node("where").addNode("asd"));

        System.out.println(node.toString());
    }
}