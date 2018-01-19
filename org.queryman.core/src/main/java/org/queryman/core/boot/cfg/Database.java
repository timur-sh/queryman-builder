/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.core.boot.cfg;

/**
 * The type of database configurations
 *
 * @author Timur Shaidullin
 */
public final class Database {
    private String url;
    private String user;
    private String password;

    private final String driver = "org.postgresql.Driver";

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
