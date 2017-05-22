package net.jplugin.org.apache.output.lf5;
///* 
// * Copyright 1999-2004 The Apache Software Foundation
// * Licensed  under the  Apache License,  Version 2.0  (the "License");
// * you may not use  this file  except in  compliance with the License.
// * You may obtain a copy of the License at 
// * 
// *   http://www.apache.org/licenses/LICENSE-2.0
// * 
// * Unless required by applicable law or agreed to in writing, software
// * distributed  under the  License is distributed on an "AS IS" BASIS,
// * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
// * implied.
// * 
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.lh.org.apache.log.output.lf5;
//
//import org.apache.log4j.lf5.viewer.LogBrokerMonitor;
//
//import com.lh.org.apache.log.LogEvent;
//import com.lh.org.apache.log.LogTarget;
//import com.lh.org.apache.log.format.Formatter;
//import com.lh.org.apache.log.format.PatternFormatter;
//
///**
// * A {@link LogTarget} that displays log events using the
// * <a href="http://jakarta.apache.org/log4j/docs/lf5/overview.html">LogFactor5</a>
// * Swing GUI.
// *
// * @author <a href="sylvain@apache.org">Sylvain Wallez</a>
// * @version CVS $Revision: 1.8 $ $Date: 2004/02/28 11:31:25 $
// */
//public class LF5LogTarget implements LogTarget
//{
//    /** Common monitor */
//    private static LogBrokerMonitor c_defaultLogMonitor;
//
//    /** Default context map formatter */
//    private static Formatter c_defaultContextFormatter = new PatternFormatter( "" );
//
//    /** Monitor for this LogTarget */
//    private LogBrokerMonitor m_monitor;
//
//    /** Format for context maps */
//    private Formatter m_contextFormatter = c_defaultContextFormatter;
//
//    /**
//     * Create a <code>LogFactorLogTarget</code> on a given <code>LogBrokerMonitor</code>.
//     * @param monitor the monitor
//     */
//    public LF5LogTarget( final LogBrokerMonitor monitor )
//    {
//        m_monitor = monitor;
//    }
//
//    /**
//     * Create <code>LogFactorLogTarget</code> on the default <code>LogBrokerMonitor</code>.
//     */
//    public LF5LogTarget()
//    {
//        // Creation of m_monitor is deferred up to the first call to processEvent().
//        // This allows the Swing window to pop up only if this target is actually used.
//    }
//
//    /**
//     * Sets the {@link Formatter} that will be used to produce the "NDC" (nested diagnostic
//     * context) text on the GUI.
//     * @param formatter the message formatter
//     */
//    public void setNDCFormatter( final Formatter formatter )
//    {
//        m_contextFormatter = formatter;
//    }
//
//    /**
//     * Get the default <code>LogBrokerMonitor</code> instance.
//     *
//     * @return the monitor
//     */
//    public static synchronized LogBrokerMonitor getDefaultMonitor()
//    {
//        if( null == c_defaultLogMonitor )
//        {
//            c_defaultLogMonitor = new LogBrokerMonitor( LogKitLogRecord.LOGKIT_LOGLEVELS );
//            c_defaultLogMonitor.setFontSize( 12 );
//            c_defaultLogMonitor.show();
//        }
//
//        return c_defaultLogMonitor;
//    }
//
//    /**
//     * Process a log event.
//     *
//     * @param event the log event
//     */
//    public void processEvent( final LogEvent event )
//    {
//        if( null == m_monitor )
//        {
//            m_monitor = getDefaultMonitor();
//        }
//
//        m_monitor.addMessage( new LogKitLogRecord( event, m_contextFormatter ) );
//    }
//}
