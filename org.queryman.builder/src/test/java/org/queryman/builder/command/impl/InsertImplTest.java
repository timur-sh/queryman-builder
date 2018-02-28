package org.queryman.builder.command.impl;

import org.queryman.builder.PostgreSQL;

import static org.junit.jupiter.api.Assertions.*;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asString;
import static org.queryman.builder.PostgreSQL.conflictTargetColumn;

class InsertImplTest {
    void insertFull1() {
        String sql = PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .overridingSystemValue()
           .values("1", asString("test"))
           .onConflict()
           .onConstraint("index_name")
           .doNothing()
           .sql()
        ;

        assertEquals("INSERT INTO book AS b (id, name) OVERRIDING SYSTEM VALUE VALUES(1, 'test') ON CONFLICT ON CONSTRAINT index_name DO NOTHING", sql);
    }

    void isnertFull2() {
        String sql = PostgreSQL.insertInto("book")
           .columns("id", "name")
           .overridingUserValue()
           .values("1", asName("test"))
           .onConflict(conflictTargetColumn("tt"))
           .where("id", "=", "2")
           .and("id", "!=", "3")
           .doNothing()
           .returning("id")
           .sql()
        ;
        assertEquals("INSERT INTO book AS b (id, name) OVERRIDING USER VALUE VALUES(1, 'test') ON CONFLICT (tt) WHERE id = 2 AND id != 3 DO NOTHING RETURNING id", sql);
    }

    void insertFull2() {
        String sql = PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .defaultValues()
           .onConflict(conflictTargetColumn("tt"))
           .doUpdate()
           .set("id", 1)
           .set("name", asString("test"))
           .where("id", "=", "2")
           .and("id", "!=", "3")
           .returning("id")
           .sql()
        ;
        assertEquals("INSERT INTO book AS b (id, name) DEFAULT VALUES ON CONFLICT (tt) DO UPDATE SET 1, 2 WHERE id = 2 AND id != 3 RETURNING id", sql);
    }

    void insertFull4() {
        String sql = PostgreSQL.insertInto("book")
           .as("b")
           .columns("id", "name")
           .values("1", "2")
           .onConflict()
           .doNothing()
           .returning("id")
           .sql()
        ;
        assertEquals("INSERT INTO book AS b (id, name) VALUES (1, 2) ON CONFLICT DO NOTHING RETURNING id", sql);
    }
}