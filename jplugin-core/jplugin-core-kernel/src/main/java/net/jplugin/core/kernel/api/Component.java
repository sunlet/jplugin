package net.jplugin.core.kernel.api;

import net.jplugin.common.kits.AssertKit;

public class Component {
    public static final int STORETYPE_CLASS=0;
    public static final int STORETYPE_JAR=1;

    //是否是平台组件
    private boolean platform;
    //appcode（组合后的）
    private String componentCode;

    //存储路径
    private String storePath;
    //存储类型（CLASS目录|JAR)
    private int storeType;

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("[")
                .append(" code:").append(componentCode).append(" , ")
                .append(" path:").append(storePath).append(" , ")
                .append(storeType).append( " , ").append(platform)
                .append("]");

        return sb.toString();
    }

    public static Component create(String storePath,int storeType){
        Component c = new Component();
//        c.platform = isPlat;
//        c.componentCode = componentCode;
        c.storePath = storePath;
        c.storeType = storeType;
        return c;
    }

    public String getStorePath() {
        return storePath;
    }

    public int getStoreType() {
        return storeType;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public boolean isPlatform() {
        return platform;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public void setPlatform(boolean platform) {
        this.platform = platform;
    }

}
