package it.matlice.ingsw.tree;

import java.util.*;

public class MapNode<T> implements Node<T> {
    private final TreeMap<String, MapNode<T>> children = new TreeMap<>();
    private final HashMap<String, Integer> duplicates = new HashMap<>();
    private T data;
    private MapNode<T> parent;

    public MapNode(T data) {
        this.data = data;
    }

    /**
     * Adds a child to the node
     *
     * @param key   the name of the child
     * @param child the data of the new child
     * @return this
     */
    public MapNode<T> addChild(String key, T child) {
        MapNode<T> childNode = new MapNode<>(child);
        childNode.parent = this;
        if (duplicates.containsKey(key)) {
            this.children.put(key + "&" + duplicates.get(key), childNode);
            duplicates.put(key, duplicates.get(key) + 1);
        } else {
            this.children.put(key, childNode);
            duplicates.put(key, 1);
        }
        return childNode;
    }

    /**
     * Adds a child to the node
     *
     * @param child the data of the new child
     * @return this
     */
    @Override
    public Node<T> addChild(T child) {
        return this.addChild(String.valueOf(children.size()), child);
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
    public MapNode<T> getParent() {
        return parent;
    }


    @Override
    @SuppressWarnings("unchecked")
    public MapNode<T>[] getChildren() {
        return children.values().toArray(new MapNode[]{});
    }

    public TreeMap<String, T> getChildrenMap() {
        var r = new TreeMap<String, T>();
        children.forEach((k, v) -> r.put(k, v.getData()));
        return r;
    }

    /**
     * Returns all node's children data
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getChildrenData() {
        return (T[]) children.values().stream().map(MapNode::getData).toArray();
    }

    /**
     * traverses the tree layer by layer
     *
     * @param action the action to be performed on each node
     * @see Node#BFT(TreeAction)
     * @deprecated since 1.5
     */
    @Override
    @Deprecated
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
    @Deprecated
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
    @Deprecated
    public void traverse_reverse_linear(TreeAction<T> action) {
        this.reverse_BFT(action);
    }

    /**
     * Branch First Traverse
     * Executes an action on each node traversing the tree prioritizing children first, then children of childes...
     *
     * @param action the action to be performed
     */
    @Override
    public void BFT(TreeAction<T> action) {
        LinkedList<MapNode<T>> nodes = new LinkedList<>();
        nodes.add(this);
        while (nodes.size() > 0) {
            var n = nodes.removeLast();
            action.nodeAction(n);
            for (MapNode<T> child : n.getChildren())
                nodes.addFirst(child);
        }
    }

    /**
     * Branch First Traverse reversed order
     * Executes an action on each node traversing the tree prioritizing childs first, then childs of childes starting from leafs...
     *
     * @param action the action to be performed
     */
    @Override
    public void reverse_BFT(TreeAction<T> action) {
        Stack<MapNode<T>> nodes = new Stack<>();
        this.BFT((r) -> nodes.push((MapNode<T>) r));
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
        this.children.forEach((k, v) -> v.DFT(action));
    }

    public MapNode<T> getChildren(String key) {
        return children.get(key);
    }

    public Set<String> childrenSet() {
        return children.keySet();
    }

    @Override
    public String toString() {
        return data.toString();
    }

}
