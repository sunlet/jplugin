package net.jplugin.core.kernel.api.compositeapp;

import javassist.bytecode.AccessFlag;
import net.jplugin.common.kits.StringKit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class ComponentModeConfigHelper {

    public static String getAppCode(Class pluginClazz){
        String jarPath = getJarPath(pluginClazz);
        if (StringKit.isNull(jarPath))
            return null;

        return getAppCode(jarPath);
    }

    private static String getJarPath(Class pluginClazz) {
        URL url = pluginClazz.getResource(pluginClazz.getSimpleName() + ".class");
        if (url==null) return null;

        String path = url.getPath();
        int pos = path.indexOf(".jar!");
        if (pos<0)
            return null;

        String jarFilePath = path.substring(0, pos+4);
        return jarFilePath;
    }

    /**
     * 获取对应jar文件中配置的appcode:modulecode
     * @param jarPath
     * @return
     */
    private static String getAppCode(String jarPath){
        try (JarFile jarFile = new JarFile(jarPath)){
            JarEntry entry =jarFile.getJarEntry("META-INF/jplugin-component.properties");
            try(InputStream stream = jarFile.getInputStream(entry)){
                if (stream==null)
                    return null;

                Properties p = new Properties();
                p.load(stream);
                String appcode = p.getProperty("app-code");
                String module = p.getProperty("module-code");

                if (StringKit.isNull(appcode)){
                    throw new RuntimeException("appcode not found in composite app jar. "+jarPath);
                }
                if (StringKit.isNull(module)){
                    throw new RuntimeException("module code not found in composite app jar. "+jarPath);
                }
                return appcode.trim()+":"+module.trim();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String path = ComponentModeConfigHelper.getJarPath(AccessFlag.class);
        System.out.println(path);
    }

    static class A{

    }
}
