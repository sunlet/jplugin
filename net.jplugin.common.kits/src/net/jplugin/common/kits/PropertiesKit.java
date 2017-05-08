package net.jplugin.common.kits;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-8 上午01:42:24
 **/

public class PropertiesKit {
	public static Properties loadProperties(String file){
		File configFile = new File(file);
		Properties prop = new Properties();
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(configFile);
			prop.load(stream);
			return prop;
		} catch (Exception e) {
			throw new RuntimeException("property file load error", e);
		}finally{
			try{
				if (stream!=null)
					stream.close();
			}catch(Exception e){}
		}
	}
	
	public static void replaceVar(Properties prop,String key,String value){
		String rKey = "${"+key+"}";
		for (Object o:prop.keySet()){
			String v = (String) prop.get(o);
			if (v.indexOf(rKey) >=0){
				String newv = StringKit.replaceStr(v,rKey , value);
				prop.setProperty((String)o, newv);
			}
		}
	}

	public static Properties loadFromClassPath(Class c, String filename) throws IOException {
		String pathname = StringKit.replaceStr(c.getPackage().getName(),".", "/")+"/"+filename;
		Properties prop = new Properties();
		prop.load(c.getClassLoader().getResourceAsStream(pathname));
		return prop;
	}
}
