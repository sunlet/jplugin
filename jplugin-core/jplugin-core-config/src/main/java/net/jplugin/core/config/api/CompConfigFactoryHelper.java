package net.jplugin.core.config.api;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.compositeapp.ComponentModeConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <PRE>
 * 调用这些方法的时候，如果没有对应的compositeapp配置，就会抛出异常。
 *
 * 这个类的实现实现规则，绝大部分方法都是支持两种方法推理 appcode的：
 * 1）通过把@XXXX 加到path前面
 * 2）通过调用堆栈上下文推理
 *
 * 但是有两个例外的方法：
 * 一个是 getGroups()，只通过堆栈推理
 * 另一个是 getGroups(appcode)，不用推理，appcode直接传入了。
 * </PRE>
 */
class CompConfigFactoryHelper {
    private static final String EXPINFO = "Can't inference composite app info , ";

    public static String getStringConfig(String path, String def){
        String val = _getStringConfig(path);
        if (StringKit.isNull(val)) return def;
        else return val;
    }
    public static String getStringConfig(String path){
        return getStringConfig(path,null);
    }
    public static String getStringConfigWithTrim(String path){
        String v = getStringConfig(path,null);
        if (v!=null)
            v = v.trim();
        return v;
    }
    public static Long getLongConfig(String path,Long def){
        String val = _getStringConfig(path);
        if (StringKit.isNull(val)) return def;
        else return Long.parseLong(val);
    }

    public static Long getLongConfig(String path){
        return getLongConfig(path,null);
    }

    public static Integer getIntConfig(String path,Integer def){
        String val = _getStringConfig(path);
        if (StringKit.isNull(val)) return def;
        return Integer.parseInt(val);
    }

    public static Integer getIntConfig(String path){
        return getIntConfig(path,null);
    }

    public static Set<String> getGroups(){
        return _getGroups();
    }

    public static Set<String> getGroups(String appcode){
        return _getGroups(appcode);
    }

    public static Map<String,String> getStringConfigInGroup(String group){
        return _getStringConfigInGroup(group);
    }



    /**
     * =================================================================================================
     * <pre>
     *     以下方法才是真正内部执行的方法，代理给ConfigFactory4Default、代理给ConfigFactory4CompositeApp。
     *     核心逻辑在这些方法里面。
     * </pre>
     */

    private static Map<String, IConfigProvidor> map;

    private static String _getStringConfig(String path){
        Tuple2<String,String> info = tryInferenceCompositeAppInfo(path);
        if (info==null)
            throw new RuntimeException( EXPINFO + path);

        checkExists(info.first);
        return map.get(info.first).getConfigValue(info.second);
    }

    private static Map<String,String> _getStringConfigInGroup(String group){
        Tuple2<String,String> info = tryInferenceCompositeAppInfo(group);
        if (info==null)
            throw new RuntimeException( EXPINFO + group);

        checkExists(info.first);
        return map.get(info.first).getStringConfigInGroup(info.second);
    }

    private static Set<String>  _getGroups(){
        //推理appcode，然后查询；
        Tuple2<String,String> info = tryInferenceCompositeAppInfo("");//这里不需要path参数的，借用这个方法，免得重写一个了。
        if (info==null)
            throw new RuntimeException( EXPINFO + "");

        checkExists(info.first);
        return map.get(info.first).getGroups();
    }


    private static Set<String>  _getGroups(String appcode){
        //直接用appcode查询，不做任何推理
        checkExists(appcode);
        return map.get(appcode).getGroups();
    }

    /**
     * <PRE>
     * 在组合APP打开的情况下
     * 1.如果是@开头，根据字符串截取appcode
     * 2.根据调用堆栈推理appcode
     * 3.以上两个都不满足，返回null
     * </PRE>
     * @param path
     * @return
     */
    private static Tuple2<String,String> tryInferenceCompositeAppInfo(String path) {

        if (path.startsWith("@")){
            int pos = path.indexOf('/');
            if (pos<0) throw new RuntimeException("error config path:"+path);
            return Tuple2.with(path.substring(1, pos),path.substring(pos));
        }else{
            String appcode = ComponentModeConfig.getCurrThreadAppCode();
            if (StringKit.isNotNull(appcode))
                return Tuple2.with(appcode,path);
        }
        //以上规则都没有命中

        return null;
    }

    private static void checkExists(String appcode) {
        if (!map.containsKey(appcode)){
            throw new RuntimeException("not found:"+appcode);
        }
    }

    /**
     * =======================================================================
     * 设置配置Providor
     * @param aMap
     */
    public static void _setComponentConfigProvidor(Map<String, IConfigProvidor> aMap) {
        map = new HashMap<>();
        map.putAll(aMap);
    }
}
