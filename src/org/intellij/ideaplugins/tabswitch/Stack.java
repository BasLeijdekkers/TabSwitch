package org.intellij.ideaplugins.tabswitch;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.EmptyStackException;

public final class Stack {

    private Object[] elements;
    private int size = 0;

    public Stack() {
        this(20);
    }

    public Stack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than zero");
        }
        elements = new Object[capacity];
    }

    public Stack(Stack stack, int capacity) {
        if (stack == null) {
            throw new NullPointerException("stack");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must must be greater than zero");
        }
        elements = new Object[capacity];
        for (int i = 0, iMax = Math.min(capacity, stack.size); i < iMax; i++) {
            push(stack.elements[i]);
        }
    }

    public int capacity() {
        return elements.length;
    }

    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        size--;
        final Object result = elements[size];
        elements[size] = null;
        return result;
    }

    public void push(Object element) {
        if (element == null) {
            throw new NullPointerException("element");
        }
        if (size == elements.length) {
            System.arraycopy(elements, 1, elements, 0, elements.length - 1);
            elements[size - 1] = element;
        } else {
            elements[size] = element;
            size++;
        }
    }

    public void remove(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                final int length = size - (i + 1);
                if (length > 0) {
                    System.arraycopy(elements, i + 1, elements, i, length);
                } else {
                    elements[i] = null;
                }
                size--;
                return;
            }
        }
    }

    public int size() {
        return size;
    }

    public Object top() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        return elements[size - 1];
    }

    public Object[] toArray() {
        final Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }

    public Object[] toArray(Object[] array) {
        if (array.length < size) {
            final Class arrayClass = array.getClass();
            final Class componentType = arrayClass.getComponentType();
            array = (Object[])Array.newInstance(componentType, size);
        }
        System.arraycopy(elements, 0, array, 0, size);
        if (array.length > size) {
            array[size] = null;
        }
        return array;
    }

    public String toString() {
        final StringBuffer result = new StringBuffer(elements.length + "[");
        if (size > 0) {
            Object element = elements[0];
            if (element == this) {
                element = "(this stack)";
            }
            result.append(element);
            for (int i = 1; i < size; i++) {
                result.append(", ");
                element = elements[i];
                if (element == this) {
                    element = "(this stack)";
                }
                result.append(element);
            }
        }
        result.append(']');
        return result.toString();
    }
}