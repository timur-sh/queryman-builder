package org.queryman.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asDollarString;
import static org.queryman.builder.PostgreSQL.asName;
import static org.queryman.builder.PostgreSQL.asQuotedName;

class PostgreSQLTest {

    @Test
    void asStringTest() {
        assertEquals("'Timur'", asConstant("Timur").getName());
        assertEquals("'I''m Timur'", asConstant("I'm Timur").getName());
    }

    @Test
    void asDollarStringTest() {
        assertEquals("$$Timur$$", asDollarString("Timur").getName());

        assertEquals("$$I'm Timur$$", asDollarString("I'm Timur").getName());

        assertEquals("$name$I'm Timur$name$", asDollarString("I'm Timur", "name").getName());
    }

    @Test
    void asNumberTest() {
        assertEquals("234.11", asConstant(234.11).getName());
        assertEquals("0.5", asConstant(.50).getName());
        assertEquals("0.51", asConstant(.51).getName());
        assertEquals("5.1", asConstant(.51E+1).getName());
    }

    @Test
    void asNameTest() {
        assertEquals("column", asName("column").getName());
        assertEquals("\"column\"", asQuotedName("column").getName());
    }

    @Test
    void asQualifiedNameTest() {
        assertEquals("table.column", asName("table.column").getName());
        assertEquals("\"table\".\"column\"", asQuotedName("table.column").getName());
    }

}