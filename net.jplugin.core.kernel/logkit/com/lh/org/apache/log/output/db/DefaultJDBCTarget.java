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
package com.lh.org.apache.log.output.db;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import com.lh.org.apache.log.ContextMap;
import com.lh.org.apache.log.LogEvent;

/**
 * The basic DB target for configurable output formats.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public class DefaultJDBCTarget
    extends AbstractJDBCTarget
{
    private final String m_table;
    private final ColumnInfo[] m_columns;

    private PreparedStatement m_statement;

    /**
     * Creation of a new JDBC logging target.
     * @param dataSource the JDBC datasource
     * @param table the table
     * @param columns a ColumnInfo array
     */
    public DefaultJDBCTarget( final DataSource dataSource,
                              final String table,
                              final ColumnInfo[] columns )
    {
        super( dataSource );
        m_table = table;
        m_columns = columns;

        if( null == table )
        {
            throw new NullPointerException( "table property must not be null" );
        }

        if( null == columns )
        {
            throw new NullPointerException( "columns property must not be null" );
        }

        if( 0 == columns.length )
        {
            throw new NullPointerException( "columns must have at least 1 element" );
        }

        open();
    }

    /**
     * Output a log event to DB.
     * This must be implemented by subclasses.
     *
     * @param event the log event.
     */
    protected synchronized void output( final LogEvent event )
    {
        //TODO: Retry logic so that this method is called multiple times if it fails
        //Make retry configurable and if fail send event onto ErrorHandler
        try
        {
            for( int i = 0; i < m_columns.length; i++ )
            {
                specifyColumn( m_statement, i, event );
            }

            m_statement.executeUpdate();
        }
        catch( final SQLException se )
        {
            getErrorHandler().error( "Error executing statement", se, event );
        }
    }

    /**
     * Open connection to underlying database.
     *
     */
    protected synchronized void openConnection()
    {
        //if( null != m_statement ) return;
        super.openConnection();

        m_statement = null;
        try
        {
            final Connection connection = getConnection();
            if( null != connection )
            {
                m_statement = connection.prepareStatement( getStatementSQL() );
            }
        }
        catch( final SQLException se )
        {
            getErrorHandler().error( "Error preparing statement", se, null );
        }
    }

    /**
     * Return the SQL insert statement.
     * @return the statement
     */
    protected String getStatementSQL()
    {
        final StringBuffer sb = new StringBuffer( "INSERT INTO " );
        sb.append( m_table );
        sb.append( " (" );
        sb.append( m_columns[ 0 ].getName() );

        for( int i = 1; i < m_columns.length; i++ )
        {
            sb.append( ", " );
            sb.append( m_columns[ i ].getName() );
        }

        sb.append( ") VALUES (?" );

        for( int i = 1; i < m_columns.length; i++ )
        {
            sb.append( ", ?" );
        }

        sb.append( ")" );

        return sb.toString();
    }

    /**
     * Test if the target is stale.
     * @return TRUE if the target is stale else FALSE
     */
    protected boolean isStale()
    {
        return super.isStale();
        //Check: "SELECT * FROM " + m_table + " WHERE 0 = 99" here ...
    }

    /**
     * Close connection to underlying database.
     *
     */
    protected synchronized void closeConnection()
    {
        //close prepared statement here
        super.closeConnection();

        if( null != m_statement )
        {
            try
            {
                m_statement.close();
            }
            catch( final SQLException se )
            {
                getErrorHandler().error( "Error closing statement", se, null );
            }

            m_statement = null;
        }
    }

    /**
     * Adds a single object into statement.
     * @param statement the prepard statement
     * @param index the index
     * @param event the log event
     * @exception SQLException if an SQL related error occurs
     * @exception IllegalStateException if the supplied index is out of bounds
     */
    protected void specifyColumn( final PreparedStatement statement,
                                  final int index,
                                  final LogEvent event )
        throws SQLException,
        IllegalStateException
    {
        final ColumnInfo info = m_columns[ index ];

        switch( info.getType() )
        {
            case ColumnType.RELATIVE_TIME:
                statement.setLong( index + 1, event.getRelativeTime() );
                break;

            case ColumnType.TIME:
                statement.setTimestamp( index + 1, new Timestamp( event.getTime() ) );
                break;

            case ColumnType.MESSAGE:
                statement.setString( index + 1, event.getMessage() );
                break;

            case ColumnType.CATEGORY:
                statement.setString( index + 1, event.getCategory() );
                break;

            case ColumnType.PRIORITY:
                statement.setString( index + 1, event.getPriority().getName() );
                break;

            case ColumnType.CONTEXT:
                statement.setString( index + 1, getContextMap( event.getContextMap(),
                                                               info.getAux() ) );
                break;

            case ColumnType.STATIC:
                statement.setString( index + 1, info.getAux() );
                break;

            case ColumnType.THROWABLE:
                statement.setString( index + 1, getStackTrace( event.getThrowable() ) );
                break;

            default:
                throw new IllegalStateException( "Unknown ColumnType: " + info.getType() );
        }
    }

    /**
     * Return the underlying table
     * @return the table name
     */
    protected final String getTable()
    {
        return m_table;
    }

    /**
     * Return the column info for an supplied index.
     * @param index the index
     * @return the column info
     */
    protected final ColumnInfo getColumn( final int index )
    {
        return m_columns[ index ];
    }

    private String getStackTrace( final Throwable throwable )
    {
        if( null == throwable )
        {
            return "";
        }
        final StringWriter sw = new StringWriter();
        throwable.printStackTrace( new java.io.PrintWriter( sw ) );
        return sw.toString();
    }

    private String getContextMap( final ContextMap map, final String aux )
    {
        if( null == map )
        {
            return "";
        }
        else
        {
            return map.get( aux, "" ).toString();
        }
    }
}
