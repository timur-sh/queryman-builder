package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.ast.NodeMetadata.*;

public class AbstractSyntaxTreeImplTest {
    @Test
    public void simpleTree() {
        AbstractSyntaxTree tree = new AbstractSyntaxTreeImpl();

        tree.startNode(SELECT, ",");
        tree.addLeaf("id");
        tree.addLeaf("name");
        tree.addLeaf("phone");

        tree.startNode(FROM);
        tree.addLeaf("table1").addLeaf("table2").setDelimiter(" ");

        tree.startNode(WHERE);
        tree.addLeaf("id")
           .addLeaf("=")
           .addLeaf("id");

        System.out.println(tree);
        assertEquals("select id,name,phone from table1 table2 where id = id ", tree.toString());
    }
}