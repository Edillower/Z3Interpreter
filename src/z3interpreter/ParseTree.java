package z3interpreter;
import java.util.*;
class ParseTree {
    String kind;
    String attributes;
    List<ParseTree> children = new LinkedList<ParseTree>();

    ParseTree(String nodeKind, String attr) {
        this.kind = nodeKind;
        this.attributes = attr;
        this.children = new LinkedList<ParseTree>();
    }

    void addChild(ParseTree child) {
        this.children.add(this.children.size(), child);
    }

}
