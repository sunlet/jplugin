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

import com.lh.org.apache.log.LogEvent;
import com.lh.org.apache.log.format.Formatter;

/**
 * A target that will open and close a file for each logevent.
 * This is slow but a more reliable form of logging on some
 * filesystems/OSes. It should only be used when there is a
 * small number of log events.
 *
 * @author Peter Donald
 */
public class SafeFileTarget
    extends FileTarget
{
    /**
     * Construct file target to write to a file with a formatter.
     *
     * @param file the file to write to
     * @param append true if file is to be appended to, false otherwise
     * @param formatter the Formatter
     * @exception IOException if an error occurs
     */
    public SafeFileTarget( final File file, final boolean append, final Formatter formatter )
        throws IOException
    {
        super( file, append, formatter );
        shutdownStream();
    }

    /**
     * Process a log event, via formatting and outputting it.
     *
     * @param event the log event
     */
    public synchronized void processEvent( final LogEvent event )
    {
        if( !isOpen() )
        {
            getErrorHandler().error( "Writing event to closed stream.", null, event );
            return;
        }

        try
        {
            final FileOutputStream outputStream =
                new FileOutputStream( getFile().getPath(), true );
            setOutputStream( outputStream );
        }
        catch( final Throwable throwable )
        {
            getErrorHandler().error( "Unable to open file to write log event.", throwable, event );
            return;
        }

        //write out event
        super.processEvent( event );

        shutdownStream();
    }
}
