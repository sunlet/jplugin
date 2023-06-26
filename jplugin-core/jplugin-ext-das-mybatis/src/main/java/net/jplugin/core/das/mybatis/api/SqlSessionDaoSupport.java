package net.jplugin.core.das.mybatis.api;

import net.jplugin.common.kits.StringKit;
import net.jplugin.core.das.api.DataSourceFactory;
import org.apache.ibatis.session.SqlSession;

public abstract class SqlSessionDaoSupport {
    private final String dataSourceName;
    public SqlSessionDaoSupport(){
        String temp=getDataSourceName();
        if (StringKit.isNull(temp)){
            temp = DataSourceFactory.DATABASE_DSKEY;
        }
        this.dataSourceName = temp;
    }

    public final SqlSession getSqlSession(){
        return MyBatisServiceFactory.getService(dataSourceName).openSession();
    }

    /**
     * 获取数据源的名字
     * @return
     */
    public abstract String getDataSourceName();
}
