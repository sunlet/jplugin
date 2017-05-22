/* 
 * Copyright 1999-2004 The Apache Software Foundation
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.jplugin.org.apache.log.output.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.DataSource;

import net.jplugin.org.apache.log.LogEvent;

/**
 * JDBC target that writes to normalized tables.
 * This reduces overhead and cost of querying/storing logs.
 *
 * <p>Parts based on JDBC logger from prottomatter by
 * <a href="mailto:nate@protomatter.com">Nate Sammons</a></p>
 *
 * @author Peter Donald
 */
public class NormalizedJDBCTarget
    extends DefaultJDBCTarget
{
    private HashMap m_categoryIDs = new HashMap();
    private HashMap m_priorityIDs = new HashMap();

    public NormalizedJDBCTarget( final DataSource dataSource,
                                 final String table,
                                 final ColumnInfo[] columns )
    {
        super( dataSource, table, columns );
    }

    /**
     * Adds a single object into statement.
     */
    protected void specifyColumn( final PreparedStatement statement,
                                  final int index,
                                  final LogEvent event )
        throws SQLException
    {
        final ColumnInfo info = getColumn( index );
        int id = 0;
        String tableName = null;

        switch( info.getType() )
        {
            case ColumnType.CATEGORY:
                tableName = getTable() + "_" + ColumnType.CATEGORY_STR + "_SET";
                id = getID( tableName, m_categoryIDs, event.getCategory() );
                statement.setInt( index + 1, id );
                break;

            case ColumnType.PRIORITY:
                tableName = getTable() + "_" + ColumnType.PRIORITY_STR + "_SET";
                id = getID( tableName, m_priorityIDs, event.getPriority().getName() );
                statement.setInt( index + 1, id );
                break;

            default:
                super.specifyColumn( statement, index, event );
        }
    }

    protected synchronized int getID( final String tableName, final HashMap idMap, final String instance )
        throws SQLException
    {
        final Integer id = (Integer)idMap.get( instance );
        if( null != id ) return id.intValue();

        // see if it's been put in before.
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            statement = getConnection().createStatement();

            final String querySql = "SELECT ID FROM " + tableName + " WHERE NAME='" + instance + "'";
            resultSet = statement.executeQuery( querySql );

            if( resultSet.next() )
            {
                final Integer newID = new Integer( resultSet.getInt( 1 ) );
                idMap.put( instance, newID );
                return newID.intValue();
            }

            resultSet.close();

            //Note that the next part should be a transaction but
            //it is not mega vital so ...

            //Find the max id in table and set
            //max to it's value if any items are present in table
            final String maxQuerySql = "SELECT MAX(ID) FROM " + tableName;
            resultSet = statement.executeQuery( maxQuerySql );
            int max = 0;
            if( resultSet.next() ) max = resultSet.getInt( 1 );
            resultSet.close();

            final int newID = max + 1;
            final String insertSQL = "INSERT INTO " + tableName +
                " (ID, NAME) VALUES ( " + newID + ", '" + instance + "')";
            statement.executeUpdate( insertSQL );

            idMap.put( instance, new Integer( newID ) );
            return newID;
        }
        finally
        {
            // close up shop
            if( null != resultSet )
            {
                try
                {
                    resultSet.close();
                }
                catch( final Exception e )
                {
                }
            }
            if( null != statement )
            {
                try
                {
                    statement.close();
                }
                catch( final Exception e )
                {
                }
            }
        }
    }
}
