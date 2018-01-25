package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.Select;
import org.queryman.builder.command.select.SelectFromStep;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectImplTest {
    private AbstractSyntaxTree ast;

    @BeforeEach
    void tearUp() {
        ast = new AbstractSyntaxTreeImpl();
    }

    @Test
    void select() {
        Select select = new SelectImpl(ast, "id", "name", "min(price) as min");
        assertEquals("select id, name, min(price) as min", select.sql());
    }

    @Test
    void selectFrom() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");

        assertEquals("select id, name from books", select.from("books").sql());

        assertEquals("select id, name from table1, table2", select.from("table1", "table2").sql());
    }


}