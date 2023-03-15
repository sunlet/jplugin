package net.jplugin.core.config.api;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

import java.util.*;

/**
 * 跨应用的全局配置，在分布式情况下生效
 */
public class GlobalConfigFactory {
    private static IConfigProvidor remoteConfigProvidor=new EmptyProvidor();


    static class EmptyProvidor implements IConfigProvidor{

        @Override
        public String getConfigValue(String path) {
            return null;
        }

        @Override
        public boolean containsConfig(String path) {
            return false;
        }

        @Override
        public Map<String, String> getStringConfigInGroup(String group) {
            return new HashMap<>();
        }

        @Override
        public Set<String> getGroups() {
            return new HashSet<>();
        }
    }

    public static String getStringConfig(String path,String def){
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
       return Collections.unmodifiableSet(remoteConfigProvidor.getGroups());
    }

    /**
     */
    public static Map<String,String> getStringConfigInGroup(String group){
        return  remoteConfigProvidor.getStringConfigInGroup(group);
    }


    private static String _getStringConfig(String path){
        return remoteConfigProvidor.getConfigValue(path);
    }

    public static void _setRemoteConfigProvidor(IConfigProvidor repo) {
        remoteConfigProvidor = repo;
        PluginEnvirement.INSTANCE.getStartLogger().log("$$$ Global Configigure Providor init:"+repo.getClass().getName());
    }
}
