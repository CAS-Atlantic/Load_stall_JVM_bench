package DataAccess.SparseMatrix;

import java.util.Optional;

class Node<E> {
    private E value;
    private Node<E> next = null;

    public Node(E value) {
        this.value = value;
    }

    public E getValue() {
        return value;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }
}


class List<E extends Comparable<E>> {

    private Node<E> head;

    private Node<E> tail;

    public List() {
        this.head = this.tail = null;
    }

    public List(E head) {
        this.head = new Node<E>(head);
        tail = this.head;
    }

    public Optional<E> get(E e) {
        if (tail == null)
            return Optional.ofNullable(null);
        E elemt = tail.getValue();
        if (e.compareTo(elemt) > 0)
            return Optional.ofNullable(null);
        Node<E> currentNode;
        for (currentNode = head; currentNode != null; currentNode = currentNode.getNext()) {
            elemt = currentNode.getValue();
            if (e.compareTo(elemt) == 0)
                return Optional.of(elemt);
            else if (elemt.compareTo(e) > 0)
                break;
        }
        return Optional.ofNullable(null);
    }

    // e must be guaranteed to be a new element
    public void add(E e) {
        if (head == null) {
            head = new Node<E>(e);
            tail = head;
        } else if (head.getValue().compareTo(e) > 0) {
            Node<E> currentNode = head;
            head = new Node<E>(e);
            head.setNext(currentNode);
            return;
        } else if (tail.getValue().compareTo(e) < 0) {
            tail.setNext(new Node<E>(e));
            tail = tail.getNext();
        } else {
            Node<E> currentNode = head;
            Node<E> currentNextNode = head.getNext();
            while (currentNextNode != null) {
                if (currentNode.getValue().compareTo(e) < 0
                        && currentNextNode.getValue().compareTo(e) > 0) {
                    currentNode.setNext(new Node<E>(e));
                    currentNode.getNext().setNext(currentNextNode);
                    return;
                }
                currentNode = currentNextNode;
                currentNextNode = currentNode.getNext();
            }
        }
    }

    public Node<E> getHead() {
        return head;
    }

    public Node<E> getTail() {
        return tail;
    }
}
