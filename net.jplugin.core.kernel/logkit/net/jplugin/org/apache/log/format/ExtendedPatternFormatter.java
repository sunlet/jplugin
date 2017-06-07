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


import net.jplugin.org.apache.log.ContextMap;
import net.jplugin.org.apache.log.LogEvent;
import net.jplugin.org.apache.log.Logger;
import net.jplugin.org.apache.util.StackIntrospector;

/**
 * Formatter especially designed for debugging applications.
 *
 * This formatter extends the standard PatternFormatter to add
 * two new possible expansions. These expansions are %{method}
 * and %{thread}. In both cases the context map is first checked
 * for values with specified key. This is to facilitate passing
 * information about caller/thread when threads change (as in
 * AsyncLogTarget). They then attempt to determine appropriate
 * information dynamically.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 * @version CVS $Revision: 1.16 $ $Date: 2004/02/28 11:31:24 $
 */
public class ExtendedPatternFormatter
    extends PatternFormatter
{
    private static final int TYPE_METHOD = MAX_TYPE + 1;
    private static final int TYPE_THREAD = MAX_TYPE + 2;

    private static final String TYPE_METHOD_STR = "method";
    private static final String TYPE_THREAD_STR = "thread";

    private int m_callStackOffset = 0;

    /**
     * Creation of a new extended pattern formatter.
     * @param format the format string
     */
    public ExtendedPatternFormatter( final String format )
    {
        this( format, 0 );
    }

    /**
     * Creation of a new extended pattern formatter.
     *
     * @param format the format string
     * @param callStackOffset the offset
     */
    public ExtendedPatternFormatter( final String format, final int callStackOffset )
    {
        super( format );
        m_callStackOffset = callStackOffset;
    }

    /**
     * Retrieve the type-id for a particular string.
     *
     * @param type the string
     * @return the type-id
     */
    protected int getTypeIdFor( final String type )
    {
        if( type.equalsIgnoreCase( TYPE_METHOD_STR ) )
        {
            return TYPE_METHOD;
        }
        else if( type.equalsIgnoreCase( TYPE_THREAD_STR ) )
        {
            return TYPE_THREAD;
        }
        else
        {
            return super.getTypeIdFor( type );
        }
    }

    /**
     * Formats a single pattern run (can be extended in subclasses).
     *
     * @param event the log event
     * @param  run the pattern run to format.
     * @return the formatted result.
     */
    protected String formatPatternRun( final LogEvent event, final PatternRun run )
    {
        switch( run.m_type )
        {
            case TYPE_METHOD:
                return getMethod( event );
            case TYPE_THREAD:
                return getThread( event );
            default:
                return super.formatPatternRun( event, run );
        }
    }

    /**
     * Utility method to format category.
     *
     * @param event the event
     * @return the formatted string
     */
    private String getMethod( final LogEvent event )
    {
        final ContextMap map = event.getContextMap();
        if( null != map )
        {
            final Object object = map.get( "method" );
            if( null != object )
            {
                return object.toString();
            }
        }

        //Determine callee of user's class.  If offset is 0, we need to find
        // Logger.class.  If offset is 1, We need to find caller of Logger.class, etc.
        final Class clazz = StackIntrospector.getCallerClass( Logger.class, m_callStackOffset - 1 );
        if( null == clazz )
        {
            return "UnknownMethod";
        }

        final String result = StackIntrospector.getCallerMethod( clazz );
        if( null == result )
        {
            return "UnknownMethod";
        }
        return result;
    }

    /**
     * Utility thread to format category.
     *
     * @param event the even
     * @return the formatted string
     */
    private String getThread( final LogEvent event )
    {
        final ContextMap map = event.getContextMap();
        if( null != map )
        {
            final Object object = map.get( "thread" );
            if( null != object )
            {
                return object.toString();
            }
        }

        return Thread.currentThread().getName();
    }
}
