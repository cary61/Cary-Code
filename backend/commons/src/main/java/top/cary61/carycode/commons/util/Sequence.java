package top.cary61.carycode.commons.util;


import top.cary61.carycode.commons.entity.Delimiter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * future features: lambda $(), when() works with or() and otherwise()
 */
public class Sequence implements Iterable<String> {

    private final List<String> strings = new ArrayList<>();
    private boolean condition = true;

    private int modifiedCount = 0;

    private String delimiter;
    private UnwrapLevel defaultUnwrapLevel;

    public enum UnwrapLevel {
        NEVER,
        ONCE,
        RECURSIVELY
    }


    public Sequence() {
        this(Delimiter.SINGLE_SPACE, UnwrapLevel.ONCE);
    }

    public Sequence(String delimiter, UnwrapLevel defaultUnwrapLevel) {
        this.delimiter = delimiter;
        this.defaultUnwrapLevel = defaultUnwrapLevel;
    }

    public Sequence(char delimiter, UnwrapLevel defaultUnwrapLevel) {
        this(Character.toString(delimiter), defaultUnwrapLevel);
    }

    public Sequence(String delimiter) {
        this(delimiter, UnwrapLevel.ONCE);
    }

    public Sequence(char delimiter) {
        this(Character.toString(delimiter));
    }

    public Sequence(UnwrapLevel defaultUnwrapLevel) {
        this(Delimiter.EMPTY_STRING, defaultUnwrapLevel);
    }


    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        modifiedCount++;
    }

    public void setDefaultUnwrapLevel(UnwrapLevel defaultUnwrapLevel) {
        this.defaultUnwrapLevel = defaultUnwrapLevel;
    }

    public void setDelimiter(char delimiter) {
        setDelimiter(Character.toString(delimiter));
    }

    public Sequence when(boolean b) {
        condition = b;
        return this;
    }

    public Sequence end() {
        condition = true;
        return this;
    }

    private int lastToStringModifiedCount = -1;
    private String toStringCache = null;
    @Override
    public String toString() {
        if (modifiedCount == lastToStringModifiedCount) {
            return toStringCache;
        }
        lastToStringModifiedCount = modifiedCount;
        return toStringCache = String.join(delimiter, strings);
    }

    private int lastToListModifiedCount = -1;
    private List<String> toListCache = null;
    public List<String> toList() {
        if (modifiedCount == lastToListModifiedCount) {
            return toListCache;
        }
        lastToListModifiedCount = modifiedCount;
        return toListCache = new ArrayList<>(strings);
    }

    public List<String> asList() {
        return Immutable.of(strings);
    }



    // Explicitly Strings
    public Sequence append(String str) {
        if (condition) {
            strings.add(str);
            modifiedCount++;
        }
        return this;
    }
    public Sequence append(CharSequence cs) {
        if (!condition) {
            return this;
        }
        return append(cs.toString());
    }
    public Sequence append(char ch) {
        if (!condition) {
            return this;
        }
        return append(Character.toString(ch));
    }
    public Sequence append(long number) {
        if (!condition) {
            return this;
        }
        return append(Long.toString(number));
    }

    // Default UnwrapLevel
    @SuppressWarnings("rawtypes")
    public Sequence append(Iterable iterable) {
        if (!condition) {
            return this;
        }
        if (defaultUnwrapLevel == UnwrapLevel.NEVER) {
            return appendWithoutUnwrapping(iterable);
        }
        if (defaultUnwrapLevel == UnwrapLevel.ONCE) {
            return appendWithUnwrappingOnce(iterable);
        }
        if (defaultUnwrapLevel == UnwrapLevel.RECURSIVELY) {
            return appendWithUnwrappingRecursively(iterable);
        }
        throw new IllegalStateException();
    }
    public Sequence append(Object... array) {
        if (!condition) {
            return this;
        }
        if (defaultUnwrapLevel == UnwrapLevel.NEVER) {
            return appendWithoutUnwrapping(array);
        }
        if (defaultUnwrapLevel == UnwrapLevel.ONCE) {
            return appendWithUnwrappingOnce(array);
        }
        if (defaultUnwrapLevel == UnwrapLevel.RECURSIVELY) {
            return appendWithUnwrappingRecursively(array);
        }
        throw new IllegalStateException();
    }

    // UnwrapLevel.NEVER
    @SuppressWarnings("rawtypes")
    public Sequence appendWithoutUnwrapping(Iterable iterable) {
        if (!condition) {
            return this;
        }
        strings.add(iterable.toString());
        modifiedCount++;
        return this;
    }
    public Sequence appendWithoutUnwrapping(Object... array) {
        if (!condition) {
            return this;
        }
        strings.add(array.toString());
        modifiedCount++;
        return this;
    }
    // UnwrapLevel.NEVER end



    // UnwrapLevel.ONCE
    @SuppressWarnings("rawtypes")
    public Sequence appendWithUnwrappingOnce(Iterable iterable) {
        if (!condition) {
            return this;
        }
        dfs(iterable, 2);
        modifiedCount++;
        return this;
    }
    public Sequence appendWithUnwrappingOnce(Object... array) {
        if (!condition) {
            return this;
        }
        dfs(array, 2);
        modifiedCount++;
        return this;
    }
    // UnwrapLevel.ONCE end



    // UnwrapLevel.RECURSIVELY
    @SuppressWarnings("rawtypes")
    public Sequence appendWithUnwrappingRecursively(Iterable iterable) {
        if (!condition) {
            return this;
        }
        dfs(iterable, Integer.MAX_VALUE);
        modifiedCount++;
        return this;
    }
    public Sequence appendWithUnwrappingRecursively(Object... array) {
        if (!condition) {
            return this;
        }
        dfs(array, Integer.MAX_VALUE);
        modifiedCount++;
        return this;
    }
    // UnwrapLevel.RECURSIVELY end



    @SuppressWarnings({"rawtypes"})
    private void dfs(Object obj, int maxDepth) {
        if (maxDepth == 0) {
            strings.add(obj.toString());
            return;
        }
        if (obj.getClass().isArray()) {
            for (Object element : (Object[]) obj) {
                dfs(element, maxDepth - 1);
            }
        } else if (obj instanceof Iterable iterable) {
            for (Object element : iterable) {
                dfs(element, maxDepth - 1);
            }
        } else {
            strings.add(obj.toString());
        }
    }

    // ------------------ alias ------------------
    // Explicitly Strings
    public Sequence $(String str) {
        return append(str);
    }
    public Sequence $(char ch) {
        return append(ch);
    }
    public Sequence $(long number) {
        return append(number);
    }

    // Default UnwrapLevel
    @SuppressWarnings("rawtypes")
    public Sequence $(Iterable iterable) {
        return append(iterable);
    }
    public Sequence $(Object... array) {
        return append(array);
    }

    // UnwrapLevel.NEVER
    @SuppressWarnings("rawtypes")
    public Sequence $0(Iterable iterable) {
        return appendWithoutUnwrapping(iterable);
    }
    public Sequence $0(Object... array) {
        return appendWithoutUnwrapping(array);
    }
    // UnwrapLevel.NEVER end

    // UnwrapLevel.ONCE
    @SuppressWarnings("rawtypes")
    public Sequence $1(Iterable iterable) {
        return appendWithUnwrappingOnce(iterable);
    }
    public Sequence $1(Object... array) {
        return appendWithUnwrappingOnce(array);
    }
    // UnwrapLevel.ONCE end

    // UnwrapLevel.RECURSIVELY
    @SuppressWarnings("rawtypes")
    public Sequence $2(Iterable iterable) {
        return appendWithUnwrappingRecursively(iterable);
    }
    public Sequence $2(Object... array) {
        return appendWithUnwrappingRecursively(array);
    }
    // UnwrapLevel.RECURSIVELY end

    // ------------------ alias end ------------------


    @Override
    public Iterator<String> iterator() {
        return new IteratorImpl();
    }

    class IteratorImpl implements Iterator<String> {

        int cursor = 0;
        final int length = strings.size();
        final int oldModifiedCount = modifiedCount;

        @Override
        public boolean hasNext() {
            return cursor < length;
        }

        @Override
        public String next() {
            if (modifiedCount != oldModifiedCount) {
                throw new IllegalStateException();
            }
            return strings.get(cursor++);
        }
    }
}
