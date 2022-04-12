package net.jplugin.core.das.dds.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.jplugin.common.kits.StringKit;
import net.jplugin.common.kits.StringMatcher;
import net.jplugin.common.kits.tuple.Tuple2;
import net.jplugin.core.das.dds.api.RouterException;


/**
 * <pre>
 * path-list=p0,p1,p2
 * 
 * p0-commands=SELECT
 * p0-tables=s1.*|s2.table1
 * p0-target=ds1
 * 
 * p1-commands=UPDATE|DELETE|DROP|CREATE|......
 * p1-tables=*
 * p1-target=ds2
 * 
 * p2-target=ds3
 * p2-default=true
 * 
 * 规则里面：
 * 所有规则里面，大小写不敏感。
 * 
 * commands规则：
 * SELECT|UPDATE|DELETE
 * 
 * p0-tables的规则：  
 * 1.直接写不带.的 ， 表示表名。对应的Schema为空。 这里引入一个表示空的常量 _EMPTY_   比如：tables1相当于 _EMPTY_.table1
 * 2.其他格式比如：s1.tb1  ,s*.tb1, s1.*tb ,*.tb , s1.* , s1|s2.tb1|tb2|tt* 等等
 * 3.多个的情况用都好分开
 * 
 * </pre>
 * @author LiuHang
 *
 */

public class SelectDatasourceConfig {
	
	private static final String EMPTY="_EMPTY_";
	
	public static final String PATH_LIST="path-list";
	
	public static final String POST_COMMANDS="commands";
	public static final String POST_TABLES="tables";
	public static final String POST_TARGET="target";
	public static final String POST_DEFAULT="default";
	
	public static final String CMD_SELECT="SELECT";
	public static final String CMD_UPDATE="UPDATE";
	public static final String CMD_DELETE="DELETE";
	public static final String CMD_INSERT="INSERT";
	public static final String CMD_COMMENT = "COMMENT";
	public static final String CMD_REPLACE = "REPLACE";
	public static final String CMD_TRUNCATE = "TRUNCATE";
	public static final String CMD_DROP = "DROP";
	public static final String CMD_CREATE_INDEX = "CREATE_INDEX";
	public static final String CMD_CREATE_TABLE = "CREATE_TABLE";
	public static final String CMD_CREATE_VIEW = "CREATE_VIEW";
	public static final String CMD_ALTER_VIEW = "ALTER_VIEW";
	public static final String CMD_ALTER = "ALTER";
	public static final String CMD_MERGE = "MERGE";
	public static final String CMD_UPSERT = "UPSERT";
	public static final String CMD_DESC = "DESC";
	

	public static class TableMatcher {
		//and
		StringMatcher schemaMatcher;
		StringMatcher tableMatcher;
	}


	Path[] notDefaultPaths;
	Path defaultPath;
	
	public static class Path {
		StringMatcher commandsMatcher; // or 
		TableMatcher  tablesMatcher;// or
		boolean isDefault;
		String target;
		
		public Path(String upperCm, String upperTm, String upperIsDefault, String tgt) {
			commandsMatcher = new StringMatcher(upperCm);
			Tuple2<String, String> tup = getSchemaAndName(upperTm);
			tablesMatcher = new TableMatcher();
			tablesMatcher.schemaMatcher = new StringMatcher(tup.first);
			tablesMatcher.tableMatcher = new StringMatcher(tup.second);
			isDefault = "TRUE".equalsIgnoreCase(upperIsDefault);
			target = tgt;
		}
		
		public boolean matchCommand(String command) {
			command = StringKit.trim(command).toUpperCase();
			return commandsMatcher.match(command); 
		}
		
		public boolean matchTable(String name) {
			if (StringKit.isNull(name)) {
				throw new RuntimeException("table name is null");
			}
			name = name.toUpperCase();
			
			Tuple2<String, String> tup = getSchemaAndName(name);
				
			//compute match
			
			return (tablesMatcher.schemaMatcher.match(tup.first) && tablesMatcher.tableMatcher.match(tup.second));
		}

		private Tuple2<String, String> getSchemaAndName(String name) {
			String[] list; 
			if (name==null) {
				list = new String[] {""};
			}else {
				list = StringKit.splitStr(name, ".");
			}
			String schema,table;
			switch(list.length) {
			case 1:
				schema = EMPTY;
				table = list[0].trim();
				break;
			case 2:
				schema = list[0].trim();
				table = list[1].trim();
				break;
			default:
				throw new RuntimeException("name segment is error:"+name);
			}
			Tuple2<String, String> tup = Tuple2.with(schema, table);
			return tup;
		}
	}



	public static SelectDatasourceConfig create(Map<String, String> c) {
		SelectDatasourceConfig o = new SelectDatasourceConfig();
//		Map<String, String> config = StringKit.trim(c);
		o.from(c);
		return o;
	}
	
	
	public String getTargetDataSource(String command,String schemaAndTableName) {
		
		if (notDefaultPaths!=null) {
			for (Path p:notDefaultPaths) {
				if (p.matchCommand(command) && p.matchTable(schemaAndTableName))
					return p.target;
			}
		}
		if (defaultPath!=null)
			return defaultPath.target;
		
		throw new RuntimeException("can't find target");
	}
	
	private void from(Map<String, String> config) {
		String list = config.get(PATH_LIST);
		if (StringKit.isNull(list)) {
			throw new RouterException(PATH_LIST+" can't be null configed.");
		}
		String[] pList = StringKit.splitStrAndTrim(list, ",");
		
		Path[] pathList = new Path[pList.length];
		if (pathList.length==0) {
			throw new RuntimeException("no path configed for key:"+PATH_LIST);
		}
		
		for (int i=0;i<pathList.length;i++) {
			String prefix = pList[i] + "-";
			String commandsMatcher=config.get(prefix + POST_COMMANDS);
			String tablesMatcher=config.get(prefix + POST_TABLES);
			String isDefault = config.get(prefix + POST_DEFAULT);
			String target = config.get(prefix + POST_TARGET);
			
			if (commandsMatcher!=null)
				commandsMatcher = commandsMatcher.toUpperCase().trim();
			if (tablesMatcher!=null)
				tablesMatcher = tablesMatcher.toUpperCase().trim();
			if (isDefault!=null)
				isDefault = isDefault.toUpperCase().trim();
			if (target!=null)
				target = target.trim();//不要转大写
			else 
				throw new RuntimeException("target datasource can't be null");
			
			pathList[i] = new Path(commandsMatcher,tablesMatcher,isDefault,target);
		}
		
		//对defaultPath 和 notDefaultPaths赋值
		List<Path> unDefault = new ArrayList<SelectDatasourceConfig.Path>();
		for (Path p:pathList) {
			if (p.isDefault) {
				if (defaultPath!=null) {
					throw new RuntimeException("only one default path is allowed");
				}else {
					defaultPath = p;
				}
			}else {
				unDefault.add(p);
			}
		}
		notDefaultPaths = unDefault.toArray(new Path[unDefault.size()]);
	}
	
	
}

