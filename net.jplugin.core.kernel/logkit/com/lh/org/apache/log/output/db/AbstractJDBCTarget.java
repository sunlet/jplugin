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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.lh.org.apache.log.LogEvent;

import com.lh.org.apache.log.output.AbstractTarget;

/**
 * Abstract JDBC target.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public abstract class AbstractJDBCTarget
    extends AbstractTarget
{
    ///Datasource to extract connections from
    private DataSource m_dataSource;

    ///Database connection
    private Connection m_connection;

    /**
     * Creation of a new instance of the AbstractJDBCTarget.
     * @param dataSource the JDBC datasource
     */
    protected AbstractJDBCTarget( final DataSource dataSource )
    {
        m_dataSource = dataSource;
    }

    /**
     * Process a log event, via formatting and outputting it.
     *
     * @param event the log event
     * @exception Exception if an event processing error occurs
     */
    protected synchronized void doProcessEvent( final LogEvent event )
        throws Exception
    {
        checkConnection();

        if( isOpen() )
        {
            output( event );
        }
    }

    /**
     * Output a log event to DB.
     * This must be implemented by subclasses.
     *
     * @param event the log event.
     */
    protected abstract void output( LogEvent event );

    /**
     * Startup log session.
     *
     */
    protected synchronized void open()
    {
        if( !isOpen() )
        {
            super.open();
            openConnection();
        }
    }

    /**
     * Open connection to underlying database.
     *
     */
    protected synchronized void openConnection()
    {
        try
        {
            m_connection = m_dataSource.getConnection();
        }
        catch( final Throwable throwable )
        {
            getErrorHandler().error( "Unable to open connection", throwable, null );
        }
    }

    /**
     * Utility method for subclasses to access connection.
     *
     * @return the Connection
     */
    protected final synchronized Connection getConnection()
    {
        return m_connection;
    }

    /**
     * Utility method to check connection and bring it back up if necessary.
     */
    protected final synchronized void checkConnection()
    {
        if( isStale() )
        {
            closeConnection();
            openConnection();
        }
    }

    /**
     * Detect if connection is stale and should be reopened.
     *
     * @return true if connection is stale, false otherwise
     */
    protected synchronized boolean isStale()
    {
        if( null == m_connection )
        {
            return true;
        }

        try
        {
            if( m_connection.isClosed() )
            {
                return true;
            }
        }
        catch( final SQLException se )
        {
            return true;
        }

        return false;
    }

    /**
     * Shutdown target.
     * Attempting to write to target after close() will cause errors to be logged.
     *
     */
    public synchronized void close()
    {
        if( isOpen() )
        {
            closeConnection();
            super.close();
        }
    }

    /**
     * Close connection to underlying database.
     *
     */
    protected synchronized void closeConnection()
    {
        if( null != m_connection )
        {
            try
            {
                m_connection.close();
            }
            catch( final SQLException se )
            {
                getErrorHandler().error( "Error shutting down JDBC connection", se, null );
            }

            m_connection = null;
        }
    }
}
