package net.jplugin.common.kits.tuple;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * 表示有N个元素的元组类型
 * 可迭代
 * 不可变，线程安全
 *
 * @author peiyu
 */
public final class TupleN extends Tuple {

    private TupleN(final Object... args) {
        super(args);
    }

    public static TupleN with(final Object... args) {
        requireNonNull(args, "args is null");
        return new TupleN(args);
    }

    public static TupleN withMap(final Map<String, Object> map) {
        requireNonNull(map, "map is null");
        return TupleN.with(map.values().toArray());
    }

    public static TupleN withList(final List<Object> list) {
        requireNonNull(list, "list is null");
        return TupleN.with(list.toArray());
    }
}
