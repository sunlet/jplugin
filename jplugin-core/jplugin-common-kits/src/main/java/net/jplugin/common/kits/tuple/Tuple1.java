package net.jplugin.common.kits.tuple;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * 表示有1个元素的元组类型
 * 可迭代
 * 不可变，线程安全
 *
 * @author peiyu
 */
public final class Tuple1<A> extends Tuple {

    public final A first;

    private Tuple1(final A first) {
        super(first);
        this.first = first;
    }

    public static <A> Tuple1<A> with(final A first) {
        return new Tuple1<>(first);
    }

    public static <A> Tuple1<A> with(final Map<String, ? extends A> map) {
        requireNonNull(map, "map is null");
        if (map.size() != 1)
            throw new IllegalArgumentException("map's size != 1");
        return new Tuple1<>(map.get("first"));
    }

    public static <A> Tuple1<A> with(final List<? extends A> list) {
        requireNonNull(list, "list is null");
        if (list.size() != 1)
            throw new IllegalArgumentException("list's size != 1");
        return new Tuple1<>(list.get(0));
    }
}
