package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.ast.NodeUtil.node;
import static org.queryman.builder.ast.NodeUtil.treeToString;

class NodeImplTest {
    @Test
    void simpleTree() {
        Node node = node(NodeMetadata.SELECT).setDelimiter(",");

        node.addLeaf("id")
           .addLeaf("name")
           .addChildNode(
              node("from")
                 .addLeaf("table1")
                 .addChildNode(
                    node("left join")
                       .addLeaf("table2")
                       .addChildNode(node("on")
                          .addLeaf("id")
                          .addLeaf("=")
                          .addLeaf("id")
                          .setDelimiter("")
                       )
                 )
           )
           .addChildNode(node("where")
              .addLeaf("id")
              .addLeaf("=")
              .addLeaf("id")
           );

        assertEquals("select id,name from table1 left join table2 on id=id where id = id ", treeToString(node));
    }
}