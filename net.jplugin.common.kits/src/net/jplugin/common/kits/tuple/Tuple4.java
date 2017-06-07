package net.jplugin.common.kits.tuple;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * 表示有4个元素的元组类型
 * 可迭代
 * 不可变，线程安全
 *
 * @author peiyu
 */
public final class Tuple4<A, B, C, D> extends Tuple {

    public final A first;
    public final B second;
    public final C third;
    public final D fourth;

    private Tuple4(final A first, final B second, final C third, final D fourth) {
        super(first, second, third, fourth);
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> with(final A first, final B second, final C third, final D fourth) {
        return new Tuple4<>(first, second, third, fourth);
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> with(final Map<String, Object> map) {
        requireNonNull(map, "map is null");
        if (map.size() != 4)
            throw new IllegalArgumentException("map's size != 4");
        return new Tuple4((A) map.get("first"), (B) map.get("second"), (C) map.get("third"), (D) map.get("fourth"));
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> with(final List<Object> list) {
        requireNonNull(list, "list is null");
        if (list.size() != 4)
            throw new IllegalArgumentException("list's size != 4");
        return new Tuple4((A) list.get(0), (B) list.get(1), (C) list.get(2), (D) list.get(3));
    }
}
