package net.jplugin.common.kits.tuple;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * 表示有3个元素的元组类型
 * 可迭代
 * 不可变，线程安全
 *
 * @author peiyu
 */
public final class Tuple3<A, B, C> extends Tuple {

    public final A first;
    public final B second;
    public final C third;

    private Tuple3(final A first, final B second, final C third) {
        super(first, second, third);
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <A, B, C> Tuple3<A, B, C> with(final A first, final B second, final C third) {
        return new Tuple3<>(first, second, third);
    }

    public static <A, B, C> Tuple3<A, B, C> with(final Map<String, Object> map) {
        requireNonNull(map, "map is null");
        if (map.size() != 3)
            throw new IllegalArgumentException("map's size != 3");
        return new Tuple3((A) map.get("first"), (B) map.get("second"), (C) map.get("third"));
    }

    public static <A, B, C> Tuple3<A, B, C> with(final List<Object> list) {
        requireNonNull(list, "list is null");
        if (list.size() != 3)
            throw new IllegalArgumentException("list's size != 3");
        return new Tuple3((A) list.get(0), (B) list.get(1), (C) list.get(2));
    }

}
