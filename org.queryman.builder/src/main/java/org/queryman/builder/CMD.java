/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder;

import org.queryman.builder.command.impl.SelectImpl;
import org.queryman.builder.command.select.Select;
import org.queryman.builder.types.Identifier;

/**
 * This {@code class} represents entry point of commands of PostgreSQL.
 *
 * @author Timur Shaidullin
 */
public class CMD {
    public CMD(Metadata metadata) {

    }

    //---
    // SELECT API
    //---
    public Select select() {
        return new SelectImpl();
    }

    public Select selectDistinct(Identifier... identifiers) {
        return new SelectImpl();
    }

    public Select selectAll(Identifier... identifiers) {
        return new SelectImpl();
    }

    public Select selectOn(Identifier... identifiers) {
        return new SelectImpl();
    }
}
