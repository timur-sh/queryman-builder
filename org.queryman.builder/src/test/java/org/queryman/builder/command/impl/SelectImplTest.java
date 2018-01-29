package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.Select;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.select.SelectFromStep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Statements.condition;

class SelectImplTest extends BaseTest {
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

        assertEquals(
           "select id, name from books",
           select.from("books").sql()
        );

        assertEquals("select id, name from table1, table2", select.from("table1", "table2").sql());
    }

    @Test
    void selectFromWhere() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("select id, name from books where id = 1 and id2 = 2", sql);
    }

    @Test
    void selectFromWhereGroupBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .groupBy("id")
           .sql();
        assertEquals("select id, name from books where id = 1 group by id", sql);
    }

    @Test
    void selectFromGroupBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .groupBy("id")
           .sql();
        assertEquals("select id, name from books group by id", sql);
    }

    @Test
    void selectFromWhereGroup() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("user")
           .where(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", "'timur@shaidullin.net'")
           )
           .or("id", "=", "1")
           .sql();

        //todo this is not valid sql query: ... = timur@shaidullin.net
        assertEquals("select id, name from user where (name = timur and phone is null or email = 'timur@shaidullin.net') or id = 1", sql);
    }

    @Test
    void selectFromWhereAndWhereGroup() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("user")
           .where("id", "=", "1")
           .and(condition("name", "=", "timur")
              .and("phone", "is", "null")
              .or("email", "=", "'timur@shaidullin.net'")
           )
           .and("id2", "=", "2")
           .sql();

        //todo this is not valid sql query: ... = timur@shaidullin.net
        assertEquals("select id, name from user where id = 1 and (name = timur and phone is null or email = 'timur@shaidullin.net') and id2 = 2", sql);
    }
}