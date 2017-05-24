package net.jplugin.core.das.mybatis.api;

import net.jplugin.core.das.api.PageCond;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

/**
 * mysql分页插件
 * @author peiyu
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class MysqlPageInterceptor extends PageInterceptor {

    @Override
    protected String buildPageSql(String sql, PageCond page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginrow = String.valueOf(page._getFirstRow());
        pageSql.append(sql);
        pageSql.append(" limit " + beginrow + "," + page.getPageSize());
        return pageSql.toString();
    }
}
