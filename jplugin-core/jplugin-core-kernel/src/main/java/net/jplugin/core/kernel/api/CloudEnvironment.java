package net.jplugin.core.kernel.api;


import net.jplugin.common.kits.AssertKit;
import net.jplugin.common.kits.StringKit;

import java.util.HashMap;
import java.util.Map;

/**
 * 云服务环境的配置信息。云服务环境的注册中心、配置中心依赖于nacos。
 */
public class CloudEnvironment {
    public static CloudEnvironment INSTANCE = new CloudEnvironment();
    public static final String NACOS_URL = "nacosUrl";
    public static final String SERVICE_CODES = "serviceCodes";
    public static final String APP_CODE = "appCode";

    private String nacosUrl;
    private String appCode;
    private String[] serviceCodes;

    private boolean inited=false;

    private CloudEnvironment(){}

    public String getNacosUrl() {
        checkInit();
        return nacosUrl;
    }

    public String getAppCode() {
        checkInit();
        return appCode;
    }

    public String[] getServiceCodes() {
        checkInit();
        return serviceCodes;
    }

    private void checkInit() {
        if (!inited){
            throw new RuntimeException("init not called");
        }
    }

    public void init(Map<String,String> map){
        if (inited)
            throw new RuntimeException("init can't call twice");
        AssertKit.assertStringNotNull(map.get(NACOS_URL),NACOS_URL);
        AssertKit.assertStringNotNull(map.get(APP_CODE), APP_CODE);
        AssertKit.assertStringNotNull(map.get(SERVICE_CODES),SERVICE_CODES);

        nacosUrl = map.get(NACOS_URL).trim();
        appCode = map.get(APP_CODE).trim();
        serviceCodes = StringKit.splitStr(map.get(SERVICE_CODES).trim(),",");

        PluginEnvirement.getInstance().getStartLogger().log("CloudEnvironment Init: "+nacosUrl+", "+appCode+", "+arrToString(serviceCodes));
        inited = true;
    }

    private String arrToString(String[] serviceCodes) {
        StringBuffer sb = new StringBuffer();
        for (String s:serviceCodes){
            sb.append(s).append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put(NACOS_URL,"url1");
        map.put(APP_CODE,"appcode1");
        map.put(SERVICE_CODES,"S1,S2");
        CloudEnvironment.INSTANCE.init(map);

        System.out.println(CloudEnvironment.INSTANCE.getAppCode());
        System.out.println(CloudEnvironment.INSTANCE.getNacosUrl());
        System.out.println(CloudEnvironment.INSTANCE.getServiceCodes().length);

    }
}
