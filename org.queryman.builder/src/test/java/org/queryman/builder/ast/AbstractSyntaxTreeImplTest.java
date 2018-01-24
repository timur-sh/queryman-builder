package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.ast.NodeMetadata.*;

public class AbstractSyntaxTreeImplTest {
    @Test
    public void simpleTree() throws NoSuchFieldException, IllegalAccessException {
        AbstractSyntaxTree tree = new AbstractSyntaxTreeImpl();

        tree.startNode(SELECT, ", ");
        tree.addLeaf("id");
        tree.addLeaf("name");
        tree.addLeaf("phone");

        tree.startNode(FROM);
        tree.addLeaf("table1").addLeaf("table2").setDelimiter(" ");
        tree.endNode();

        tree.startNode(WHERE, "")
           .addLeaf("id")
           .addLeaf("=")
           .addLeaf("id")
           .endNode();

        tree.endNode();

        assertEquals("select id, name, phone from table1 table2 where id=id", tree.toString());

        Field field = tree.getClass().getDeclaredField("nodes");
        field.setAccessible(true);
        Stack<Node> nodes = (Stack<Node>) field.get(tree);
        assertEquals(nodes.size(), 0);
    }
}