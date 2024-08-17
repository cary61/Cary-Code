package top.cary61.carycode.commons.util;

import java.util.*;

public class Immutable {

    public static <E> List<E> of(List<E> list) {
        return new ListAdapter<>(list);
    }


    private static UnsupportedOperationException uoe() {
        return new UnsupportedOperationException();
    }

    private static class ListAdapter<E> extends AbstractList<E> {

        private final List<E> list;

        ListAdapter(List<E> list) {
            this.list = list;
        }

        @Override
        public E get(int index) { return list.get(index); }
        @Override
        public int size() { return list.size(); }

        @Override
        public boolean add(E e) { throw uoe(); }
        @Override
        public boolean remove(Object o) { throw uoe(); }
        @Override
        public boolean removeAll(Collection<?> c) { throw uoe(); }
        @Override
        public boolean retainAll(Collection<?> c) { throw uoe(); }
        @Override
        public boolean addAll(Collection<? extends E> c) { throw uoe(); }
        @Override
        public boolean addAll(int index, Collection<? extends E> c) { throw uoe(); }
        @Override
        public void clear() { throw uoe(); }
    }

}
