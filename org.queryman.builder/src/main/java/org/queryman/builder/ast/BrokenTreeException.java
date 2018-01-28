/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.ast;

/**
 * It is thrown when {@link AbstractSyntaxTreeImpl#toString()} is called for
 * non-empty {@link AbstractSyntaxTreeImpl#NODES}.
 *
 * @author Timur Shaidullin
 */
public class BrokenTreeException extends RuntimeException {
}
