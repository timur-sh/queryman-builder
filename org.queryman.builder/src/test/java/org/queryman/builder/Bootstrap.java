/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Timur Shaidullin
 */
public class Bootstrap {
    private static Properties properties = new Properties();
    private DataSource dataSource;

    public Bootstrap init() {
        try {
            load();
        } catch (IOException e) {
            throw new BootstrapException(e.getMessage());
        }

        //todo move it to JNDI
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setUser(properties.getProperty("user"));
        source.setPassword(properties.getProperty("password"));
        source.setPortNumber(Integer.valueOf(properties.getProperty("port")));
        source.setDatabaseName(properties.getProperty("databaseName"));
        source.setServerName(properties.getProperty("serverName"));
        this.dataSource = source;

        return this;
    }

    private void load() throws IOException {
        URL resources = this.getClass().getClassLoader().getResource("database.properties");

        if (resources != null) {
            properties.load(resources.openStream());
            return;
        }

        throw new FileNotFoundException("File database.properties not found");
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
