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
        Bootstrap bootstrap = new Bootstrap()
           .init();

        flyway.setDataSource(bootstrap.getDataSource());
    }

    public void migrate() {
        System.err.close();
        flyway.migrate();
        System.setErr(System.out);
    }

    public void clean() {
        flyway.clean();
    }
}
