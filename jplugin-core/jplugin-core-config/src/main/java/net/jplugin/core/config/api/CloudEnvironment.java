package net.jplugin.core.config.api;


import net.jplugin.common.kits.*;
import net.jplugin.core.kernel.api.PluginEnvirement;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * <pre>
 * 云服务环境的配置信息。云服务环境的注册中心、配置中心依赖于nacos。
 *
 * 全局变量
 *      存放位置：跨应用的全局变量在public命名空间下面，GLOBAL-CONFIG(dataId)，
 *      访问方式：访问时候使用GlobalConfigFactory 来访问。
 *
 * 应用级共享配置
 *      存放位置：一个应用中各个Module的共享配置，放在应用的命名空间下面，APP-CONFIG(group)下面。
 *      访问方式：这些变量会被融合到Module的访问当中，如果APP-CONFIG 下的group名称和 Module自己的group重合，MOdule自己配置的Group优先。
 *
 * 关于旧版本兼容性的设计：
 *      兼容旧版本的AppEnvirement.getGlobalVar(varname）：旧版本的GlobalVar访问，统一在public命名空间-->GLOBAL-CONFIG数据Id-->DEFAULT_GROUP组下面。
 * 关于订阅应用时不指定服务：
 *      A应用访问B应用，如果没有指定MODULE-CODE,则会订阅默认的服务。服务编码是：DEFAULT.
 *      所以，如果一个应用希望别人访问路径中不包含ModuleCode也能访问，需要自己部署一个服务编码为DEFAULT的服务。
 *      也就是说：esf://appcode/svc1 等价于 esf://appcode:DEFAULT/svc1
 * </pre>
 */
public class CloudEnvironment {


    public static CloudEnvironment INSTANCE = new CloudEnvironment();
    public static final String NACOS_URL = "nacos-url";
    public static final String MODULE_CODE = "module-code";
    public static final String APP_CODE = "app-code";
    public static final String RPC_PORT = "rpc-port";
    public static final String NACOS_USER = "nacos-user";
    public static final String NACOS_PWD = "nacos-pwd";

    /**
     * embbed tomcat 情况下获取默认的rpc端口。
     */
    public static final String RPC_PORT_HINT_EMBED_TOMCAT = "-1";

    private String nacosUrl;
    private String appCode;
    private String moduleCode;
    private String rpcPort;
    private String nacosUser;
    private String nacosPwd;
    private String composedAppCode;

    private String boundIp;

    private boolean inited = false;

    private CloudEnvironment() {
    }

    public String getNacosUrl() {
        checkInit();
        return nacosUrl;
    }

    public String getNacosPwd() {
        return nacosPwd;
    }

    public String getRpcPort() {
        checkInit();
        return rpcPort;
    }

    public String getNacosUser() {
        return nacosUser;
    }

    public String getAppCode() {
        checkInit();
        return appCode;
    }

    public String getModuleCode() {
        checkInit();
        return moduleCode;
    }

    public String _composeAppCode(){
        checkInit();
        if (StringKit.isNull(composedAppCode)){
            composedAppCode = appCode+":"+ moduleCode;
        }
        return composedAppCode;
    }

    public String getBoundIp(){
        checkInit();
        if (StringKit.isNull(boundIp)){
            boundIp = IpKit.getLocalIp();
        }
        return boundIp;
    }

    public boolean hasInit(){
        return this.inited;
    }

    private void checkInit() {
        if (!inited) {
            throw new RuntimeException("init not called");
        }
    }

    public void init(Map<String, String> map) {
        if (inited)
            PluginEnvirement.getInstance().getStartLogger().log("WARNNING: CloudEnvironment init called a second time，Ignored!");

        AssertKit.assertStringNotNull(map.get(NACOS_URL), NACOS_URL);
        AssertKit.assertStringNotNull(map.get(APP_CODE), APP_CODE);
        AssertKit.assertStringNotNull(map.get(MODULE_CODE), MODULE_CODE);
        AssertKit.assertStringNotNull(map.get(RPC_PORT), RPC_PORT);

        nacosUrl = map.get(NACOS_URL).trim();
        appCode = map.get(APP_CODE).trim();
        moduleCode = map.get(MODULE_CODE).trim();
        rpcPort = map.get(RPC_PORT).trim();

        rpcPort = handleForEmbbedTomcat(rpcPort);

        //handle composed appcode
        if (StringKit.isNotNull(appCode) && appCode.indexOf(":")>=0){
            //service code must be null
            AssertKit.assertNull(moduleCode);
            int pos = appCode.indexOf(":");
            String temp = appCode;
            appCode = temp.substring(0, pos);
            moduleCode = temp.substring(pos+1);
        }

        //user
        String temp = map.get(NACOS_USER);
        if (StringKit.isNotNull(temp))
            nacosUser = temp.trim();
        //pwd
        temp = map.get(NACOS_PWD);
        if (StringKit.isNotNull(temp))
            nacosPwd = temp.trim();

        inited = true;
        PluginEnvirement.getInstance().getStartLogger().log("$$$ CloudEnvironment Init: nacosUrl=" + nacosUrl + ", appCode=" + appCode + ", serviceCode=" + moduleCode + " nacosUser=" + nacosUser + " rpcPort=" + getRpcPort());
    }

    private static String handleForEmbbedTomcat(String rpcPort) {
        if (!RPC_PORT_HINT_EMBED_TOMCAT.equals(rpcPort)){
            return rpcPort;
        }else{
            Integer intTomcatPort = ConfigFactory.getIntConfig("embed-tomcat.context-port", 8080);
            String esfport = (intTomcatPort + 100) + "";
            //赋值一下
            return esfport;
        }
    }

    private String arrToString(String[] serviceCodes) {
        StringBuffer sb = new StringBuffer();
        for (String s : serviceCodes) {
            sb.append(s).append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put(NACOS_URL, "url1");
        map.put(APP_CODE, "appcode1");
        map.put(MODULE_CODE, "S1,S2");
        CloudEnvironment.INSTANCE.init(map);

        System.out.println(CloudEnvironment.INSTANCE.getAppCode());
        System.out.println(CloudEnvironment.INSTANCE.getNacosUrl());
        System.out.println(CloudEnvironment.INSTANCE.getModuleCode());

    }

    public void loadFromConfig() {
        String cfgName = PluginEnvirement.getInstance().getConfigDir() + "/jplugin-cloud.properties";
        if (!FileKit.existsAndIsFile(cfgName)) {
            PluginEnvirement.getInstance().getStartLogger().log("$$$ jplugin-cloud.properties not found");
            return;
        } else {
            Properties prop = null;
            try {
                prop = PropertiesKit.loadProperties(cfgName);
            } catch (Exception e) {
                throw new RuntimeException("load jplugin-cloud.properties error");
            }
            Map<String, String> map = propertiesToMap(prop);
            CloudEnvironment.INSTANCE.init(map);

            PluginEnvirement.getInstance().getStartLogger().log("$$$ CloudEnvironment init ok from jplugin-cloud.properties");
        }
    }

    private Map<String, String> propertiesToMap(Properties prop) {
        Map<String, String> map = new HashMap(0);
        for (Map.Entry en:prop.entrySet()){
            map.put((String)en.getKey(),(String)en.getValue());
        }
        return map;
    }

}