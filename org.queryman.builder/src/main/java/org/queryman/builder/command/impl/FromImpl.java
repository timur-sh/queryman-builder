/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.impl;

import org.queryman.builder.PostgreSQL;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.command.from.From;
import org.queryman.builder.command.from.FromFinalStep;
import org.queryman.builder.command.from.FromFirstStep;
import org.queryman.builder.command.from.FromRepeatableStep;
import org.queryman.builder.command.from.FromTablesampleStep;
import org.queryman.builder.token.Expression;

import java.util.Arrays;
import java.util.Objects;

import static org.queryman.builder.PostgreSQL.asConstant;
import static org.queryman.builder.PostgreSQL.asFunc;
import static org.queryman.builder.PostgreSQL.asList;
import static org.queryman.builder.PostgreSQL.asName;

/**
 * Standard implementation of FROM statement.
 *
 * @author Timur Shaidullin
 */
public class FromImpl implements
   From,
   FromFirstStep,
   FromTablesampleStep,
   FromRepeatableStep,
   FromFinalStep,
   AstVisitor {

    private final Expression tableName;

    private String alias;

    private boolean      only;
    private String       tableSampleMethod;
    private Expression[] tableSampleArguments;
    private boolean      repeatable;
    private Number       seed;

    public FromImpl(Expression tableName) {
        this(tableName, false);
    }

    public FromImpl(Expression tableName, boolean only) {
        this.tableName = tableName;
        this.only = only;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        if (alias != null) {
            tableName.as(alias);
        }
        StringBuilder builder = new StringBuilder();

        if (only)
            builder.append(asName("ONLY " + tableName.getName()));
        else
            builder.append(tableName);

        if (tableSampleMethod != null) {
            builder.append(' ')
               .append(asFunc("TABLESAMPLE " + tableSampleMethod, asList(tableSampleArguments)));

            if (repeatable)
                builder
                   .append(' ')
                   .append(asFunc("REPEATABLE", asList(seed)));
        }

        tree.addLeaves(asConstant(builder.toString()));
    }

    //----
    // API
    //----

    @Override
    public final FromImpl as(String alias) {
        Objects.requireNonNull(alias);
        this.alias = alias;
        return this;
    }

    @Override
    public FromRepeatableStep tablesample(String method, String... arguments) {
        Objects.requireNonNull(arguments);

        return tablesample(method, Arrays.stream(arguments).map(PostgreSQL::asName).toArray(Expression[]::new));
    }

    @Override
    public final FromImpl tablesample(String method, Expression... arguments) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(arguments);

        if (arguments.length == 0)
            throw new IllegalArgumentException("Argument mus not be null");

        tableSampleMethod = method;
        tableSampleArguments = arguments;

        return this;
    }

    @Override
    public final FromImpl repeatable(Number seed) {
        repeatable = true;
        this.seed = seed;
        return this;
    }
}
