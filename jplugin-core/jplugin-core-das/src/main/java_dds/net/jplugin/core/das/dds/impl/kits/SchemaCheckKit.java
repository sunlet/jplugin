package net.jplugin.core.das.dds.impl.kits;

import java.util.HashSet;
import java.util.Set;

import net.jplugin.common.kits.StringKit;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SchemaCheckKit {
	public static final String E_M_P_T_Y = "E_M_P_T_Y";

	public static Set<String> extractAndRemoveSchema(Statement stmt) {
		return extractAndRemoveSchema(stmt,true,true);
	}
	
	public static Set<String> extractAndRemoveSchema(Statement stmt,boolean toUpperCase) {
		return extractAndRemoveSchema(stmt,toUpperCase,true);
	}
	
	public static Set<String> extractSchema(Statement stmt,boolean toUpperCase) {
		return extractAndRemoveSchema(stmt,toUpperCase,false);
	}
	
	private static Set<String> extractAndRemoveSchema(Statement stmt,boolean toUpperCase,boolean remove) {
		SchemaNameFinderAndRemover snf = new SchemaNameFinderAndRemover(toUpperCase,remove);
		return snf.getSchemas(stmt);
	}
	
	static class SchemaNameFinderAndRemover extends TablesNamesFinder{
		
		Set<String> schemaNames = new HashSet();
		private boolean needUpperCase;
		private boolean toRemove;
		
		public SchemaNameFinderAndRemover(boolean upper, boolean remove) {
			this.needUpperCase = upper;
			this.toRemove = remove;
		}

		public Set<String> getSchemas(Statement stmt){
			getTableList(stmt);
			return this.schemaNames;
		}
		
		/**
		 * <PRE>
		 * 这里为了获取schemaname ，所以对这个方法进行了重载。
		 * 并且tablename已经是底层，不需要继续调用父类的visit。
		 * </PRE>
		 */
	    public void visit(Table tableName) {
	    	String name = tableName.getSchemaName();
	        if(StringKit.isNull(name)){
	        	name = E_M_P_T_Y;
	        }else{
	        	if (this.toRemove) {
	        		tableName.setSchemaName(null);
	        	}
	        }
	        schemaNames.add( needUpperCase? name.toUpperCase(): name);
	    }
	}
}
