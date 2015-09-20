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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lh.org.apache.log.format.Formatter;

/**
 * A basic target that writes to a File.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public class FileTarget
    extends StreamTarget
{
    ///File we are writing to
    private File m_file;

    ///Flag indicating whether or not file should be appended to
    private boolean m_append;

    /**
     * Construct file target to write to a file with a formatter.
     *
     * @param file the file to write to
     * @param append true if file is to be appended to, false otherwise
     * @param formatter the Formatter
     * @exception IOException if an error occurs
     */
    public FileTarget( final File file, final boolean append, final Formatter formatter )
        throws IOException
    {
        super( null, formatter );

        if( null != file )
        {
            setFile( file, append );
            openFile();
        }
    }

    /**
     * Set the file for this target.
     *
     * @param file the file to write to
     * @param append true if file is to be appended to, false otherwise
     * @exception IOException if directories can not be created or file can not be opened
     */
    protected synchronized void setFile( final File file, final boolean append )
        throws IOException
    {
        if( null == file )
        {
            throw new NullPointerException( "file property must not be null" );
        }

        if( isOpen() )
        {
            throw new IOException( "target must be closed before "
                                   + "file property can be set" );
        }

        m_append = append;
        m_file = file;
    }

    /**
     * Open underlying file and allocate resources.
     * This method will attempt to create directories below file and
     * append to it if specified.
     * @exception IOException if directories can not be created or file can not be opened
     */
    protected synchronized void openFile()
        throws IOException
    {
        if( isOpen() )
        {
            close();
        }

        final File file = getFile().getCanonicalFile();

        final File parent = file.getParentFile();
        if( null != parent && !parent.exists() )
        {
            parent.mkdirs();
        }

        final FileOutputStream outputStream =
            new FileOutputStream( file.getPath(), m_append );

        setOutputStream( outputStream );
        open();
    }

    /**
     * Retrieve file associated with target.
     * This allows subclasses to access file object.
     *
     * @return the output File
     */
    protected synchronized File getFile()
    {
        return m_file;
    }
}
