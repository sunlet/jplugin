package net.jplugin.core.kernel.impl_incept;

import net.jplugin.core.kernel.api.IMethodFilter;

import java.lang.reflect.Method;
import java.util.Objects;

public class AnnotationMethodFilter implements IMethodFilter {
    Class annoClass;
    public AnnotationMethodFilter(Class c){
        this.annoClass = c;
    }
    @Override
    public boolean match(Method m) {
        return m.getAnnotation(annoClass)!=null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationMethodFilter that = (AnnotationMethodFilter) o;
        return Objects.equals(annoClass, that.annoClass);
    }

}