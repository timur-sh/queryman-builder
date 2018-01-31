package org.queryman.builder.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.Select;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.command.select.SelectFromStep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.condition;

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

        assertEquals("select id, name from books", select.from("books").sql());

        assertEquals("select id, name from table1, table2", select.from("table1", "table2").sql());
    }

    @Test
    void selectFromOrderBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .orderBy("id")
           .sql();

        assertEquals("select id, name from books order by id", sql);

        sql = select.from("books")
           .orderBy("name", "desc")
           .sql();

        assertEquals("select id, name from books order by name desc", sql);

        sql = select.from("books")
           .orderBy("name", "desc", "nulls last")
           .sql();

        assertEquals("select id, name from books order by name desc nulls last", sql);
    }

    @Test
    void selectFromLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .limit(1)
           .sql();

        assertEquals("select id, name from books limit 1", sql);
    }

    @Test
    void selectFromOrderByLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .orderBy("id")
           .limit(2)
           .sql();

        assertEquals("select id, name from books order by id limit 2", sql);
    }

    @Test
    void selectFromGroupByLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .groupBy("id")
           .limit(2)
           .sql();

        assertEquals("select id, name from books group by id limit 2", sql);
    }

    @Test
    void selectFromWhereLimit() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .limit(3)
           .sql();
        assertEquals("select id, name from books where id = 1 and id2 = 2 limit 3", sql);
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
    void selectFromWhereOrderBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .orderBy("name")
           .sql();
        assertEquals("select id, name from books where id = 1 order by name", sql);

        sql = select.from("books")
           .where("id", "=", "1")
           .and("id2", "=", "2")
           .orderBy("id")
           .sql();
        assertEquals("select id, name from books where id = 1 and id2 = 2 order by id", sql);
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
    void selectFromWhereGroupByOrderBy() {
        SelectFromStep select = new SelectImpl(ast, "id", "name");
        String sql = select.from("books")
           .where("id", "=", "1")
           .groupBy("id")
           .orderBy("name")
           .sql();
        assertEquals("select id, name from books where id = 1 group by id order by name", sql);
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

        assertEquals("select id, name from user where id = 1 and (name = timur and phone is null or email = 'timur@shaidullin.net') and id2 = 2", sql);
    }
}