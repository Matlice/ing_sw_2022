package it.matlice.ingsw.tree;

public interface Node<T> {

    /**
     * Adds a child to the node
     *
     * @param child the data of the new child
     * @return this
     */
    Node<T> addChild(T child);

    T getData();

    Node<T> setData(T data);

    Node<T> getParent();

    Node<T>[] getChildren();

    /**
     * Returns all node's children data
     *
     * @return all node's children data
     */
    T[] getChildrenData();

    /**
     * traverses the tree layer by layer
     *
     * @param action the action to be performed on each node
     * @see Node#BFT(TreeAction)
     * @deprecated since 1.5
     */
    @Deprecated
    void traverse_linear(TreeAction<T> action);

    /**
     * traverses the tree recursively
     *
     * @param action the action to be performed on each node
     * @see Node#DFT(TreeAction)
     * @deprecated since 1.5
     */
    @Deprecated
    void traverse(TreeAction<T> action);

    /**
     * traverses the tree layer by layer, bottom first
     *
     * @param action the action to be performed on each node
     * @see Node#BFT(TreeAction)
     * @deprecated since 1.5
     */
    @Deprecated
    void traverse_reverse_linear(TreeAction<T> action);

    /**
     * Branch First Traverse
     * Executes an action on each node traversing the tree prioritizing childs first, then childs of childes...
     *
     * @param action the action to be performed
     */
    void BFT(TreeAction<T> action);

    /**
     * Breadth First Traverse reversed order
     * Executes an action on each node traversing the tree prioritizing childs first, then childs of childes starting from leafes...
     *
     * @param action the action to be performed
     */
    void reverse_BFT(TreeAction<T> action);

    /**
     * Depth First Traverse
     * Executes an action on each node traversing the tree prioritizing branches
     *
     * @param action the action to be performed
     */
    void DFT(TreeAction<T> action);

}