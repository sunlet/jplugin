package net.jplugin.core.kernel.impl_incept;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.IExtensionFactory;

public class ExtensionInterceptorFactory implements IExtensionFactory {


    private Class implClazz;

    @Override
    public Object create(Extension extension) {
        try {
            return this.implClazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class getAccessClass() {
        return this.implClazz;
    }

    @Override
    public boolean contentEqual(IExtensionFactory f) {
        if (f instanceof ExtensionInterceptorFactory){
            ExtensionInterceptorFactory ff = (ExtensionInterceptorFactory) f;
            return eqOrAllNull(forExtensions,ff.forExtensions) 
                    && eqOrAllNull(forPoints,ff.forPoints)
                    && eqOrAllNull(methodFilter,ff.methodFilter)
                    && this.implClazz.equals( ((ExtensionInterceptorFactory)f).implClazz);
        }else{
            return false;
        }
    }

    private boolean eqOrAllNull(String a, String b) {
        return (a==null && b==null) || (a!=null && a.equals(b));
    }


    public static IExtensionFactory create(Class implClazz,String forExt,String forP,String m){
        valid(forExt,forP,m);

        ExtensionInterceptorFactory f = new ExtensionInterceptorFactory();
        f.forExtensions = forExt;
        f.forPoints = forP;
        f.methodFilter = m;
        f.implClazz = implClazz;

        f.forExtensionsArr = f.forExtensions==null? EMPTYARR:StringKit.splitStr(f.forExtensions,",");
        f.forPointsArr = f.forPoints==null? EMPTYARR:StringKit.splitStr(f.forPoints,",");
        return f;
    }

    private static void valid(String forExt, String forP, String m) {
        if (StringKit.isNull(forExt) && StringKit.isNull(forP)){
            throw new RuntimeException("forExtensions and forPoint all null");
        }

    }

    String forExtensions;
    String forPoints;
    String methodFilter;

    public static String[] EMPTYARR = new String[]{};
    private String[] forExtensionsArr;
    private String[] forPointsArr;

    public boolean matchExtension(Extension e){
        for (String s:forExtensionsArr){
            if (s.equals(e.getId())) return true;
        }
        for (String s:forPointsArr){
            if (s.equals(e.getExtensionPointName())) return true;
        }
        return false;
    }
}
