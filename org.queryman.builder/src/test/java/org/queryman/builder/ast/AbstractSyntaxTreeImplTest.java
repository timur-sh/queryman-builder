package org.queryman.builder.ast;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.queryman.builder.PostgreSQL.qualifiedName;
import static org.queryman.builder.ast.NodesMetadata.*;

public class AbstractSyntaxTreeImplTest {
    @Test
    public void simpleTree() throws NoSuchFieldException, IllegalAccessException {
        AbstractSyntaxTree tree = new AbstractSyntaxTreeImpl();

        tree.startNode(SELECT, ", ");
        tree.addLeaf(qualifiedName("id"));
        tree.addLeaf(qualifiedName("name"));
        tree.addLeaf(qualifiedName("phone"));

        tree.addLeaves(qualifiedName("date"), qualifiedName("email"));

        tree.startNode(FROM);
        tree.addLeaf(qualifiedName("table1")).addLeaf(qualifiedName("table2")).setDelimiter(" ");
        tree.endNode();

        tree.startNode(WHERE, "")
           .addLeaf(qualifiedName("id"))
           .addLeaf(qualifiedName("="))
           .addLeaf(qualifiedName("id"))
           .endNode();

        assertThrows(BrokenTreeException.class, () -> tree.toString());

        tree.endNode();

        assertEquals("SELECT id, name, phone, date, email FROM table1 table2 WHERE id=id", tree.toString());

        Field field = tree.getClass().getDeclaredField("NODES");
        field.setAccessible(true);
        Stack<Node> nodes = (Stack<Node>) field.get(tree);
        assertEquals(nodes.size(), 0);
    }
}