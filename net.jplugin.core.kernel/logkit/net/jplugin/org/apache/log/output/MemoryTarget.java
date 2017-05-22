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
package net.jplugin.org.apache.log.output;

import net.jplugin.org.apache.log.LogEvent;
import net.jplugin.org.apache.log.LogTarget;
import net.jplugin.org.apache.log.Priority;

/**
 * Output LogEvents into an buffer in memory.
 * At a later stage these LogEvents can be forwarded or
 * pushed to another target. This pushing is triggered
 * when buffer is full, the priority of a LogEvent reaches a threshold
 * or when another class calls the push method.
 *
 * This is based on specification of MemoryHandler in Logging JSR47.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public class MemoryTarget
    extends AbstractTarget
{
    ///Buffer for all the LogEvents
    private final LogEvent[] m_buffer;

    ///Priority at which to push LogEvents to next LogTarget
    private Priority m_threshold;

    ///Target to push LogEvents to
    private LogTarget m_target;

    ///Count of used events
    private int m_used;

    ///Position of last element inserted
    private int m_index;

    ///Flag indicating whether it is possible to overite elements in array
    private boolean m_overwrite;

    /**
     * Creation of a new instance of the memory target.
     * @param target the target to push LogEvents to
     * @param size the event buffer size
     * @param threshold the priority at which to push LogEvents to next LogTarget
     */
    public MemoryTarget( final LogTarget target,
                         final int size,
                         final Priority threshold )
    {
        m_target = target;
        m_buffer = new LogEvent[ size ];
        m_threshold = threshold;
        open();
    }

    /**
     * Set flag indicating whether it is valid to overwrite memory buffer.
     *
     * @param overwrite true if buffer should overwrite logevents in buffer, false otherwise
     */
    protected synchronized void setOverwrite( final boolean overwrite )
    {
        m_overwrite = overwrite;
    }

    /**
     * Process a log event, via formatting and outputting it.
     *
     * @param event the log event
     */
    protected synchronized void doProcessEvent( final LogEvent event )
    {
        //Check if it is full
        if( isFull() )
        {
            if( m_overwrite )
            {
                m_used--;
            }
            else
            {
                getErrorHandler().error( "Memory buffer is full", null, event );
                return;
            }
        }

        if( 0 == m_used )
        {
            m_index = 0;
        }
        else
        {
            m_index = ( m_index + 1 ) % m_buffer.length;
        }
        m_buffer[ m_index ] = event;
        m_used++;

        if( shouldPush( event ) )
        {
            push();
        }
    }

    /**
     * Check if memory buffer is full.
     *
     * @return true if buffer is full, false otherwise
     */
    public final synchronized boolean isFull()
    {
        return m_buffer.length == m_used;
    }

    /**
     * Determine if LogEvent should initiate a push to target.
     * Subclasses can overide this method to change the conditions
     * under which a push occurs.
     *
     * @param event the incoming LogEvent
     * @return true if should push, false otherwise
     */
    protected synchronized boolean shouldPush( final LogEvent event )
    {
        return ( m_threshold.isLowerOrEqual( event.getPriority() ) || isFull() );
    }

    /**
     * Push log events to target.
     */
    public synchronized void push()
    {
        if( null == m_target )
        {
            getErrorHandler().error( "Can not push events to a null target", null, null );
            return;
        }

        try
        {
            final int size = m_used;
            int base = m_index - m_used + 1;
            if( base < 0 )
            {
                base += m_buffer.length;
            }

            for( int i = 0; i < size; i++ )
            {
                final int index = ( base + i ) % m_buffer.length;

                //process event in buffer
                m_target.processEvent( m_buffer[ index ] );

                //help GC
                m_buffer[ index ] = null;
                m_used--;
            }
        }
        catch( final Throwable throwable )
        {
            getErrorHandler().error( "Unknown error pushing events.", throwable, null );
        }
    }
}
