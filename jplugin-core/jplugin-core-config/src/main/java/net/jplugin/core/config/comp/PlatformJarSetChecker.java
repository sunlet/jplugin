package net.jplugin.core.config.comp;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.config.api.ConfigFactory;
import net.jplugin.core.kernel.api.Component;

import java.util.HashSet;
import java.util.Set;

public class PlatformJarSetChecker {
    static Set<String> nameSet = null; 
    public static boolean checkPlatform(String path, Integer type) {
        if (nameSet==null){
           initNameSet(); 
        }

        String name;
        if (type== Component.STORETYPE_CLASS){
            name = getNameFromClassTypePath(path);
        }else{
            name = getNameFromJarTypePath(path);
        }



        return nameSet.contains(name);
    }

    /**
     * 去除 target classes  test-classes . 前面的一级目录名称。 这个主要是为了便于测试
     * @param name
     * @return
     */
    private static String getNameFromClassTypePath(String name) {
        while(true) {
            int pos = name.lastIndexOf('/');
            if (pos < 0)
                return name;
            String postFix = name.substring(pos + 1);
            if (postFix.equals("target") || postFix.equals("classes") || postFix.equals("test-classes")) {
                name = name.substring(0, pos);
            }else{
                return postFix;
            }
        }
    }

    private static String getNameFromJarTypePath(String path) {
        String name;//去除路径，获得文件名
        int pos = path.lastIndexOf('/');
        if (pos<0 ) throw new RuntimeException("error path: "+path);
        name = path.substring(0,pos);
        name = getJarNameExcludeVersion(name);
        return name;
    }

    //对文件名进行处理，如果一个 “-”以后的内容，是非字母开头 || SNAPSHOT，就可以舍弃，然后继续往前查找判断
    private static String getJarNameExcludeVersion(String name) {
        while(true) {
            int pos = name.lastIndexOf('-');
            if (pos < 0)
                return name;
            String postFix = name.substring(pos + 1);
            if (notChar(postFix.charAt(0)) || postFix.startsWith("SNAPSHOT")) {
                name = name.substring(0, pos);
            }else{
                return name;
            }
        }
    }

    private static boolean notChar(char c) {
        return !((c>='A' && c<='Z') || (c>='a'&& c<='z'));
    }

    private static void initNameSet() {
        nameSet = new HashSet<>();

        String temp = ConfigFactory.getStringConfig("local.platform-jars","");
        if (StringKit.isNotNull(temp)) {
            String[] names = StringKit.splitStr(temp, ",");
            for (String n : names) {
                nameSet.add(n);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(getJarNameExcludeVersion("jplugin-cloud-3.1.0-SNAPSHOT.jar"));
        System.out.println(getJarNameExcludeVersion("jplugin-das-3.0.0.jar"));
        System.out.println(getJarNameExcludeVersion("jplugin-das-3.0.0b1.jar"));
        System.out.println(getNameFromClassTypePath("jplugin-core/jplugin-ext-gtrace/target/classes"));

    }
}
