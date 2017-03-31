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
package com.lh.org.apache.log.output.io.rotate;

import java.io.File;
import java.io.IOException;

import com.lh.org.apache.log.format.Formatter;
import com.lh.org.apache.log.output.io.FileTarget;

/**
 * This is a basic Output log target that writes to rotating files.
 *
 * @author Peter Donald
 * @author <a href="mailto:mcconnell@osm.net">Stephen McConnell</a>
 * @author <a href="mailto:bh22351@i-one.at">Bernhard Huber</a>
 */
public class RotatingFileTarget
    extends FileTarget
{
    ///Flag indicating whether or not file should be appended to
    private boolean m_append;

    ///The rotation strategy to be used.
    private RotateStrategy m_rotateStrategy;

    ///The file strategy to be used.
    private FileStrategy m_fileStrategy;

    /**
     * Construct RotatingFileTarget object.
     *
     * @param formatter Formatter to be used
     * @param rotateStrategy RotateStrategy to be used
     * @param fileStrategy FileStrategy to be used
     * @exception IOException if a file access or write related error occurs
     */
    public RotatingFileTarget( final Formatter formatter,
                               final RotateStrategy rotateStrategy,
                               final FileStrategy fileStrategy )
        throws IOException
    {
        this( false, formatter, rotateStrategy, fileStrategy );
    }

    /**
     * Construct RotatingFileTarget object.
     *
     * @param append true if file is to be appended to, false otherwise
     * @param formatter Formatter to be used
     * @param rotateStrategy RotateStrategy to be used
     * @param fileStrategy FileStrategy to be used
     * @exception IOException if a file access or write related error occurs
     */
    public RotatingFileTarget( final boolean append,
                               final Formatter formatter,
                               final RotateStrategy rotateStrategy,
                               final FileStrategy fileStrategy )
        throws IOException
    {
        super( null, append, formatter );

        m_append = append;
        m_rotateStrategy = rotateStrategy;
        m_fileStrategy = fileStrategy;

        rotate();
    }

    /**
     * Rotates the file.
     * @exception IOException if a file access or write related error occurs
     */
    protected synchronized void rotate()
        throws IOException
    {
        close();

        final File file = m_fileStrategy.nextFile();
        setFile( file, m_append );
        openFile();
    }

    /**
     * Output the log message, and check if rotation is needed.
     * @param data the date to write to the target
     */
    protected synchronized void write( final String data )
    {
        // if rotation is needed, close old File, create new File
        if( m_rotateStrategy.isRotationNeeded( data, getFile() ) )
        {
            try
            {
                rotate();

                m_rotateStrategy.reset();
            }
            catch( final IOException ioe )
            {
                getErrorHandler().error( "Error rotating file", ioe, null );
            }
        }

        // write the log message
        super.write( data );
    }
}

