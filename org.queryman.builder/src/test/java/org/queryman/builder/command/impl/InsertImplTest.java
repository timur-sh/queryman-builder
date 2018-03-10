package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.PostgreSQL;
import org.queryman.builder.Query;
import org.queryman.builder.ast.TreeFormatterTestUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.targetColumn;
import static org.queryman.builder.PostgreSQL.targetExpression;
import static org.queryman.builder.PostgreSQL.select;

class InsertImplTest {

    @Test
    void insertFull1() {
        String sql = PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .overridingSystemValue()
           .values(1, "test")
           .onConflict()
           .onConstraint("index_name")
           .doNothing()
           .sql()
        ;

        assertEquals("INSERT INTO book AS b (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'test') ON CONFLICT ON CONSTRAINT index_name DO NOTHING", sql);
    }

    @Test
    void insertFull2() {
        String sql = PostgreSQL.insertInto("book")
           .columns("id", "name")
           .overridingUserValue()
           .values(1, "test")
           .onConflict(targetColumn("tt", "44", "55"), PostgreSQL.targetExpression("qq"))
           .where("id", "=", "2")
           .and("id", "!=", "3")
           .doNothing()
           .returning("id")
           .sql()
        ;
        assertEquals("INSERT INTO book (id, name) OVERRIDING USER VALUE VALUES (1, 'test') ON CONFLICT (tt COLLATE 44 55, (qq)) WHERE id = 2 AND id != 3 DO NOTHING RETURNING id", sql);
    }

    @Test
    void insertFull3() throws NoSuchFieldException, IllegalAccessException {
        Query query = PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .defaultValues()
           .onConflict("id")
           .doUpdate()
           .set("id", 1)
           .set("name", "test")
           .where("id", "=", 1)
           .and("id", "!=", 3)
           .returning("id")
        ;
        assertEquals("INSERT INTO book AS b (id, name) DEFAULT VALUES ON CONFLICT (id) DO UPDATE SET id = 1, name = 'test' WHERE id = 1 AND id != 3 RETURNING id", query.sql());
        assertEquals("INSERT INTO book AS b (id, name) DEFAULT VALUES ON CONFLICT (id) DO UPDATE SET id = ?, name = ? WHERE id = ? AND id != ? RETURNING id", TreeFormatterTestUtil.buildPreparedSQL(query));


    }

    @Test
    void insertFull4() {
        String sql = PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .values(select("id", "name").from("book"))
           .onConflict()
           .doNothing()
           .returning("id")
           .sql()
        ;
        assertEquals("INSERT INTO book AS b (id, name) VALUES (SELECT id, name FROM book) ON CONFLICT DO NOTHING RETURNING id", sql);
    }
}