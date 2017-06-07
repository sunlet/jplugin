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
package net.jplugin.org.apache.output.io.rotate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Strategy for naming log files based on appending time suffix.
 * A file name can be based on simply appending the number of miliseconds
 * since (not really sure) 1/1/1970.
 * Other constructors accept a pattern of a <code>SimpleDateFormat</code>
 * to form the appended string to the base file name as well as a suffix
 * which should be appended last.
 *
 * A <code>new UniqueFileStrategy( new File( "foo." ), "yyyy-MM-dd", ".log" )</code>
 * object will return <code>File</code> objects with file names like
 * <code>foo.2001-12-24.log</code>
 *
 * @author <a href="mailto:bh22351@i-one.at">Bernhard Huber</a>
 * @author <a href="mailto:giacomo@apache.org">Giacomo Pati</a>
 */
public class UniqueFileStrategy
    implements FileStrategy
{
    private File m_baseFile;

    private SimpleDateFormat m_formatter;

    private String m_suffix;

    /**
     * Creation of a new Unique File Strategy ??
     * @param baseFile the base file
     */
    public UniqueFileStrategy( final File baseFile )
    {
        m_baseFile = baseFile;
    }

    /**
     * Creation of a new Unique File Strategy ??
     * @param baseFile the base file
     * @param pattern the format pattern
     */
    public UniqueFileStrategy( final File baseFile, String pattern )
    {
        this( baseFile );
        m_formatter = new SimpleDateFormat( pattern );
    }

    /**
     * Creation of a new Unique File Strategy ??
     * @param baseFile the base file
     * @param pattern the format pattern
     * @param suffix the suffix ??
     */
    public UniqueFileStrategy( final File baseFile, String pattern, String suffix )
    {
        this( baseFile, pattern );
        m_suffix = suffix;
    }

    /**
     * Calculate the real file name from the base filename.
     *
     * @return File the calculated file name
     */
    public File nextFile()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append( m_baseFile );
        if( m_formatter == null )
        {
            sb.append( System.currentTimeMillis() );
        }
        else
        {
            final String dateString = m_formatter.format( new Date() );
            sb.append( dateString );
        }

        if( m_suffix != null )
        {
            sb.append( m_suffix );
        }

        return new File( sb.toString() );
    }
}

