/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.queryman.builder.testing.JDBC;

/**
 * @author Timur Shaidullin
 */
public class BaseTest {
    private static FlywayManager manager = new FlywayManager();

    protected static final org.queryman.builder.testing.JDBC JDBC = new JDBC();

    static {
        manager.init();
        manager.clean();
        manager.migrate();
    }

    @BeforeAll
    static void beforeAll() {
        JDBC.createConnection();
    }

    @AfterAll
    static void afterAll() {
        JDBC.closeConnection();
    }

}
