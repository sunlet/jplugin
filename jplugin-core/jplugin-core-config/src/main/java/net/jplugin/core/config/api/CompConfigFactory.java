package net.jplugin.core.config.api;

import net.jplugin.core.config.comp.CompConfigFactoryHelper;

import java.util.Map;
import java.util.Set;

/**
 * 组合应用中获取配置，此类实现简单的路由功能。
 */
public class CompConfigFactory {

    public static String getStringConfig(String path,String def){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getStringConfig(path,def);
        }else{
            return CompConfigFactoryHelper.getStringConfig(path,def);
        }
    }

    public static String getStringConfig(String path){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getStringConfig(path);
        }else{
            return CompConfigFactoryHelper.getStringConfig(path);
        }
    }

    public static String getStringConfigWithTrim(String path){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getStringConfigWithTrim(path);
        }else{
            return CompConfigFactoryHelper.getStringConfigWithTrim(path);
        }
    }

    public static Long getLongConfig(String path,Long def){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getLongConfig(path,def);
        }else{
            return CompConfigFactoryHelper.getLongConfig(path,def);
        }
    }

    public static Long getLongConfig(String path){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getLongConfig(path);
        }else{
            return CompConfigFactoryHelper.getLongConfig(path);
        }
    }


    public static Integer getIntConfig(String path,Integer def){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getIntConfig(path,def);
        }else{
            return CompConfigFactoryHelper.getIntConfig(path,def);
        }
    }

    public static Integer getIntConfig(String path){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getIntConfig(path);
        }else{
            return CompConfigFactoryHelper.getIntConfig(path);
        }
    }

    public static Set<String> getGroupsOfComponent(){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getGroups();
        }else{
            return CompConfigFactoryHelper.getGroups();
        }
    }

    public static Map<String,String> getStringConfigInGroup(String group){
        if (!CloudEnvironment.isUseComponentMode()){
            return ConfigFactory.getStringConfigInGroup(group);
        }else{
            return CompConfigFactoryHelper.getStringConfigInGroup(group);
        }
    }

    /**
     * 这个方法比较特殊，只能在组合应用情况被调用
     * @param appcode
     * @return
     */
    public static Set<String> getGroupsOfComponent(String appcode){
        if (!CloudEnvironment.isUseComponentMode()){
            throw new RuntimeException("Not support when COMPOSITE APP  is disnabled!");
        }else{
            return CompConfigFactoryHelper.getGroups(appcode);
        }
    }


    /**
     * 设置组合应用配置的提供者
     * @param aMap
     */
    public static void _setComponentConfigProvidor(Map<String, IConfigProvidor> aMap) {
        CompConfigFactoryHelper._setComponentConfigProvidor(aMap);
    }
}
