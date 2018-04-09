/*
 *  Queryman. Java tools for working with queries of PostgreSQL database.
 *
 *  License: MIT License.
 *  To see license follow by http://queryman.org/license.txt
 */
package org.queryman.builder.command.clause;

import org.queryman.builder.Keywords;
import org.queryman.builder.ast.AbstractSyntaxTree;
import org.queryman.builder.ast.AstVisitor;
import org.queryman.builder.ast.NodeMetadata;
import org.queryman.builder.ast.NodesMetadata;
import org.queryman.builder.token.Expression;
import org.queryman.builder.token.Keyword;

import static org.queryman.builder.ast.NodesMetadata.EMPTY;
import static org.queryman.builder.ast.NodesMetadata.OF;

/**
 * It's used by SELECT locking clauses.
 *
 * @author Timur Shaidullin
 */
public class Locking implements AstVisitor {
    private       Expression[] tables;
    private final LockingMode  lockingMode;
    private       WaitingMode  waitingMode;

    public static Locking forUpdate() {
        return new Locking(LockingMode.FOR_UPDATE);
    }

    public static Locking forNoKeyUpdate() {
        return new Locking(LockingMode.FOR_NO_KEY_UPDATE);
    }

    public static Locking forShare() {
        return new Locking(LockingMode.FOR_SHARE);
    }

    public static Locking forKeyShare() {
        return new Locking(LockingMode.FOR_KEY_SHARE);
    }

    private Locking(LockingMode mode) {
        this.lockingMode = mode;
    }

    public void setTables(Expression... tables) {
        this.tables = tables;
    }

    public void noWait() {
        this.waitingMode = WaitingMode.NOWAIT;
    }

    public void skipLocked() {
        this.waitingMode = WaitingMode.SKIP_LOCKED;
    }

    @Override
    public void assemble(AbstractSyntaxTree tree) {
        tree.startNode(lockingMode.getMetadata());

        if (tables != null && tables.length > 0)
            tree.startNode(OF, ", ")
               .addLeaves(tables)
               .endNode();

        if (waitingMode != null)
            tree.startNode(EMPTY)
               .addLeaf(waitingMode.getKeyword())
               .endNode();

        tree.endNode();
    }

    public enum LockingMode {
        FOR_UPDATE(NodesMetadata.FOR_UPDATE),
        FOR_NO_KEY_UPDATE(NodesMetadata.FOR_NO_KEY_UPDATE),
        FOR_SHARE(NodesMetadata.FOR_SHARE),
        FOR_KEY_SHARE(NodesMetadata.FOR_KEY_SHARE);

        private final NodeMetadata metadata;

        LockingMode(NodeMetadata metadata) {
            this.metadata = metadata;
        }

        NodeMetadata getMetadata() {
            return metadata;
        }
    }

    public enum WaitingMode {
        NOWAIT(Keywords.NOWAIT),
        SKIP_LOCKED(Keywords.SKIP_LOCKED);

        private final Keyword keyword;

        WaitingMode(Keyword keyword) {
            this.keyword = keyword;
        }

        Keyword getKeyword() {
            return keyword;
        }
    }
}
