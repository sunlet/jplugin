package net.jplugin.core.kernel.api;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ISetAnnotationTransformer {
    Annotation[] getAnnoList(Annotation a);
}
