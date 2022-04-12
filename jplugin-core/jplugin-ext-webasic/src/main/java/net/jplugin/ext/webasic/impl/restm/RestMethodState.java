package net.jplugin.ext.webasic.impl.restm;

@Deprecated
public class RestMethodState {
	
	public static void reset(){
		threadLocal.remove();
	}
	public static State get(){
		return threadLocal.get();
	}
	
	public static void setSuccess(boolean b){
		threadLocal.get().success = b;
	}
	public static void setCode(String code){
		threadLocal.get().code = code;
	}
	public static void setMessage(String m){
		threadLocal.get().message = m;
	}
	
	static ThreadLocal<State> threadLocal = new ThreadLocal<State>(){
		@Override
		protected State initialValue() {
			return new State();
		}
	};
	
	public static class State{
		public boolean success =  true;
		public String code = "0";
		public String message="";
	}
}
