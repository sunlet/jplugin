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
package com.lh.org.apache.log.output.io;

import java.io.IOException;
import java.io.OutputStream;

import com.lh.org.apache.log.format.Formatter;
import com.lh.org.apache.log.output.AbstractOutputTarget;

/**
 * A basic target that writes to an OutputStream.
 *
 * @author Peter Donald
 */
public class StreamTarget
    extends AbstractOutputTarget
{
    /** OutputStream we are writing to. */
    private OutputStream m_outputStream;

    /** The encoding to use when creating byte array for string, may be null. */
    private String m_encoding;

    /**
     * Constructor that writes to a stream and uses a particular formatter.
     *
     * @param outputStream the OutputStream to write to
     * @param formatter the Formatter to use
     * @param encoding Desired encoding to use when writing to the log, null
     *                 implies the default system encoding.
     */
    public StreamTarget( final OutputStream outputStream,
                         final Formatter formatter,
                         final String encoding )
    {
        super( formatter );
        m_encoding = encoding;

        if( null != outputStream )
        {
            setOutputStream( outputStream );
            open();
        }
    }

    /**
     * Constructor that writes to a stream and uses a particular formatter.
     *
     * @param outputStream the OutputStream to write to
     * @param formatter the Formatter to use
     */
    public StreamTarget( final OutputStream outputStream,
                         final Formatter formatter )
    {
        // We can get the default system encoding by calling the following
        //  method, but it is not a standard API so we work around it by
        //  allowing encoding to be null.
        //    sun.io.Converters.getDefaultEncodingName();

        this( outputStream, formatter, null );
    }

    /**
     * Set the output stream.
     * Close down old stream and write tail if appropriate.
     *
     * @param outputStream the new OutputStream
     */
    protected synchronized void setOutputStream( final OutputStream outputStream )
    {
        if( null == outputStream )
        {
            throw new NullPointerException( "outputStream property must not be null" );
        }

        m_outputStream = outputStream;
    }

    /**
     * Abstract method that will output event.
     *
     * @param data the data to be output
     */
    protected synchronized void write( final String data )
    {
        //Cache method local version
        //so that can be replaced in another thread
        final OutputStream outputStream = m_outputStream;

        if( null == outputStream )
        {
            final String message = "Attempted to write data '" + data + "' to Null OutputStream";
            getErrorHandler().error( message, null, null );
            return;
        }

        try
        {
            byte[] bytes;
            if( m_encoding == null )
            {
                bytes = data.getBytes();
            }
            else
            {
                bytes = data.getBytes( m_encoding );
            }
            outputStream.write( bytes );
            outputStream.flush();
        }
        catch( final IOException ioe )
        {
            final String message = "Error writing data '" + data + "' to OutputStream";
            getErrorHandler().error( message, ioe, null );
        }
    }

    /**
     * Shutdown target.
     * Attempting to write to target after close() will cause errors to be logged.
     *
     */
    public synchronized void close()
    {
        super.close();
        shutdownStream();
    }

    /**
     * Shutdown output stream.
     */
    protected synchronized void shutdownStream()
    {
        final OutputStream outputStream = m_outputStream;
        m_outputStream = null;

        try
        {
            if( null != outputStream )
            {
                // Never close System Streams
                if( ! ( System.out == outputStream && System.err == outputStream ) )
                {
                    outputStream.close();
                }
            }
        }
        catch( final IOException ioe )
        {
            getErrorHandler().error( "Error closing OutputStream", ioe, null );
        }
    }
}
