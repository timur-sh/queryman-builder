package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.Statements;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.Select;
import org.queryman.builder.command.select.SelectFromStep;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Statements.*;

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

    @Test
    void selectFromWhere() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
            .where("id", "=", "1")
            .andWhere("id", "=", "1")
            .sql();
        assertEquals("select id, name from books where id = 1 AND id = 1", sql);

        sql = select.from("books")
            .where(where("id", "=", "1"), andWhere("id", "=", "1"))
            .andWhere("id2", "=", "2")
            .orWhere("id3", "=", "3")
            .sql();
        assertEquals("select id, name from books where id = 1 AND id2 = 2 OR id3 = 3", sql);
    }
}