package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.createSequence;
import static org.queryman.builder.PostgreSQL.createSequenceIfNotExists;
import static org.queryman.builder.PostgreSQL.createTempSequence;
import static org.queryman.builder.PostgreSQL.createTempSequenceIfNotExists;

class SequenceImplTest {
    @Test
    void sequenceCommon() {
        String sql = createSequence("book_seq")
           .as("smallint")
           .incrementBy(1)
           .minvalue(0)
           .noMaxvalue()
           .startWith(0)
           .cache(5)
           .cycle()
           .ownedByNone()
           .sql();

        assertEquals("CREATE SEQUENCE book_seq AS smallint INCREMENT BY 1 MINVALUE 0 MAXVALUE NO MAXVALUE START WITH 0 CACHE 5 CYCLE OWNED BY NONE", sql);

        sql = createSequence("book_seq")
           .as("smallint")
           .increment(1)
           .noMinvalue()
           .maxvalue(10)
           .start(1)
           .noCycle()
           .ownedBy("book.id")
           .sql();

        assertEquals("CREATE SEQUENCE book_seq AS smallint INCREMENT 1 MINVALUE NO MINVALUE MAXVALUE 10 START 1 NO CYCLE OWNED BY book.id", sql);
    }

    @Test
    void sequenceSimple() {
        String sql = createSequence("book_seq")
           .sql();

        assertEquals("CREATE SEQUENCE book_seq", sql);
    }

    @Test
    void createTemp() {
        String sql = createTempSequence("book_seq")
           .sql();

        assertEquals("CREATE TEMP SEQUENCE book_seq", sql);
    }

    @Test
    void createTempIfNotExists() {
        String sql = createTempSequenceIfNotExists("book_seq")
           .sql();

        assertEquals("CREATE TEMP SEQUENCE IF NOT EXISTS book_seq", sql);
    }

    @Test
    void createIfNotExists() {
        String sql = createSequenceIfNotExists("book_seq")
           .sql();

        assertEquals("CREATE SEQUENCE IF NOT EXISTS book_seq", sql);
    }

}