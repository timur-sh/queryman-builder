/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.flywaydb.core.Flyway;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public final class FlywayManager {
    public Flyway flyway = new Flyway();

    public void init() {
        flyway.setDataSource(Bootstrap.BOOT.getDataSource());
    }

    public void migrate() {
        flyway.migrate();
    }

    public void clean() {
        flyway.clean();
    }
}
