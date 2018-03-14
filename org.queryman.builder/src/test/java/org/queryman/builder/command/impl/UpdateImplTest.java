package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.Query;
import org.queryman.builder.command.update.UpdateSetStep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Operators.LT;
import static org.queryman.builder.Operators.NE2;
import static org.queryman.builder.Operators.NOT_IN;
import static org.queryman.builder.Queryman.asConstant;
import static org.queryman.builder.Queryman.asList;
import static org.queryman.builder.Queryman.asName;
import static org.queryman.builder.Queryman.asQuotedName;
import static org.queryman.builder.Queryman.condition;
import static org.queryman.builder.Queryman.select;
import static org.queryman.builder.Queryman.update;
import static org.queryman.builder.Queryman.updateOnly;
import static org.queryman.builder.ast.TreeFormatterTestUtil.buildPreparedSQL;

class UpdateImplTest {
    @Test
    void updateSetTest() throws NoSuchFieldException, IllegalAccessException {
        Query query = update("book")
           .set(asList(asName("id"), asName("name")), asList(1, asConstant("Timur")))
           .set(asList(asName("year"), asName("price")), select("year", "price").from("book"))
           .from("order");

        assertEquals("UPDATE book SET (id, name) = (1, 'Timur'), (year, price) = (SELECT year, price FROM book) FROM order", query.sql());
        assertEquals("UPDATE book SET (id, name) = (?, ?), (year, price) = (SELECT year, price FROM book) FROM order", buildPreparedSQL(query));
    }

    @Test
    void updateOnlyTest() {
        String sql = updateOnly("book").sql();
        assertEquals("UPDATE ONLY book", sql);
    }

    @Test
    void updateAsTest() {
        String sql = update("book").as("b").sql();
        assertEquals("UPDATE book AS b", sql);
    }

    @Test
    void updateAsSetTest() {
        String sql = update("book").as("b").set("year", 2010).sql();
        assertEquals("UPDATE book AS b SET year = 2010", sql);
    }

    @Test
    void updateAsSetWhereTest() {
        String sql = update("book").as("b").set("author", 1).where("b.id", "=", "1").sql();
        assertEquals("UPDATE book AS b SET author = 1 WHERE b.id = 1", sql);
    }

    @Test
    void updateWhereAnd() {
        UpdateSetStep update = update("book")
           .as("b");

        update.set("author", asConstant("order"))
           .where("b.id", "=", "1");

        assertEquals("UPDATE book AS b SET author = 'order' WHERE b.id = 1", update.sql());


        update = update("book")
           .as("b");

        String sql = update.where("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND id2 = 2", sql);

        sql = update
           .where("id", "=", "1")
           .and(asQuotedName("id2"), EQUAL, asConstant(2))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND \"id2\" = 2", sql);

        sql = update
           .where("id", "=", "1")
           .and(condition(asQuotedName("id2"), EQUAL, asConstant(4)))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND \"id2\" = 4", sql);

        sql = update
           .where("id", "=", "1")
           .and(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = update
           .where("id", "=", "1")
           .andExists(select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void updateWhereAndNot() {
        UpdateSetStep update = update("book").as("b");

        String sql = update
           .where("id", "=", "1")
           .andNot("id2", "!=", "2")
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND NOT id2 != 2", sql);

        sql = update
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), EQUAL, asConstant(3))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND NOT \"id2\" = 3", sql);

        sql = update
           .where("id", "=", "1")
           .andNot(condition(asQuotedName("id2"), EQUAL, asConstant(4)))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND NOT \"id2\" = 4", sql);

        sql = update
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = update
           .where("id", "=", "1")
           .andNotExists(select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 AND NOT EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void updateWhereOr() {
        UpdateSetStep update = update("book").as("b");
        String sql = update
           .where("id", "=", "1")
           .or("id2", "=", "2")
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR id2 = 2", sql);

        sql = update
           .where("id", "<>", "1")
           .or(asQuotedName("id2"), NE2, asConstant(3))
           .sql();
        assertEquals("UPDATE book AS b WHERE id <> 1 OR \"id2\" <> 3", sql);

        sql = update
           .where("id", "=", "1")
           .or(condition(asQuotedName("id2"), LT, asConstant(4)))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR \"id2\" < 4", sql);

        sql = update
           .where("id", "=", "1")
           .or(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = update
           .where("id", "=", "1")
           .orExists(select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void updateWhereOrNot() {
        UpdateSetStep update = update("book").as("b");
        String sql = update
           .where("id", "=", "1")
           .orNot("id2", "=", "2")
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR NOT id2 = 2", sql);

        sql = update
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), EQUAL, asConstant(3))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR NOT \"id2\" = 3", sql);

        sql = update
           .where("id", "=", "1")
           .orNot(condition(asQuotedName("id2"), EQUAL, asConstant(4)))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR NOT \"id2\" = 4", sql);

        sql = update
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = update
           .where("id", "=", "1")
           .orNotExists(select("1", "2"))
           .sql();
        assertEquals("UPDATE book AS b WHERE id = 1 OR NOT EXISTS (SELECT 1, 2)", sql);
    }


    @Test
    void updateAsUsingWhereReturningTest() throws NoSuchFieldException, IllegalAccessException {
        String sql = update("book")
           .as("b")
           .set("author", asConstant("order"))
           .where("b.id", "=", "1")
           .returning("*")
           .sql();
        assertEquals("UPDATE book AS b SET author = 'order' WHERE b.id = 1 RETURNING *", sql);

        Query query = update("book")
           .as("b")
           .set("author", asConstant("Andrew"))
           .where("b.id", "=", 1)
           .returning(asName("max(price)").as("price"));
        assertEquals("UPDATE book AS b SET author = 'Andrew' WHERE b.id = 1 RETURNING max(price) AS price", query.sql());

        assertEquals("UPDATE book AS b SET author = ? WHERE b.id = ? RETURNING max(price) AS price", buildPreparedSQL(query));
    }
}