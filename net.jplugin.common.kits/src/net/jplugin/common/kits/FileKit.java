/*
 * Created on 2003-10-22
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.jplugin.common.kits;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.Properties;

/**
 * @author Liu Hang
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FileKit {

	public static void string2File(String string, String filename,String encode)
			throws Exception {
		FileOutputStream fop=null;
		OutputStreamWriter osw=null;
		try {
			fop = new FileOutputStream(filename);
			osw = new OutputStreamWriter(fop,encode);
			osw.write(string);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("FileNotFound",e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("IOException",e);
		}finally{
			if (osw!=null) osw.close();
			if (fop!=null) fop.close();
		}
	}

	
	public static void string2File(String string, String filename)
			throws Exception {
		try {
			FileWriter fw = new FileWriter(filename);
			fw.write(string);
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("FileNotFound");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("IOException");
		}

	}
	
	public static String classPathFile2String(Class clazz,String filename, String charSet)
		{
		InputStream fis=null;
		try {
			fis = clazz.getResourceAsStream(filename);
			int arrSize = 1000000;
			byte[] barr = new byte[arrSize];
			int streamSize = fis.read(barr);

			int lastread = streamSize;
			while (lastread > 0) {
				lastread = fis.read(barr, streamSize, barr.length - streamSize);
				streamSize += lastread;
			}

			if (streamSize >= arrSize)
				throw new Exception("stream too long!");
			String s = new String(barr, 0, streamSize, charSet);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("IOException,filename="+filename,e);
		}finally{
			if (fis!=null) {
				try{fis.close();}catch(Exception e){e.printStackTrace();}
			}
		}
	}


	public static String classPathFile2String(String filename, String charSet)
			throws Exception {
		InputStream fis;
		try {
			fis = FileKit.class.getClassLoader().getResourceAsStream(filename);
			int arrSize = 1000000;
			byte[] barr = new byte[arrSize];
			int streamSize = fis.read(barr);

			int lastread = streamSize;
			while (lastread > 0) {
				lastread = fis.read(barr, streamSize, barr.length - streamSize);
				streamSize += lastread;
			}

			if (streamSize >= arrSize)
				throw new Exception("stream too long!");
			String s = new String(barr, 0, streamSize, charSet);
			return s;
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("IOException");
		}
	}

	public static String file2String(String filename) throws Exception {
		FileReader fr;
		try {
			fr = new FileReader(filename);
			StringWriter sw = new StringWriter();
			char[] buffer = new char[1024];
			while (true) {
				int cnt;
				cnt = fr.read(buffer);
				sw.write(buffer, 0, cnt);
				if (cnt < buffer.length)
					break;
			}
			return sw.getBuffer().toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("FileNotFound");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("IOException");
		}
	}

	public static String file2String(File file) throws Exception {
		return file2String(file.getName());
	}

	public static String file2String(String filename, String charSet)
			throws Exception {
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(filename);
			File file = new File(filename);
			byte[] barr = new byte[(int) file.length()];
			fis.read(barr);
			String s = new String(barr, charSet);
			return s;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("FileNotFound");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("IOException");
		}finally{
			if (fis!=null){
				try{
					fis.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param string
	 */
	public static void makeDirectory(String dir) {
		File file = new File(dir);
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			makeDirectory(file.getParentFile().getPath());
			file.mkdir();
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public static String getFileExt(String name) {
		if (StringKit.isNull(name)) {
			return null;
		}

		int pos = name.lastIndexOf('.');
		if (pos < 0)
			return null;
		return name.substring(pos + 1);
	}

	/**
	 * @param string
	 * @param destFilename
	 */
	public static void copyFile(String s, String t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			throw new RuntimeException("copy file error:"+s+" "+t,e);
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				throw new RuntimeException("copy file error:"+s+" "+t,e);
			}
		}
	}


	public static boolean existsFile(String filepath) {
		return new File(filepath).exists();
	}

}
