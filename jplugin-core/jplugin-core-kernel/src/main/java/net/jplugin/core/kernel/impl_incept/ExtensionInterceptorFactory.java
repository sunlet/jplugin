package net.jplugin.core.kernel.impl_incept;

import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.StringMatcher;
import net.jplugin.core.kernel.api.AbstractExtensionInterceptor;
import net.jplugin.core.kernel.api.Extension;
import net.jplugin.core.kernel.api.IExtensionFactory;
import net.jplugin.core.kernel.api.IMethodFilter;

import java.lang.reflect.Method;

public class ExtensionInterceptorFactory implements IExtensionFactory {


    private Class implClazz;
    private IMethodFilter methodFilter;
    private boolean preCheckNeedIncept;

    @Override
    public Object create(Extension extension) {
        try {
            AbstractExtensionInterceptor o = (AbstractExtensionInterceptor)this.implClazz.newInstance();
            o.setMethodFilter(methodFilter);
            return o;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class getImplClass() {
        return this.implClazz;
    }

    @Override
    public boolean contentEqual(IExtensionFactory f) {
        if (f instanceof ExtensionInterceptorFactory){
            ExtensionInterceptorFactory ff = (ExtensionInterceptorFactory) f;
            return eqOrAllNull(forExtensions,ff.forExtensions)
                    && eqOrAllNull(forPoints,ff.forPoints)
                    && eqOrAllNull(forImplClasses,ff.forImplClasses)
                    && eqOrAllNull(methodFilter,ff.methodFilter)
                    && this.implClazz.equals( ((ExtensionInterceptorFactory)f).implClazz);
        }else{
            return false;
        }
    }

    private boolean eqOrAllNull(Object a, Object b) {
        return (a==null && b==null) || (a!=null && a.equals(b));
    }


    public static IExtensionFactory create(Class implClazz, String forExt, String forP, String forImplClasses, IMethodFilter mf, boolean preCheck){
//        valid(forExt,forP,forImplClasses,m);

        ExtensionInterceptorFactory f = new ExtensionInterceptorFactory();
        f.forExtensions = forExt;
        f.forPoints = forP;
        f.methodFilter = mf;
        f.implClazz = implClazz;
        f.forImplClasses = forImplClasses;
        f.preCheckNeedIncept = preCheck;

//        f.forExtensionsArr = StringKit.isNull(f.forExtensions)? EMPTYARR:StringKit.splitStr(f.forExtensions,",");
//        f.forPointsArr = StringKit.isNull(f.forPoints)? EMPTYARR:StringKit.splitStr(f.forPoints,",");

        //验证不能包含星号
        AssertKit.assertTrue(f.forExtensions==null || f.forExtensions.indexOf("*")<0);
        AssertKit.assertTrue(f.forPoints==null || f.forPoints.indexOf("*")<0);
        AssertKit.assertTrue(f.forImplClasses==null || f.forImplClasses.indexOf("*")<0);

        f.forExtensionsMatcher = StringKit.isNull(f.forExtensions)? null:new StringMatcher(f.forExtensions);
        f.forPointsMatcher = StringKit.isNull(f.forPoints)? null:new StringMatcher(f.forPoints);
        f.forImplClassesMatcher = StringKit.isNull(f.forImplClasses)? null:new StringMatcher(f.forImplClasses);
        return f;
    }


    String forExtensions;
    String forPoints;
    String forImplClasses;


    private StringMatcher forExtensionsMatcher;
    private StringMatcher forPointsMatcher;
    private StringMatcher forImplClassesMatcher;


    public boolean matchExtension(Extension e){
        if (matchClass(e)) {
            if (!preCheckNeedIncept) {
                //在检查Extension匹配性时不需要预检查Method，直接返回
                return true;
            }else {
                //进行method预检
                return matchMethods(e) ? true : false;
            }
        }else {
            return false;
        }
    }

    /**
     * 如果有一个方法匹配就返回true，全部不匹配返回false
     * @param e
     * @return
     */
    private boolean matchMethods(Extension e) {
        Class c = e.getClazz();
        Method[] methods = c.getMethods();
        for (int i=0;i<methods.length;i++){
            if (methodFilter.match(methods[i]))
                return true;
        }
        return false;
    }

    private boolean matchClass(Extension e) {
        String temp = e.getId();
        if (StringKit.isNotNull(temp) && this.forExtensionsMatcher!=null && this.forExtensionsMatcher.match(temp))
            return true;

        temp = e.getExtensionPointName();
        if (StringKit.isNotNull(temp) && this.forPointsMatcher!=null && this.forPointsMatcher.match(temp))
            return true;

        temp = e.getClazz().getName();
        if (StringKit.isNotNull(temp) && this.forImplClassesMatcher!=null && this.forImplClassesMatcher.match(temp))
            return true;
        return false;
    }
}
