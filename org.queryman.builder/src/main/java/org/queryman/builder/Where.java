/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

/**
 *
 * This is marker interface represents {@code WHERE} clause of SQL.
 *
 * @author Timur Shaidullin
 */
public interface Where {


    public String getToken();

    public String getLeftValue();

    public String getRightValue();

    public String getOperator();
}
