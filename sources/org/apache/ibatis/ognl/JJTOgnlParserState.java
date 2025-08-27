package org.apache.ibatis.ognl;

import java.util.ArrayList;
import java.util.List;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/JJTOgnlParserState.class */
public class JJTOgnlParserState {
    private List nodes = new ArrayList();
    private List marks = new ArrayList();
    private int sp = 0;
    private int mk = 0;
    private boolean node_created;

    public boolean nodeCreated() {
        return this.node_created;
    }

    public void reset() {
        this.nodes.clear();
        this.marks.clear();
        this.sp = 0;
        this.mk = 0;
    }

    public Node rootNode() {
        return (Node) this.nodes.get(0);
    }

    public void pushNode(Node n) {
        this.nodes.add(n);
        this.sp++;
    }

    public Node popNode() {
        int i = this.sp - 1;
        this.sp = i;
        if (i < this.mk) {
            this.mk = ((Integer) this.marks.remove(this.marks.size() - 1)).intValue();
        }
        return (Node) this.nodes.remove(this.nodes.size() - 1);
    }

    public Node peekNode() {
        return (Node) this.nodes.get(this.nodes.size() - 1);
    }

    public int nodeArity() {
        return this.sp - this.mk;
    }

    public void clearNodeScope(Node n) {
        while (this.sp > this.mk) {
            popNode();
        }
        this.mk = ((Integer) this.marks.remove(this.marks.size() - 1)).intValue();
    }

    public void openNodeScope(Node n) {
        this.marks.add(new Integer(this.mk));
        this.mk = this.sp;
        n.jjtOpen();
    }

    public void closeNodeScope(Node n, int num) {
        this.mk = ((Integer) this.marks.remove(this.marks.size() - 1)).intValue();
        while (true) {
            int i = num;
            num--;
            if (i > 0) {
                Node c = popNode();
                c.jjtSetParent(n);
                n.jjtAddChild(c, num);
            } else {
                n.jjtClose();
                pushNode(n);
                this.node_created = true;
                return;
            }
        }
    }

    public void closeNodeScope(Node n, boolean condition) {
        if (condition) {
            int a = nodeArity();
            this.mk = ((Integer) this.marks.remove(this.marks.size() - 1)).intValue();
            while (true) {
                int i = a;
                a--;
                if (i > 0) {
                    Node c = popNode();
                    c.jjtSetParent(n);
                    n.jjtAddChild(c, a);
                } else {
                    n.jjtClose();
                    pushNode(n);
                    this.node_created = true;
                    return;
                }
            }
        } else {
            this.mk = ((Integer) this.marks.remove(this.marks.size() - 1)).intValue();
            this.node_created = false;
        }
    }
}
