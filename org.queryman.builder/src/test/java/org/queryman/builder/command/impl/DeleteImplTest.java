package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;
import org.queryman.builder.command.delete.DeleteUsingStep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.Operators.EQUAL;
import static org.queryman.builder.Operators.LT;
import static org.queryman.builder.Operators.NE2;
import static org.queryman.builder.Operators.NOT_IN;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asNumber;
import static org.queryman.builder.PostgreSQL.asQuotedName;
import static org.queryman.builder.PostgreSQL.condition;
import static org.queryman.builder.PostgreSQL.deleteFrom;
import static org.queryman.builder.PostgreSQL.deleteFromOnly;
import static org.queryman.builder.PostgreSQL.select;

class DeleteImplTest {
    @Test
    void deleteFromTest() {
        String sql = deleteFrom("book").sql();
        assertEquals("DELETE FROM book", sql);

        sql = deleteFrom(asQuotedName("book")).sql();
        assertEquals("DELETE FROM \"book\"", sql);
    }

    @Test
    void deleteFromOnlyTest() {
        String sql = deleteFromOnly("book").sql();
        assertEquals("DELETE FROM ONLY book", sql);
    }

    @Test
    void deleteFromAsTest() {
        String sql = deleteFrom("book").as("b").sql();
        assertEquals("DELETE FROM book AS b", sql);
    }

    @Test
    void deleteFromAsUsingTest() {
        String sql = deleteFrom("book").as("b").using("author", "order").sql();
        assertEquals("DELETE FROM book AS b USING author, order", sql);
    }

    @Test
    void deleteFromAsUsingWhereTest() {
        String sql = deleteFrom("book").as("b").using("author", "order").where("b.id", "=", "1").sql();
        assertEquals("DELETE FROM book AS b USING author, order WHERE b.id = 1", sql);
    }

    @Test
    void deleteFromWhereAnd() {
        DeleteUsingStep delete = deleteFrom("book")
           .as("b");

        delete.using("author", "order")
           .where("b.id", "=", "1");

        assertEquals("DELETE FROM book AS b USING author, order WHERE b.id = 1", delete.sql());


        delete = deleteFrom("book")
           .as("b");

        String sql = delete.where("id", "=", "1")
           .and("id2", "=", "2")
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND id2 = 2", sql);

        sql = delete
           .where("id", "=", "1")
           .and(asQuotedName("id2"), EQUAL, asNumber(2))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND \"id2\" = 2", sql);

        sql = delete
           .where("id", "=", "1")
           .and(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND \"id2\" = 4", sql);

        sql = delete
           .where("id", "=", "1")
           .and(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = delete
           .where("id", "=", "1")
           .andExists(select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void deleteFromWhereAndNot() {
        DeleteUsingStep delete = deleteFrom("book").as("b");

        String sql = delete
           .where("id", "=", "1")
           .andNot("id2", "!=", "2")
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND NOT id2 != 2", sql);

        sql = delete
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), EQUAL, asNumber(3))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND NOT \"id2\" = 3", sql);

        sql = delete
           .where("id", "=", "1")
           .andNot(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND NOT \"id2\" = 4", sql);

        sql = delete
           .where("id", "=", "1")
           .andNot(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = delete
           .where("id", "=", "1")
           .andNotExists(select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 AND NOT EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void deleteFromWhereOr() {
        DeleteUsingStep delete = deleteFrom("book").as("b");
        String sql = delete
           .where("id", "=", "1")
           .or("id2", "=", "2")
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR id2 = 2", sql);

        sql = delete
           .where("id", "<>", "1")
           .or(asQuotedName("id2"), NE2, asNumber(3))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id <> 1 OR \"id2\" <> 3", sql);

        sql = delete
           .where("id", "=", "1")
           .or(condition(asQuotedName("id2"), LT, asNumber(4)))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR \"id2\" < 4", sql);

        sql = delete
           .where("id", "=", "1")
           .or(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = delete
           .where("id", "=", "1")
           .orExists(select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR EXISTS (SELECT 1, 2)", sql);
    }

    @Test
    void deleteFromWhereOrNot() {
        DeleteUsingStep delete = deleteFrom("book").as("b");
        String sql = delete
           .where("id", "=", "1")
           .orNot("id2", "=", "2")
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR NOT id2 = 2", sql);

        sql = delete
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), EQUAL, asNumber(3))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR NOT \"id2\" = 3", sql);

        sql = delete
           .where("id", "=", "1")
           .orNot(condition(asQuotedName("id2"), EQUAL, asNumber(4)))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR NOT \"id2\" = 4", sql);

        sql = delete
           .where("id", "=", "1")
           .orNot(asQuotedName("id2"), NOT_IN, select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR NOT \"id2\" NOT IN (SELECT 1, 2)", sql);

        sql = delete
           .where("id", "=", "1")
           .orNotExists(select("1", "2"))
           .sql();
        assertEquals("DELETE FROM book AS b WHERE id = 1 OR NOT EXISTS (SELECT 1, 2)", sql);
    }


    @Test
    void deleteFromAsUsingWhereReturningTest() {
        String sql = deleteFrom("book")
           .as("b")
           .using("author", "order")
           .where("b.id", "=", "1")
           .returning("*")
           .sql();
        assertEquals("DELETE FROM book AS b USING author, order WHERE b.id = 1 RETURNING *", sql);

        sql = deleteFrom("book")
           .as("b")
           .using("author", "order")
           .where("b.id", "=", "1")
           .returning(asName("max(price)").as("price"))
           .sql();
        assertEquals("DELETE FROM book AS b USING author, order WHERE b.id = 1 RETURNING max(price) AS price", sql);
    }
}