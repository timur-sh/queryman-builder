/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.ast.TreeFactory;

/**
 * @author Timur Shaidullin
 */
public class ServiceRegister {
    private MetadataBuilder metadataBuilder;
    private final TreeFactory treeFactory = new TreeFactory();

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
        treeFactory.setMetadata(metadataBuilder.getMetadata());
        return this;
    }

    public ServiceRegister makeDefaults() {
        metadataBuilder.buildFromDefault();
        treeFactory.setMetadata(metadataBuilder.getMetadata());
        return this;
    }

    public ServiceRegister make(Metadata metadata) {
        metadataBuilder.build(metadata);
        treeFactory.setMetadata(metadataBuilder.getMetadata());
        return this;
    }

    public final TreeFactory treeFactory() {
        return treeFactory;
    }

}
