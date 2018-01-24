/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.Command;
import org.queryman.builder.Metadata;
import org.queryman.builder.MetadataBuilder;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AbstractSyntaxTreeImpl;
import org.queryman.builder.impl.MetadataBuilderImpl;

/**
 * @author Timur Shaidullin
 */
public class ServiceRegister {
    private MetadataBuilder metadataBuilder;

    public ServiceRegister() {
        this(new MetadataBuilderImpl());
    }

    public ServiceRegister(MetadataBuilder metadataBuilder) {
        this.metadataBuilder = metadataBuilder;
    }

    public ServiceRegister changeBuilder(MetadataBuilder newBuilder) {
        metadataBuilder = newBuilder;
        return this;
    }

    public MetadataBuilder getMetadataBuilder() {
        return metadataBuilder;
    }

    public ServiceRegister make() {
        metadataBuilder.build();
        return this;
    }

    public ServiceRegister make(Metadata metadata) {
        metadataBuilder.build(metadata);
        return this;
    }

    private final AbstractSyntaxTree ast() {
        return new AbstractSyntaxTreeImpl();
    }

    public Command getCommand() {
        return new Command(ast());
    }

}
