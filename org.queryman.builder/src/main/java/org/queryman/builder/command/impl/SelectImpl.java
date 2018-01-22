/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.ast.AST;
import org.queryman.builder.command.select.Select;
import org.queryman.builder.command.select.SelectFinalStep;
import org.queryman.builder.command.select.SelectInitialStep;
import org.queryman.builder.AbstractQuery;

/**
 * @author Timur Shaidullin
 */
public class SelectImpl extends AbstractQuery implements
   SelectInitialStep,
   SelectFinalStep,
   Select {
    private final String[] columns;

    public SelectImpl(AST ast, String... columns) {
        super(ast);
        this.columns = columns;
    }

    @Override
    public Select select() {
        return null;
    }

    @Override
    public Select selectDistinct() {
        return null;
    }

    @Override
    public Select selectAll() {
        return null;
    }

    @Override
    public Select selectOn() {
        return null;
    }
}
