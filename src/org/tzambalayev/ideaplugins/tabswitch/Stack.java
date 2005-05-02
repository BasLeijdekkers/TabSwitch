package org.tzambalayev.ideaplugins.tabswitch;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * @author Bas Leijdekkers
 */
public class Stack {
    protected Object[] elements;
    protected int size = 0;

    public Stack() {
        this(10);
    }

    public Stack(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be zero or greater");
        }
        elements = new Object[initialCapacity];
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

    protected void ensureCapacity() {
        if (size == elements.length) {
            final Object[] oldelements = elements;
            final int capacity = (elements.length << 1) + 1;
            elements = new Object[capacity];
            System.arraycopy(oldelements, 0, elements, 0, size);
        }
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
        ensureCapacity();
        elements[size++] = element;
    }

    public void remove(Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                final int length = size - (i + 1);
                if (length > 0) {
                    System.arraycopy(elements, i + 1, elements, i, length);
                }
                size--;
                for (int j = 0; j < size; j++) {
                    if (elements[j] == null) {
                        throw new NullPointerException("elements[" + j + "]");
                    }
                }
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
            final Class componentType = array.getClass().getComponentType();
            array = (Object[])java.lang.reflect.Array.newInstance(componentType, size);
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

    public static void main(String[] args) {
        final Stack stack = new Stack(3);
        System.out.println(stack);
        stack.push("1");
        System.out.println(stack);
        stack.push("2");
        System.out.println(stack);
        stack.push("3");
        System.out.println(stack);
        stack.remove("3");
        stack.push("2");
        stack.push("4");
        System.out.println(stack);
        stack.remove("2");
        stack.remove("2");
        stack.remove("1");
        stack.remove("4");
        System.out.println(stack);
    }
}