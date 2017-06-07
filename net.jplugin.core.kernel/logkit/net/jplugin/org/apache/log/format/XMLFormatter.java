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
package net.jplugin.org.apache.log.format;

import java.util.Date;

import net.jplugin.org.apache.log.LogEvent;

/**
 * Basic XML formatter that writes out a basic XML-ified log event.
 *
 * Note that this formatter assumes that the category and context
 * values will produce strings that do not need to be escaped in XML.
 *
 * @author Peter Donald
 */
public class XMLFormatter
    implements Formatter
{
    private static final String EOL = System.getProperty( "line.separator", "\n" );

    //Booleans indicating whether or not we
    //print out a particular field
    private boolean m_printTime = true;
    private boolean m_printRelativeTime = false;
    private boolean m_printPriority = true;
    private boolean m_printCategory = true;
    
    // This can't be changed to 'true' until the Testcases have been fixed
    // 
    private boolean m_printContext = false;
    
    
    private boolean m_printMessage = true;
    private boolean m_printException = true;

    private boolean m_printNumericTime = true;

    /**
     * Print out time field to log.
     *
     * @param printTime true to print time, false otherwise
     */
    public void setPrintTime( final boolean printTime )
    {
        m_printTime = printTime;
    }

    /**
     * Print out relativeTime field to log.
     *
     * @param printRelativeTime true to print relativeTime, false otherwise
     */
    public void setPrintRelativeTime( final boolean printRelativeTime )
    {
        m_printRelativeTime = printRelativeTime;
    }

    /**
     * Print out priority field to log.
     *
     * @param printPriority true to print priority, false otherwise
     */
    public void setPrintPriority( final boolean printPriority )
    {
        m_printPriority = printPriority;
    }

    /**
     * Print out category field to log.
     *
     * @param printCategory true to print category, false otherwise
     */
    public void setPrintCategory( final boolean printCategory )
    {
        m_printCategory = printCategory;
    }

    /**
     * Print out context field to log.
     *
     * @param printContext true to print context, false otherwise
     */
    public void setPrintContext( final boolean printContext )
    {
        m_printContext = printContext;
    }

    /**
     * Print out message field to log.
     *
     * @param printMessage true to print message, false otherwise
     */
    public void setPrintMessage( final boolean printMessage )
    {
        m_printMessage = printMessage;
    }

    /**
     * Print out exception field to log.
     *
     * @param printException true to print exception, false otherwise
     */
    public void setPrintException( final boolean printException )
    {
        m_printException = printException;
    }

    /**
     * Format log event into string.
     *
     * @param event the event
     * @return the formatted string
     */
    public String format( final LogEvent event )
    {
        final StringBuffer sb = new StringBuffer( 400 );

        sb.append( "<log-entry>" );
        sb.append( EOL );

        if( m_printTime )
        {
            sb.append( "  <time>" );

            if( m_printNumericTime )
            {
                sb.append( event.getTime() );
            }
            else
            {
                sb.append( new Date( event.getTime() ) );
            }

            sb.append( "</time>" );
            sb.append( EOL );
        }

        if( m_printRelativeTime )
        {
            sb.append( "  <relative-time>" );
            sb.append( event.getRelativeTime() );
            sb.append( "</relative-time>" );
            sb.append( EOL );
        }

        if( m_printPriority )
        {
            sb.append( "  <priority>" );
            sb.append( event.getPriority().getName() );
            sb.append( "</priority>" );
            sb.append( EOL );
        }

        if( m_printCategory )
        {
            sb.append( "  <category>" );
            sb.append( event.getCategory() );
            sb.append( "</category>" );
            sb.append( EOL );
        }

        if( m_printContext && null != event.getContextMap() )
        {
            sb.append( "  <context-map>" );
            sb.append( event.getContextMap() );
            sb.append( "</context-map>" );
            sb.append( EOL );
        }

        if( m_printMessage && null != event.getMessage() )
        {
            sb.append( "  <message><![CDATA[" );
            sb.append( event.getMessage() );
            sb.append( "]]></message>" );
            sb.append( EOL );
        }

        if( m_printException && null != event.getThrowable() )
        {
            sb.append( "  <exception><![CDATA[" );
            //sb.append( event.getThrowable() );
            sb.append( "]]></exception>" );
            sb.append( EOL );
        }

        sb.append( "</log-entry>" );
        sb.append( EOL );

        return sb.toString();
    }
}
