package net.jplugin.core.kernel.api;

import java.lang.reflect.Method;

public interface IMethodFilter {
    boolean match(Method m);
}
