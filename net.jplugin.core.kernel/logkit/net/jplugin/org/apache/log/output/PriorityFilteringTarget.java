/* 
 * Copyright 2004 The Apache Software Foundation
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
import net.jplugin.org.apache.util.Closeable;

/**
 * This is a priority filtering target that forwards only requests
 * to other (wrapped) targets that have the same or a higher
 * priority.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 */
public class PriorityFilteringTarget
    extends AbstractTarget
{
    private final Priority m_priority;

    private final boolean m_closeWrapped;

    /** Log targets in filter chain */
    private LogTarget m_targets[];

    /**
     * @param priority The priority used to filter
     * @param closeWrappedTarget see AbstractWrappingTarget
     */
    public PriorityFilteringTarget(Priority priority,
                                    boolean closeWrappedTarget) 
    {
        m_priority = priority;
        m_closeWrapped = closeWrappedTarget;
        open();
    }

    /**
     * @param priority The priority used to filter
     */
    public PriorityFilteringTarget(Priority priority)
    {
        this(priority, false );
    }

    /**
     * Add a new target to output chain.
     *
     * @param target the target
     */
    public void addTarget( final LogTarget target )
    {
        if( null == m_targets )
        {
            m_targets = new LogTarget[]{target};
        }
        else
        {
            final LogTarget oldTargets[] = m_targets;
            m_targets = new LogTarget[ oldTargets.length + 1 ];
            System.arraycopy( oldTargets, 0, m_targets, 0, oldTargets.length );
            m_targets[ m_targets.length - 1 ] = target;
        }
    }


    /* (non-Javadoc)
	 * @see org.apache.log.output.AbstractTarget#doProcessEvent(org.apache.log.LogEvent)
	 */
	protected void doProcessEvent(LogEvent event) throws Exception 
    {
		if ( event != null 
             && m_targets != null
             && !event.getPriority().isLower(m_priority) )
        {
            for( int i = 0; i < m_targets.length; i++ )
            {
                m_targets[ i ].processEvent( event );
            }
        }		
	}
    
    /* (non-Javadoc)
     * @see org.apache.log.util.Closeable#close()
     */
    public void close()
    {
        super.close();

        if( m_closeWrapped && m_targets != null )
        {
            for( int i = 0; i < m_targets.length; i++ )
            {
                if ( m_targets[i] instanceof Closeable )
                {
                    ( (Closeable)m_targets[i] ).close();                    
                }
            }
        }
    }
    
}
