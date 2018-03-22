package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.BaseTest;
import org.queryman.builder.Query;
import org.queryman.builder.Queryman;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Queryman.select;
import static org.queryman.builder.Queryman.targetColumn;
import static org.queryman.builder.Queryman.targetExpression;
import static org.queryman.builder.TestHelper.testBindParameters;
import static org.queryman.builder.ast.TreeFormatterUtil.buildPreparedSQL;


class InsertImplTest extends BaseTest {

    @Test
    void insertFull1() throws SQLException {
        Query query = Queryman.insertInto("book")
           .as("b")
           .columns("id", "name")
           .overridingSystemValue()
           .values(1, "test")
           .onConflict()
           .onConstraint("index_name")
           .doNothing()
        ;

        assertEquals("INSERT INTO book AS b (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'test') ON CONFLICT ON CONSTRAINT index_name DO NOTHING", query.sql());
        assertEquals("INSERT INTO book AS b (id, name) OVERRIDING SYSTEM VALUE VALUES (?, ?) ON CONFLICT ON CONSTRAINT index_name DO NOTHING", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(2, map.size());
        });
        inBothStatement(query, rs -> {});
    }

    @Test
    void insertFull2() throws SQLException {
        Query query = Queryman.insertInto("book")
           .columns("id", "name")
           .overridingUserValue()
           .values(1, "test")
           .onConflict(targetColumn("tt", "44", "55"), targetExpression("qq"))
           .where("id", "=", 2)
           .and(3, "!=", "id")
           .doNothing()
           .returning("id")
        ;
        assertEquals("INSERT INTO book (id, name) OVERRIDING USER VALUE VALUES (1, 'test') ON CONFLICT (tt COLLATE 44 55, (qq)) WHERE id = 2 AND 3 != id DO NOTHING RETURNING id", query.sql());
        assertEquals("INSERT INTO book (id, name) OVERRIDING USER VALUE VALUES (?, ?) ON CONFLICT (tt COLLATE 44 55, (qq)) WHERE id = ? AND ? != id DO NOTHING RETURNING id", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(4, map.size());
            assertEquals(1, map.get(1).getValue());
            assertEquals("test", map.get(2).getValue());
            assertEquals(2, map.get(3).getValue());
            assertEquals(3, map.get(4).getValue());
        });
        inBothStatement(query, rs -> {});
    }

    @Test
    void insertFull3() throws NoSuchFieldException, IllegalAccessException, SQLException {
        Query query = Queryman.insertInto("book")
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
        assertEquals("INSERT INTO book AS b (id, name) DEFAULT VALUES ON CONFLICT (id) DO UPDATE SET id = ?, name = ? WHERE id = ? AND id != ? RETURNING id", buildPreparedSQL(query));
        testBindParameters(query, map -> {
            assertEquals(4, map.size());
        });
//        inBothStatement(query, rs -> {});
    }

    @Test
    void insertFull4() {
        String sql = Queryman.insertInto("book")
           .as("b")
           .columns("id", "name")
           .query(select("id", "name").from("book"))
           .onConflict()
           .doNothing()
           .returning("id")
           .sql()
        ;
        assertEquals("INSERT INTO book AS b (id, name) (SELECT id, name FROM book) ON CONFLICT DO NOTHING RETURNING id", sql);
    }
}