package net.jplugin.core.config.comp;

import javassist.bytecode.AccessFlag;
import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.kernel.api.Component;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class ComponentModeConfigHelper {

    public static Tuple2<String,Integer> getStorePathAndType(Class pluginClazz) {
        URL url = pluginClazz.getResource(pluginClazz.getSimpleName() + ".class");
        if (url==null) return null;

        String path = url.getFile();
        if (path.startsWith("file:/")){
            path = path.substring(6);
        }

        int pos = path.indexOf(".jar!");
        if (pos>0) {
            String jarFilePath = path.substring(0, pos + 4);
            return Tuple2.with(jarFilePath, Component.STORETYPE_JAR);
        }

        pos = getClassesRootPos(path,pluginClazz);
        if (pos>=0) {
            String classDir = path.substring(0, pos);
            return Tuple2.with(classDir,Component.STORETYPE_CLASS);
        }

        throw new RuntimeException("Error to inference component of plugin:"+pluginClazz.getName());
    }

    private static int getClassesRootPos(String path, Class pluginClazz) {
        String name = pluginClazz.getName();

        int posName;
        int posPath;

        while(true){
            posName =name.lastIndexOf('.');
            posPath = path.lastIndexOf('/');

            if (posName<0) return posPath;

            name = name.substring(0, posName);
            path = path.substring(0,posPath);
        }
    }


    public static String getAppCode(String path,int type){
        if (Component.STORETYPE_CLASS==type){
            return getAppCodeFromDir(path);
        }else{
            return getAppCodeFromJar(path);
        }
    }

    /**
     * 获取对应jar文件中配置的appcode:modulecode
     * @return
     */

    private static String getAppCodeFromDir(String dirPath){
        String file = dirPath+"/META-INF/jplugin-component.properties";
        Properties properties = new Properties();

        if (!new File(file).exists()){
            return null;
        }

        try(InputStream fis = new FileInputStream(file)){
            properties.load(fis);
            return getComponentCode(file, properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getAppCodeFromJar(String jarPath){
        try (JarFile jarFile = new JarFile(jarPath)){
            JarEntry entry =jarFile.getJarEntry("META-INF/jplugin-component.properties");
            try(InputStream stream = jarFile.getInputStream(entry)){
                if (stream==null)
                    return null;

                Properties p = new Properties();
                p.load(stream);
                return getComponentCode(jarPath, p);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getComponentCode(String filePath, Properties p) {
        String appcode = p.getProperty("app-code");
        String module = p.getProperty("module-code");

        if (StringKit.isNull(appcode)){
            throw new RuntimeException("appcode not found in composite app jar. "+filePath);
        }
        if (StringKit.isNull(module)){
            throw new RuntimeException("module code not found in composite app jar. "+filePath);
        }
        return appcode.trim()+":"+module.trim();
    }

    public static void main(String[] args) {
//        String path = ComponentModeConfigHelper.getJarPath(AccessFlag.class);

        Tuple2<String, Integer> p1 = getStorePathAndType(AccessFlag.class);
        Tuple2<String, Integer> p2 = getStorePathAndType(ComponentModeConfigHelper.class);

        System.out.println(p1.first);
        System.out.println(new File(p1.first).exists());
        System.out.println(new File(p2.first).exists());
        

    }

}
