package net.jplugin.core.kernel.impl_incept;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.StringMatcher;
import net.jplugin.core.kernel.api.IMethodFilter;

import java.lang.reflect.Method;
import java.util.Objects;

public class StringMethodFilter implements IMethodFilter {
    StringMatcher methodMatcher;
    public StringMethodFilter(String s){
        if (StringKit.isNotNull(s)){
            this.methodMatcher = new StringMatcher(s);
        }
    }
    @Override
    public boolean match(Method m) {
        return methodMatcher.match(m.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringMethodFilter that = (StringMethodFilter) o;
        return methodMatcher.equals(that.methodMatcher);
    }
}
