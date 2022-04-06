package it.matlice.ingsw.tree;

import java.util.LinkedList;
import java.util.Stack;

public class TreeNode<T> implements Node<T> {
    private final LinkedList<TreeNode<T>> children = new LinkedList<>();
    private T data;
    private TreeNode<T> parent;

    public TreeNode(T data) {
        this.data = data;
    }

    /**
     * Adds a child to the node
     *
     * @param child the data of the new child
     * @return this
     */
    @Override
    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public Node<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public TreeNode<T> getParent() {
        return parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TreeNode<T>[] getChildren() {
        return children.toArray(new TreeNode[]{});
    }

    /**
     * Returns all node's children data
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getChildrenData() {
        return (T[]) children.stream().map(TreeNode::getData).toArray();
    }

    /**
     * traverses the tree layer by layer
     *
     * @param action the action to be performed on each node
     * @see Node#BFT(TreeAction)
     * @deprecated since 1.5
     */
    @Override
    public void traverse_linear(TreeAction<T> action) {
        this.BFT(action);
    }

    /**
     * traverses the tree recursively
     *
     * @param action the action to be performed on each node
     * @see Node#DFT(TreeAction)
     * @deprecated since 1.5
     */
    @Override
    public void traverse(TreeAction<T> action) {
        this.DFT(action);
    }

    /**
     * traverses the tree layer by layer, bottom first
     *
     * @param action the action to be performed on each node
     * @see Node#BFT(TreeAction)
     * @deprecated since 1.5
     */
    @Override

    public void traverse_reverse_linear(TreeAction<T> action) {
        this.reverse_BFT(action);
    }

    /**
     * Breadth First Traverse
     * Executes an action on each node traversing the tree prioritizing childs first, then childs of childes...
     *
     * @param action the action to be performed
     */
    @Override
    public void BFT(TreeAction<T> action) {
        LinkedList<TreeNode<T>> nodes = new LinkedList<>();
        nodes.add(this);
        while (nodes.size() > 0) {
            var n = nodes.removeLast();
            action.nodeAction(n);
            for (TreeNode<T> child : n.getChildren())
                nodes.addFirst(child);
        }
    }

    /**
     * Branch First Traverse reversed order
     * Executes an action on each node traversing the tree prioritizing childs first, then childs of childes starting from leafes...
     *
     * @param action the action to be performed
     */
    @Override
    public void reverse_BFT(TreeAction<T> action) {
        Stack<TreeNode<T>> nodes = new Stack<>();
        this.BFT((r) -> nodes.push((TreeNode<T>) r));
        while (!nodes.empty()) action.nodeAction(nodes.pop());
    }

    /**
     * Depth First Traverse
     * Executes an action on each node traversing the tree prioritizing branches
     *
     * @param action the action to be performed
     */
    @Override
    public void DFT(TreeAction<T> action) {
        for (TreeNode<T> elm : this.children) elm.DFT(action);
    }
}