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
package com.lh.org.apache.log.output;

import java.util.LinkedList;

import com.lh.org.apache.log.ErrorAware;
import com.lh.org.apache.log.ErrorHandler;
import com.lh.org.apache.log.LogEvent;
import com.lh.org.apache.log.LogTarget;

/**
 * An asynchronous LogTarget that sends entries on in another thread.
 * It is the responsibility of the user of this class to start
 * the thread etc.
 *
 * <pre>
 * LogTarget mySlowTarget = ...;
 * AsyncLogTarget asyncTarget = new AsyncLogTarget( mySlowTarget );
 * Thread thread = new Thread( asyncTarget );
 * thread.setPriority( Thread.MIN_PRIORITY );
 * thread.start();
 *
 * logger.setLogTargets( new LogTarget[] { asyncTarget } );
 * </pre>
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public class AsyncLogTarget
    extends AbstractWrappingTarget
    implements Runnable
{
    private final LinkedList m_list;
    private final int m_queueSize;

    /**
     * Creation of a new async log target.
     * @param logTarget the underlying target
     */
    public AsyncLogTarget( final LogTarget logTarget )
    {
        this( logTarget, 15 );
    }

    /**
     * Creation of a new async log target.
     * @param logTarget the underlying target
     * @param queueSize the queue size
     */
    public AsyncLogTarget( final LogTarget logTarget, final int queueSize )
    {
        this( logTarget, queueSize, false );
    }

    /**
     * Creation of a new async log target.
     * @param logTarget the underlying target
     * @param closeTarget close the underlying target when this target is closed. This flag
     *        has no effect unless the logTarget implements Closeable.
     */
    public AsyncLogTarget( final LogTarget logTarget, final boolean closeTarget )
    {
        this( logTarget, 15, closeTarget );
    }

    /**
     * Creation of a new async log target.
     * @param logTarget the underlying target
     * @param queueSize the queue size
     * @param closeTarget close the underlying target when this target is closed. This flag
     *        has no effect unless the logTarget implements Closeable.
     */
    public AsyncLogTarget( final LogTarget logTarget, final int queueSize, final boolean closeTarget )
    {
        super( logTarget, closeTarget );
        m_list = new LinkedList();
        m_queueSize = queueSize;
        open();
    }

    /**
     * Provide component with ErrorHandler.
     *
     * @param errorHandler the errorHandler
     */
    public synchronized void setErrorHandler( final ErrorHandler errorHandler )
    {
        super.setErrorHandler( errorHandler );

        if( this.getLogTarget() instanceof ErrorAware )
        {
            ( (ErrorAware)this.getLogTarget() ).setErrorHandler( errorHandler );
        }
    }

    /**
     * Process a log event by adding it to queue.
     *
     * @param event the log event
     */
    public void doProcessEvent( final LogEvent event )
    {
        synchronized( m_list )
        {
            int size = m_list.size();
            while( m_queueSize <= size )
            {
                try
                {
                    m_list.wait();
                }
                catch( final InterruptedException ie )
                {
                    //This really should not occur ...
                    //Maybe we should log it though for
                    //now lets ignore it
                }
                size = m_list.size();
            }

            m_list.addFirst( event );

            if( size == 0 )
            {
                //tell the "server" thread to wake up
                //if it is waiting for a queue to contain some items
                m_list.notify();
            }
        }
    }

    /**
     * Thread startup.
     */
    public void run()
    {
        //set this variable when thread is interupted
        //so we know we can shutdown thread soon.
        boolean interupted = false;

        while( true )
        {
            LogEvent event = null;

            synchronized( m_list )
            {
                while( null == event )
                {
                    final int size = m_list.size();

                    if( size > 0 )
                    {
                        event = (LogEvent)m_list.removeLast();

                        if( size == m_queueSize )
                        {
                            //tell the "client" thread to wake up
                            //if it is waiting for a queue position to open up
                            m_list.notify();
                        }

                    }
                    else if( interupted || Thread.interrupted() )
                    {
                        //ie there is nothing in queue and thread is interrupted
                        //thus we stop thread
                        return;
                    }
                    else
                    {
                        try
                        {
                            m_list.wait();
                        }
                        catch( final InterruptedException ie )
                        {
                            //Ignore this and let it be dealt in next loop
                            //Need to set variable as the exception throw cleared status
                            interupted = true;
                        }
                    }
                }
            }

            try
            {
                //actually process an event
                this.getLogTarget().processEvent( event );
            }
            catch( final Throwable throwable )
            {
                getErrorHandler().error( "Unknown error writing event.", throwable, event );
            }
        }
    }
}
