/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.CMD;
import org.queryman.builder.Metadata;
import org.queryman.builder.MetadataBuilder;
import org.queryman.builder.impl.MetadataBuilderImpl;

import java.io.IOException;

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

    public ServiceRegister make() throws IOException, ClassNotFoundException {
        metadataBuilder.build();
        return this;
    }

    public ServiceRegister make(Metadata metadata) throws IOException, ClassNotFoundException {
        metadataBuilder.build(metadata);
        return this;
    }

    public CMD getCmd() {
        return new CMD(metadataBuilder.getMetadata());
    }

}
