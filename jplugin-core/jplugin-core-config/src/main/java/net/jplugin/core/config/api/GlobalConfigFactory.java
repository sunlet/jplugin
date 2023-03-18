package net.jplugin.core.config.api;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.kernel.api.PluginEnvirement;

import java.util.*;

/**
 * 跨应用的全局配置，在分布式情况下生效
 */
public class GlobalConfigFactory {
    private static IConfigProvidor remoteConfigProvidor=new EmptyProvidor();
    private static final String DEFAULT_GROUP="DEFAULT_GROUP";

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

    public static String getValueInDefaultGroup(String path,String def){
        return getValue(DEFAULT_GROUP+'.'+path,def);
    }
    public static String getValueInDefaultGroup(String path){
        return getValueInDefaultGroup(path,null);
    }

    public static String getValue(String path,String def){
        String val = _getStringConfig(path);
        if (StringKit.isNull(val)) return def;
        else return val;
    }
    public static String getValue(String path){
        return getValue(path,null);
    }
    public static String getValueWithTrim(String path){
        String v = getValue(path,null);
        if (v!=null)
            v = v.trim();
        return v;
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
