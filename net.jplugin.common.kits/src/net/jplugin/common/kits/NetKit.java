package net.jplugin.common.kits;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetKit {

	public static String getIpAddress() {
//		Enumeration allNetInterfaces;
//		try {
//			allNetInterfaces = NetworkInterface.getNetworkInterfaces();
//		} catch (SocketException e) {
//			throw new RuntimeException(e);
//		}
//		InetAddress ip = null;
//		while (allNetInterfaces.hasMoreElements()) {
//			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
//					.nextElement();
//			System.out.println(netInterface.getName());
//			Enumeration addresses = netInterface.getInetAddresses();
//			while (addresses.hasMoreElements()) {
//				ip = (InetAddress) addresses.nextElement();
//				if (ip != null && ip instanceof Inet4Address) {
//					String addr = ip.getHostAddress();
//					System.out.println(addr);
//					// System.out.println("本机的IP = " + ip.getHostAddress());
//				}
//			}
//		}
		 
		return null;
	}
	public static void main(String[] args) {
		System.out.println(getIpAddress());
	}
}
