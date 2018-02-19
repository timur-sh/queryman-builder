/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.boot;

import org.queryman.builder.ast.TreeFactory;

/**
 * Register a services such as {@link TreeFactory}.
 *
 * @author Timur Shaidullin
 */
public class ServiceRegister {
    private MetadataBuilder metadataBuilder;
    private final TreeFactory treeFactory = new TreeFactory();

    /**
     * The default metadata build implementation is used.
     */
    public ServiceRegister() {
        this(new MetadataBuilderImpl());
    }

    /**
     * The user provided metadata implementation is used.
     *
     * @param metadataBuilder builder
     */
    public ServiceRegister(MetadataBuilder metadataBuilder) {
        this.metadataBuilder = metadataBuilder;
    }

    /**
     * Change metadata builder implementation.
     *
     * @param newBuilder new builder
     * @return current instance
     */
    public ServiceRegister changeBuilder(MetadataBuilder newBuilder) {
        metadataBuilder = newBuilder;
        return this;
    }

    /**
     * @return a metadata builder
     */
    public MetadataBuilder getMetadataBuilder() {
        return metadataBuilder;
    }

    /**
     * Builds metadata from configuration files.
     *
     * @return current instance
     */
    public ServiceRegister make() {
        metadataBuilder.build();
        treeFactory.setMetadata(metadataBuilder.getMetadata());
        return this;
    }

    /**
     * Builds default metadata.
     *
     * @return current instance
     */
    public ServiceRegister makeDefaults() {
        metadataBuilder.buildFromDefault();
        treeFactory.setMetadata(metadataBuilder.getMetadata());
        return this;
    }

    /**
     * Builds metadata from configuration files, then merge it with user-provided
     * metadata.
     *
     * @return current instance
     */
    public ServiceRegister make(Metadata metadata) {
        metadataBuilder.build(metadata);
        treeFactory.setMetadata(metadataBuilder.getMetadata());
        return this;
    }

    /**
     * @return a service to make an instance of tree.
     */
    public final TreeFactory treeFactory() {
        return treeFactory;
    }

}
