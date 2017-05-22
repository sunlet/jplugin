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
package net.jplugin.org.apache.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

import net.jplugin.org.apache.log.Logger;
import net.jplugin.org.apache.log.Priority;

/**
 * Redirect an output stream to a logger.
 * This class is useful to redirect standard output or
 * standard error to a Logger. An example use is
 *
 * <pre>
 * final LoggerOutputStream outputStream =
 *     new LoggerOutputStream( logger, Priority.DEBUG );
 * final PrintStream output = new PrintStream( outputStream, true );
 *
 * System.setOut( output );
 * </pre>
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public class LoggerOutputStream
    extends OutputStream
{
    ///Logger that we log to
    private final Logger m_logger;

    ///Log level we log to
    private final Priority m_priority;

    ///The buffered output so far
    private final StringBuffer m_output = new StringBuffer();

    ///Flag set to true once stream closed
    private boolean m_closed;

    /**
     * Construct OutputStreamLogger to write to a particular logger at a particular priority.
     *
     * @param logger the logger to write to
     * @param priority the priority at which to log
     */
    public LoggerOutputStream( final Logger logger,
                               final Priority priority )
    {
        m_logger = logger;
        m_priority = priority;
    }

    /**
     * Shutdown stream.
     * @exception IOException if an error occurs while closing the stream
     */
    public void close()
        throws IOException
    {
        flush();
        super.close();
        m_closed = true;
    }

    /**
     * Write a single byte of data to output stream.
     *
     * @param data the byte of data
     * @exception IOException if an error occurs
     */
    public void write( final int data )
        throws IOException
    {
        checkValid();

        //Should we properly convert char using locales etc??
        m_output.append( (char)data );

        if( '\n' == data )
        {
            flush();
        }
    }

    /**
     * Flush data to underlying logger.
     *
     * @exception IOException if an error occurs
     */
    public synchronized void flush()
        throws IOException
    {
        checkValid();

        m_logger.log( m_priority, m_output.toString() );
        m_output.setLength( 0 );
    }

    /**
     * Make sure stream is valid.
     *
     * @exception IOException if an error occurs
     */
    private void checkValid()
        throws IOException
    {
        if( true == m_closed )
        {
            throw new EOFException( "OutputStreamLogger closed" );
        }
    }
}
