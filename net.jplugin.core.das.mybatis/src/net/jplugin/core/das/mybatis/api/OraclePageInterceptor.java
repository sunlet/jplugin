package net.jplugin.core.das.mybatis.api;

import net.jplugin.core.das.api.PageCond;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

/**
 * oracle分页插件
 * @author peiyu
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class OraclePageInterceptor extends PageInterceptor {

    /**
     * 参考hibernate的实现完成oracle的分页
     * @param sql
     * @param page
     * @return
     */
    @Override
    protected String buildPageSql(String sql, PageCond page) {
        StringBuilder pageSql = new StringBuilder(100);
        String beginrow = String.valueOf(page._getFirstRow());
        String endrow = String.valueOf(page._getFirstRow() + page.getPageSize());

        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <= ").append(endrow);
        pageSql.append(") where row_id > ").append(beginrow);
        return pageSql.toString();
    }
}
