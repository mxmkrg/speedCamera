package utility;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.NoSuchElementException;

@Data
@SuperBuilder
public abstract class MyStack<T> {
    private final Object[] elements;
    private int top;

    public MyStack(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        this.elements = new Object[capacity];
        this.top = -1;
    }

    public void push(T item) {
        if (isFull()) throw new IllegalStateException("stack overflow");
        elements[++top] = item;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("stack underflow");
        @SuppressWarnings("unchecked") T item;
        item = (T) elements[top];
        elements[top--] = null;
        return item;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == elements.length - 1;
    }

    public int size() {
        return top + 1;
    }
}