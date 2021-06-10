package DataAccess.SearchTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// An implementation of a 16-ary tree. The tree is always full for simplicity.

class TreeNode {

    public final static int maxChildren = 16;

    private final long key;

    private final int depth;

    // children stored as linked list
    private TreeNode firstChild = null;
    private TreeNode lastChild = null;

    // next sibling
    private TreeNode nextSibling = null;

    private int numberChildren = 0;

    public TreeNode(long key, int depth) {
        this.key = key;
        this.depth = depth;
    }

    public TreeNode getChild(int index) {
        if (index >= numberChildren)
            return null;
        TreeNode currentNode = firstChild;
        while (index > 0) {
            index--;
            currentNode = currentNode.nextSibling;
        }
        return currentNode;
    }

    public boolean hasRightSibling() {
        return nextSibling != null;
    }

    public boolean setChild(int index, TreeNode newChild) {
        if (index > -1 && index < numberChildren && (!newChild.hasRightSibling())) {
            if (index == 0) {
                newChild.nextSibling = firstChild.nextSibling;
                firstChild = newChild;
                return true;
            }
            TreeNode previousNode = firstChild;
            while (index > 1) {
                previousNode = previousNode.nextSibling;
                index--;
            }
            newChild.nextSibling = previousNode.nextSibling.nextSibling;
            previousNode.nextSibling = newChild;
            return true;
        }
        return false;
    }

    public boolean addChild(TreeNode child) {
        if (numberChildren == maxChildren)
            return false;
        else {
            if (firstChild == null) {
                firstChild = child;
            } else {
                lastChild.nextSibling = child;
            }
            lastChild = child;
            numberChildren++;
            return true;
        }
    }

    public long getKey() {
        return this.key;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getNumberChildren() {
        return this.numberChildren;
    }

    public TreeNode getNextSibling() {
        return this.nextSibling;
    }

    public void print() {
        System.out.println("{key:" + key + "," + "depth:" + depth + "}");
    }

    public boolean isLeaf() {
        return firstChild == null;
    }

}


public class Tree {

    private TreeNode root;

    public final int height;

    private long numberNodes;

    private Tree(int height) {
        this.height = height;
        this.root = new TreeNode(0, 0);
    }

    public TreeNode getRoot() {
        return root;
    }

    public long getNumberNodes() {
        return numberNodes;
    }

    public void printTree() {
        Queue<TreeNode> bfsQueue = new LinkedList<TreeNode>();
        bfsQueue.add(root);
        TreeNode currentTreeNode;
        while (!bfsQueue.isEmpty()) {
            currentTreeNode = bfsQueue.poll();
            currentTreeNode.print();
            if (!currentTreeNode.isLeaf()) {
                for (TreeNode childNode =
                        currentTreeNode.getChild(0); childNode != null; childNode =
                                childNode.getNextSibling()) {
                    bfsQueue.add(childNode);
                }
            }
        }
        System.out.println("tree height is: " + height);
    }

    // Tree builder builds the tree in a BF manner
    public static class TreeBuilder {

        long currentKey = 0;

        Queue<TreeNode> bfsQueue = new LinkedList<TreeNode>();

        public void init() {
            bfsQueue.clear();
            currentKey = 0;
        }

        public Tree build(int height) {
            Tree tree = new Tree(height);
            TreeNode currentTreeNode;
            bfsQueue.add(tree.root);
            while (!bfsQueue.isEmpty()) {
                currentTreeNode = bfsQueue.poll();
                if (currentTreeNode.getDepth() < height) {
                    for (int i = 0; i < TreeNode.maxChildren; i++) {
                        TreeNode childNode =
                                new TreeNode(++currentKey, currentTreeNode.getDepth() + 1);
                        currentTreeNode.addChild(childNode);
                        if (childNode.getDepth() < height)
                            bfsQueue.add(childNode);
                    }
                }
            }
            tree.numberNodes = currentKey + 1;
            return tree;
        }

        public TreeNode cloneSubTree(TreeNode subTreeRoot) {
            TreeNode newSubTreeRoot = new TreeNode(subTreeRoot.getKey(), subTreeRoot.getDepth());
            // clone the sub tree in BF manner
            Queue<TreeNode> queue = new LinkedList<TreeNode>();
            queue.offer(subTreeRoot);
            HashMap<TreeNode, TreeNode> hashMap = new HashMap<TreeNode, TreeNode>();
            hashMap.put(subTreeRoot, newSubTreeRoot);
            while (!queue.isEmpty()) {
                TreeNode curr = queue.poll();
                TreeNode newCurr = hashMap.get(curr);
                for (TreeNode node = curr.getChild(0); node != null; node = node.getNextSibling()) {
                    TreeNode newNode = new TreeNode(node.getKey(), node.getDepth());
                    hashMap.put(node, newNode);
                    newCurr.addChild(newNode);
                    queue.add(node);
                }
            }
            return newSubTreeRoot;
        }

    }
}
