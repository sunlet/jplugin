package net.jplugin.common.kits.tuple;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * 表示有2个元素的元组类型
 * 可迭代
 * 不可变，线程安全
 *
 * @author peiyu
 */
public final class Tuple2<A, B> extends Tuple {

    public final A first;
    public final B second;

    private Tuple2(final A first, final B second) {
        super(first, second);
        this.first = first;
        this.second = second;
    }

    public String toString(){
        return "[ "+ first+","+second+" ]";
    }

    public static <A, B> Tuple2<A, B> with(final A first, final B second) {
        return new Tuple2<>(first, second);
    }

    public static <A, B> Tuple2<A, B> with(final Map<String, Object> map) {
        requireNonNull(map, "map is null");
        if (map.size() != 2)
            throw new IllegalArgumentException("map's size != 2");
        return new Tuple2<>((A) map.get("first"), (B) map.get("second"));
    }

    public static <A, B> Tuple2<A, B> with(final List<Object> list) {
        requireNonNull(list, "list is null");
        if (list.size() != 2)
            throw new IllegalArgumentException("list's size != 2");
        return new Tuple2<>((A) list.get(0), (B) list.get(1));
    }
}
