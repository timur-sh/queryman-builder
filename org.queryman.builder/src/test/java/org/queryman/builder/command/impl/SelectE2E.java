/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.junit.jupiter.api.Test;

import static org.queryman.builder.PostgreSQL.from;
import static org.queryman.builder.PostgreSQL.select;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * @author Timur Shaidullin
 */
public class SelectE2E {
    @Test
    void selectE2e() {
        String sql = select("id")
            .from(from("books").as("t").tablesample("SYSTEM", "1").repeatable(1))
            .sql()
           ;

        assertEquals("SELECT id FROM books AS t TABLESAMPLE SYSTEM(1) REPEATABLE(1)", sql);
    }
}
