package net.jplugin.core.kernel.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoBindExtensionManager {
	public static AutoBindExtensionManager INSTANCE = new AutoBindExtensionManager();
	
	private List<IBindExtensionHandler> handlers = new ArrayList<IBindExtensionHandler>();
	private Map<Class,IBindAnnotationTransformer> transformers = new HashMap<Class, IBindAnnotationTransformer>();
	
	private AutoBindExtensionManager(){
		handlers.add(new Handler4Transformers());
	}
	
	/**
	 * 推荐使用addBindAnnotationTransformer,应该可以更简单解决问题
	 * @param h
	 */
	@Deprecated
	public void addBindExtensionHandler(IBindExtensionHandler h){
		this.handlers.add(h);
	}
	
	public List<IBindExtensionHandler> getHandlers() {
		return handlers;
	}
	
	public void addBindExtensionTransformer(Class annoClass,IBindAnnotationTransformer bat) {
		transformers.put(annoClass,bat);
	}
	
	class Handler4Transformers implements IBindExtensionHandler {
		@Override
		public void handle(AbstractPlugin p) {
			if (transformers.isEmpty()) 
				return;
			
			p.filterContainedClassesByChecker(null,(c)->{
				//假定大部分都没Annotation,所以先获取所有的anno，这样性能更好
				Annotation[] annos = c.getAnnotations();
				if (annos==null || annos.length==0) {
					//do nothing
				}else {
					for (Annotation a:annos) {
						IBindAnnotationTransformer trans = transformers.get(a.annotationType());
						if (trans!=null) {
							trans.transform(p, c, a);
							addExtensionAndLog(p,c,a);
						}
					}
				}
				//已经发挥效果，直接返回false
				return false;
			});
		}

		private void addExtensionAndLog(AbstractPlugin p, Class c, Annotation a){
			StringBuffer sb = new StringBuffer("$$$ Auto add extension for ");
			sb.append(a.annotationType().getSimpleName());
			sb.append(" class=").append(c.getSimpleName());
//			sb.append("   ").append(a);
			Method[] methods = a.annotationType().getDeclaredMethods();
			for (Method f:methods) {
				String name = f.getName();
				Object v;
				try {
					v = f.invoke(a, new Object[] {});
					if (v.getClass().isArray()) {
						sb.append(" "+name+"= { ");
						sb.append(getArrayString(v));
						sb.append(" }");
					}else {
						sb.append(" "+name+"=");
						sb.append(v.toString());
					}
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Error when transorm anno."+sb.toString(),e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Error when transorm anno."+sb.toString(),e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException("Error when transorm anno."+sb.toString(),e);
				}
				
			}
			PluginEnvirement.getInstance().getStartLogger().log(sb);
		}

		private String getArrayString(Object array) {
			StringBuffer sb =new StringBuffer();
			int len = Array.getLength(array);
			for (int i=0;i<len ;i++) {
				Object obj = Array.get(array, i);
				sb.append(obj).append("  ");
			}
			return sb.toString();
		}
	}
}
