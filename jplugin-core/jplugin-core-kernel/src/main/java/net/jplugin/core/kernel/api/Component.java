package net.jplugin.core.kernel.api;

import net.jplugin.common.kits.AssertKit;

/**
 * Component代表一个部署的组件，一个组件为一个jar包或者这目录，一个组件包含一组Plugin。
 * componentCode组件编码，appcode和modulecode的组合。
 * 在Component模式，有些组件是平台组件，有些组件是应用组件，通过platform属性区分。
 * storePath和storeType代表组件对应的部署物的路径和类型。
 */
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
