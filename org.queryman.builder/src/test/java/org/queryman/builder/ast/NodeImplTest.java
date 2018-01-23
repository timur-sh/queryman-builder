package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeImplTest {
    @Test
    void test() {
        Node node = new NodeImpl("select");

        node.addNode(new NodeImpl("id"))
           .addNode(new NodeImpl("name"))
           .addNode(new NodeImpl("from").addNode(new NodeImpl("table1")))
           .addNode(new NodeImpl("where").addNode(new NodeImpl("id=1")));

        System.out.println(node.toString());
    }
}