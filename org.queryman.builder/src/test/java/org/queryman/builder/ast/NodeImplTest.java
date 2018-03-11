package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.keyword;
import static org.queryman.builder.Queryman.operator;
import static org.queryman.builder.ast.NodeUtil.node;

class NodeImplTest {
    @Test
    void simpleTree() {
        Node node = node(NodesMetadata.SELECT).setDelimiter(",");

        node.addLeaf(asName("id"))
           .addLeaf(asName("name"))
           .setDelimiter(", ")
           .addChildNode(
              node(keyword("from"))
                 .addLeaf(asName("table1"))
                 .addChildNode(
                    node(keyword("left join"))
                       .addLeaf(asName("table2"))
                       .addChildNode(node(keyword("on"))
                          .addLeaf(asName("id"))
                          .addLeaf(operator("="))
                          .addLeaf(asName("id"))
                          .setDelimiter("")
                       )
                 )
           )
           .addChildNode(node(keyword("condition"))
              .addLeaf(asName("id"))
              .addLeaf(operator("="))
              .addLeaf(asName("id"))
           );

        TreeFormatter formatter = new TreeFormatter();
        assertEquals("SELECT id, name from table1 left join table2 on id=id condition id = id", formatter.buildSQL(node));
    }
}