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

import net.jplugin.org.apache.log.LogEvent;
import net.jplugin.org.apache.log.Priority;

/**
 * A formatter that serializes in the format originally
 * used by BSD syslog daemon.
 *
 * @author Peter Donald
 */
public class SyslogFormatter
    implements Formatter
{
    public static final int PRIORITY_DEBUG = 7;
    public static final int PRIORITY_INFO = 6;
    public static final int PRIORITY_NOTICE = 5;
    public static final int PRIORITY_WARNING = 4;
    public static final int PRIORITY_ERR = 3;
    public static final int PRIORITY_CRIT = 2;
    public static final int PRIORITY_ALERT = 1;
    public static final int PRIORITY_EMERG = 0;

    /*
     * Constants for facility.
     */
    public static final int FACILITY_KERN = ( 0 << 3 );
    public static final int FACILITY_USER = ( 1 << 3 );
    public static final int FACILITY_MAIL = ( 2 << 3 );
    public static final int FACILITY_DAEMON = ( 3 << 3 );
    public static final int FACILITY_AUTH = ( 4 << 3 );
    public static final int FACILITY_SYSLOG = ( 5 << 3 );
    public static final int FACILITY_LPR = ( 6 << 3 );
    public static final int FACILITY_NEWS = ( 7 << 3 );
    public static final int FACILITY_UUCP = ( 8 << 3 );
    public static final int FACILITY_CRON = ( 9 << 3 );
    public static final int FACILITY_AUTHPRIV = ( 10 << 3 );
    public static final int FACILITY_FTP = ( 11 << 3 );

    public static final int FACILITY_LOCAL0 = ( 16 << 3 );
    public static final int FACILITY_LOCAL1 = ( 17 << 3 );
    public static final int FACILITY_LOCAL2 = ( 18 << 3 );
    public static final int FACILITY_LOCAL3 = ( 19 << 3 );
    public static final int FACILITY_LOCAL4 = ( 20 << 3 );
    public static final int FACILITY_LOCAL5 = ( 21 << 3 );
    public static final int FACILITY_LOCAL6 = ( 22 << 3 );
    public static final int FACILITY_LOCAL7 = ( 23 << 3 );

    ///String descriptions of all the facilities
    protected static final String[] FACILITY_DESCRIPTIONS =
        {
            "kern", "user", "mail", "daemon", "auth", "syslog",
            "lpr", "news", "uucp", "cron", "authpriv", "ftp",
            "", "", "", "", "local0", "local1", "local2", "local3",
            "local4", "local5", "local6", "local7"
        };

    ///Constant for holding facility id
    private int m_facility;

    ///flag to decide whether we write out Facility banner
    private boolean m_showFacilityBanner;

    /**
     * Constructor that assumes FACILITY_USER.
     */
    public SyslogFormatter()
    {
        this( FACILITY_USER );
    }

    /**
     * Constructor so that you can associate facility with formatter.
     *
     * @param facility the facility constant
     */
    public SyslogFormatter( final int facility )
    {
        this( facility, true );
    }

    /**
     * Constructor allowing setting of facility and whether to show banner.
     *
     * @param facility the facility code.
     * @param showFacilityBanner true if facility banner should be shown
     */
    public SyslogFormatter( final int facility, final boolean showFacilityBanner )
    {
        m_facility = facility;
        m_showFacilityBanner = showFacilityBanner;
    }

    /**
     * Format log event into syslog string.
     *
     * @param event the event
     * @return the formatted string
     */
    public String format( final LogEvent event )
    {
        final int priority = getSyslogPriority( event );
        final int facility = getSyslogFacility( event );
        String message = event.getMessage();

        //TODO: Clean and spruce message here (ie remove \t and \n's)

        if( null == message )
        {
            message = "";
        }

        if( m_showFacilityBanner )
        {
            message = getFacilityDescription( facility ) + ": " + message;
        }

        return "<" + ( facility | priority ) + "> " + message;
    }

    /**
     * Retrieve description for facility.
     *
     * @param facility the facility code
     * @return the facility description
     */
    protected String getFacilityDescription( final int facility )
    {
        return FACILITY_DESCRIPTIONS[ facility >> 3 ];
    }

    /**
     * Get facility associated with event.
     * Default implementation returns facility set in constructor.
     *
     * @param event the log event
     * @return the facility code
     */
    protected int getSyslogFacility( final LogEvent event )
    {
        return m_facility;
    }

    /**
     * Get syslog priority code for LogEvent.
     * This is done by translating LogKit priority to syslog priority.
     *
     * @param event the log event
     * @return the priority code
     */
    protected int getSyslogPriority( final LogEvent event )
    {
        if( event.getPriority().isLowerOrEqual( Priority.DEBUG ) )
        {
            return PRIORITY_DEBUG;
        }
        else if( event.getPriority().isLowerOrEqual( Priority.INFO ) )
        {
            return PRIORITY_INFO;
        }
        else if( event.getPriority().isLowerOrEqual( Priority.WARN ) )
        {
            return PRIORITY_WARNING;
        }
        else if( event.getPriority().isLowerOrEqual( Priority.ERROR ) )
        {
            return PRIORITY_ERR;
        }
        else
        {
            return PRIORITY_CRIT;
        }
    }
}
