/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * Element of tree.
 *
 * @author Timur Shaidullin
 */
interface Node {
    Node addNode(Node node);

    Node createNode(String value);

    String getValue();
}
