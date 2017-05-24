package net.jplugin.common.kits;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.jplugin.common.kits.Base64Kit;

/**
 *
 * @author: LiuHang
 * @version 创建时间：2015-2-11 下午02:02:39
 **/

public class SerializKit {

	public final static String NULLVALUE="NA";
	/**
	 * @param s
	 * @return
	 */
	public static Object deserialFromString(String s) {
		if (NULLVALUE.equals(s)){
			return null;
		}
		
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try{
			byte[] bytes = s.getBytes("utf-8");
			byte[] base64bytes = Base64Kit.decode(bytes);
			
			bais = new ByteArrayInputStream(base64bytes);
			ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			return obj;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if (bais!=null){
				try {bais.close();}catch(Exception e){e.printStackTrace();}
			}
			if (ois!=null){
				try {ois.close();}catch(Exception e){e.printStackTrace();}
			}
		}
	}

	/**
	 * @param result
	 * @return
	 */
	public static String encodeToString(Object o) {
		if (o==null){
			return NULLVALUE;
		}

		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			
			byte[] barr = baos.toByteArray();
			byte[] base64arr = Base64Kit.encode(barr);
			return new String(base64arr,"utf-8");
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			if (baos!=null){
				try {baos.close();}catch(Exception e){e.printStackTrace();}
			}
			if (oos!=null){
				try {oos.close();}catch(Exception e){e.printStackTrace();}
			}
		}
	}
}
