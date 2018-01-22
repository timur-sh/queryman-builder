/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.command.impl.SelectImpl;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.token.Identifier;

/**
 * This {@code class} represents entry point of sql commands of PostgreSQL.
 *
 * @author Timur Shaidullin
 */
public class Command {
    public Command() {

    }

    //---
    // SELECT API
    //---
    public SelectFinalStep select(Identifier... columns) {
//        return new SelectImpl();
        return null;
    }


//    public static
}
