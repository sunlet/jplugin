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
package net.jplugin.org.apache.log;

import net.jplugin.org.apache.util.LoggerListener;

/**
 * The object interacted with by client objects to perform logging.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public class Logger
{
    private static final Logger[] EMPTY_SET = new Logger[ 0 ];

    /**
     * Separator character use to separate different categories
     */
    public static final char CATEGORY_SEPARATOR = '.';

    // The ErrorHandler associated with Logger
    private final ErrorHandler m_errorHandler;

    ///The ErrorHandler associated with Logger
    private final LoggerListener m_loggerListener;

    ///Logger to inherit logtargets and priorities from
    private final Logger m_parent;

    ///the fully qualified name of category
    private final String m_category;

    ///The list of child loggers associated with this logger
    private Logger[] m_children;

    ///The log-targets this logger writes to
    private LogTarget[] m_logTargets;

    ///Indicate that logTargets were set with setLogTargets() rather than inherited
    private boolean m_logTargetsForceSet;

    ///The priority threshold associated with logger
    private Priority m_priority;

    ///Indicate that priority was set with setPriority() rather than inherited
    private boolean m_priorityForceSet;

    /**
     * True means LogEvents will be sent to parents LogTargets
     * aswell as the ones set for this Logger.
     */
    private boolean m_additivity;

    /**
     * Protected constructor for use inside the logging toolkit.
     * You should not be using this constructor directly.
     *
     * @param errorHandler the ErrorHandler logger uses to log errors
     * @param category the fully qualified name of category
     * @param logTargets the LogTargets associated with logger
     * @param parent the parent logger (used for inheriting from)
     */
    Logger( final ErrorHandler errorHandler,
            final LoggerListener loggerListener,
            final String category,
            final LogTarget[] logTargets,
            final Logger parent )
    {
        m_errorHandler = errorHandler;
        m_loggerListener = loggerListener;
        m_category = category;
        m_logTargets = logTargets;
        m_parent = parent;

        if( null == m_logTargets )
        {
            unsetLogTargets();
        }

        unsetPriority();
    }

    /**
     * Determine if messages of priority DEBUG will be logged.
     *
     * @return true if DEBUG messages will be logged
     */
    public final boolean isDebugEnabled()
    {
        return m_priority.isLowerOrEqual( Priority.DEBUG );
    }

    /**
     * Log a debug priority event.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public final void debug( final String message, final Throwable throwable )
    {
        if( isDebugEnabled() )
        {
            output( Priority.DEBUG, message, throwable );
        }
    }

    /**
     * Log a debug priority event.
     *
     * @param message the message
     */
    public final void debug( final String message )
    {
        if( isDebugEnabled() )
        {
            output( Priority.DEBUG, message, null );
        }
    }

    /**
     * Determine if messages of priority INFO will be logged.
     *
     * @return true if INFO messages will be logged
     */
    public final boolean isInfoEnabled()
    {
        return m_priority.isLowerOrEqual( Priority.INFO );
    }

    /**
     * Log a info priority event.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public final void info( final String message, final Throwable throwable )
    {
        if( isInfoEnabled() )
        {
            output( Priority.INFO, message, throwable );
        }
    }

    /**
     * Log a info priority event.
     *
     * @param message the message
     */
    public final void info( final String message )
    {
        if( isInfoEnabled() )
        {
            output( Priority.INFO, message, null );
        }
    }

    /**
     * Determine if messages of priority WARN will be logged.
     *
     * @return true if WARN messages will be logged
     */
    public final boolean isWarnEnabled()
    {
        return m_priority.isLowerOrEqual( Priority.WARN );
    }

    /**
     * Log a warn priority event.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public final void warn( final String message, final Throwable throwable )
    {
        if( isWarnEnabled() )
        {
            output( Priority.WARN, message, throwable );
        }
    }

    /**
     * Log a warn priority event.
     *
     * @param message the message
     */
    public final void warn( final String message )
    {
        if( isWarnEnabled() )
        {
            output( Priority.WARN, message, null );
        }
    }

    /**
     * Determine if messages of priority ERROR will be logged.
     *
     * @return true if ERROR messages will be logged
     */
    public final boolean isErrorEnabled()
    {
        return m_priority.isLowerOrEqual( Priority.ERROR );
    }

    /**
     * Log a error priority event.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public final void error( final String message, final Throwable throwable )
    {
        if( isErrorEnabled() )
        {
            output( Priority.ERROR, message, throwable );
        }
    }

    /**
     * Log a error priority event.
     *
     * @param message the message
     */
    public final void error( final String message )
    {
        if( isErrorEnabled() )
        {
            output( Priority.ERROR, message, null );
        }
    }

    /**
     * Determine if messages of priority FATAL_ERROR will be logged.
     *
     * @return true if FATAL_ERROR messages will be logged
     */
    public final boolean isFatalErrorEnabled()
    {
        return m_priority.isLowerOrEqual( Priority.FATAL_ERROR );
    }

    /**
     * Log a fatalError priority event.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public final void fatalError( final String message, final Throwable throwable )
    {
        if( isFatalErrorEnabled() )
        {
            output( Priority.FATAL_ERROR, message, throwable );
        }
    }

    /**
     * Log a fatalError priority event.
     *
     * @param message the message
     */
    public final void fatalError( final String message )
    {
        if( isFatalErrorEnabled() )
        {
            output( Priority.FATAL_ERROR, message, null );
        }
    }

    /**
     * Make this logger additive. I.e. Send all log events to parent
     * loggers LogTargets regardless of whether or not the
     * LogTargets have been overidden.
     *
     * This is derived from Log4js notion of Additivity.
     *
     * @param additivity true to make logger additive, false otherwise
     */
    public final void setAdditivity( final boolean additivity )
    {
        m_additivity = additivity;
    }

    /**
     * Determine if messages of priority 锟絯ill be logged.
     * @param priority the priority
     * @return true if messages will be logged
     */
    public final boolean isPriorityEnabled( final Priority priority )
    {
        return m_priority.isLowerOrEqual( priority );
    }

    /**
     * Log a event at specific priority with a certain message and throwable.
     *
     * @param priority the priority
     * @param message the message
     * @param throwable the throwable
     */
    public final void log( final Priority priority,
                           final String message,
                           final Throwable throwable )
    {
        if( m_priority.isLowerOrEqual( priority ) )
        {
            output( priority, message, throwable );
        }
    }

    /**
     * Log a event at specific priority with a certain message.
     *
     * @param priority the priority
     * @param message the message
     */
    public final void log( final Priority priority, final String message )
    {
        if( m_priority.isLowerOrEqual( priority ) )
        {
            log( priority, message, null );
        }
    }

    /**
     * Set the priority for this logger.
     *
     * @param priority the priority
     */
    public synchronized void setPriority( final Priority priority )
    {
        m_priority = priority;
        m_priorityForceSet = true;
        resetChildPriorities( false );
    }

    /**
     * Unset the priority of Logger.
     * (Thus it will use it's parent's priority or DEBUG if no parent.
     */
    public synchronized void unsetPriority()
    {
        unsetPriority( false );
    }

    /**
     * Unset the priority of Logger.
     * (Thus it will use it's parent's priority or DEBUG if no parent.
     * If recursive is true unset priorities of all child loggers.
     *
     * @param recursive true to unset priority of all child loggers
     */
    public synchronized void unsetPriority( final boolean recursive )
    {
        if( null != m_parent )
        {
            m_priority = m_parent.m_priority;
        }
        else
        {
            m_priority = Priority.DEBUG;
        }

        m_priorityForceSet = false;
        resetChildPriorities( recursive );
    }

    /**
     * Set the log targets for this logger.
     *
     * @param logTargets the Log Targets
     */
    public synchronized void setLogTargets( final LogTarget[] logTargets )
    {
        if( null != logTargets )
        {
            //Make sure that the array passed in does not have any
            //nulls in it before we actually do the assignment
            for( int i = 0; i < logTargets.length; i++ )
            {
                if( null == logTargets[ i ] )
                {
                    final String message = "logTargets[ " + i + " ]";
                    throw new NullPointerException( message );
                }
            }
        }

        m_logTargets = logTargets;

        setupErrorHandlers();
        m_logTargetsForceSet = true;
        resetChildLogTargets( false );
    }

    /**
     * Unset the logtargets for this logger.
     * This logger (and thus all child loggers who don't specify logtargets) will
     * inherit from the parents LogTargets.
     */
    public synchronized void unsetLogTargets()
    {
        unsetLogTargets( false );
    }

    /**
     * Unset the logtargets for this logger and all child loggers if recursive is set.
     * The loggers unset (and all child loggers who don't specify logtargets) will
     * inherit from the parents LogTargets.
     * @param recursive the recursion policy
     */
    public synchronized void unsetLogTargets( final boolean recursive )
    {
        if( null != m_parent )
        {
            m_logTargets = m_parent.safeGetLogTargets();
        }
        else
        {
            m_logTargets = null;
        }

        m_logTargetsForceSet = false;
        resetChildLogTargets( recursive );
    }

    /**
     * Get all the child Loggers of current logger.
     *
     * @return the child loggers
     */
    public synchronized Logger[] getChildren()
    {
        if( null == m_children )
        {
            return EMPTY_SET;
        }

        final Logger[] children = new Logger[ m_children.length ];

        for( int i = 0; i < children.length; i++ )
        {
            children[ i ] = m_children[ i ];
        }

        return children;
    }

    /**
     * Create a new child logger.
     * The category of child logger is [current-category].subcategory
     *
     * @param subCategory the subcategory of this logger
     * @return the new logger
     * @exception IllegalArgumentException if subCategory has an empty element name
     */
    public synchronized Logger getChildLogger( final String subCategory )
        throws IllegalArgumentException
    {
        final int end = subCategory.indexOf( CATEGORY_SEPARATOR );

        String nextCategory = null;
        String remainder = null;

        if( -1 == end )
        {
            nextCategory = subCategory;
        }
        else
        {
            if( end == 0 )
            {
                throw new IllegalArgumentException( "Logger categories MUST not have empty elements" );
            }

            nextCategory = subCategory.substring( 0, end );
            remainder = subCategory.substring( end + 1 );
        }

        //Get FQN for category
        String category = null;
        if( m_category.equals( "" ) )
        {
            category = nextCategory;
        }
        else
        {
            category = m_category + CATEGORY_SEPARATOR + nextCategory;
        }

        //Check existing children to see if they
        //contain next Logger for step in category
        if( null != m_children )
        {
            for( int i = 0; i < m_children.length; i++ )
            {
                if( m_children[ i ].m_category.equals( category ) )
                {
                    if( null == remainder )
                    {
                        return m_children[ i ];
                    }
                    else
                    {
                        return m_children[ i ].getChildLogger( remainder );
                    }
                }
            }
        }

        //Create new logger
        final Logger child =
            new Logger( m_errorHandler, m_loggerListener, category, null, this );

        if( m_additivity )
        {
            child.setAdditivity( true );
        }

        m_loggerListener.loggerCreated( child.m_category, child );


        //Add new logger to child list
        if( null == m_children )
        {
            m_children = new Logger[]{child};
        }
        else
        {
            final Logger[] children = new Logger[ m_children.length + 1 ];
            System.arraycopy( m_children, 0, children, 0, m_children.length );
            children[ m_children.length ] = child;
            m_children = children;
        }

        if( null == remainder )
        {
            return child;
        }
        else
        {
            return child.getChildLogger( remainder );
        }
    }

    /**
     * Internal method to do actual outputting.
     *
     * @param priority the priority
     * @param message the message
     * @param throwable the throwable
     */
    private final void output( final Priority priority,
                               final String message,
                               final Throwable throwable )
    {
        final LogEvent event = new LogEvent();
        event.setCategory( m_category );
        event.setContextMap( ContextMap.getCurrentContext( false ) );

        if( null != message )
        {
            event.setMessage( message );
        }
        else
        {
            event.setMessage( "" );
        }

        event.setThrowable( throwable );
        event.setPriority( priority );

        //this next line can kill performance. It may be wise to
        //disable it sometimes and use a more granular approach
        event.setTime( System.currentTimeMillis() );

        output( event );
    }

    private final void output( final LogEvent event )
    {
        //cache a copy of targets for thread safety
        //It is now possible for another thread
        //to replace m_logTargets
        final LogTarget[] targets = m_logTargets;

        if( null == targets )
        {
            final String message = "LogTarget is null for category '" + m_category + "'";
            m_errorHandler.error( message, null, event );
        }
        else if( !m_additivity )
        {
            fireEvent( event, targets );
        }
        else
        {
            //If log targets were not inherited, additivity is true
            //then fire an event to local targets
            if( m_logTargetsForceSet )
            {
                fireEvent( event, targets );
            }

            //if we have a parent Logger then send log event to parent
            if( null != m_parent )
            {
                m_parent.output( event );
            }
        }
    }

    private final void fireEvent( final LogEvent event, final LogTarget[] targets )
    {
        for( int i = 0; i < targets.length; i++ )
        {
            //No need to clone array as addition of a log-target
            //will result in changin whole array
            targets[ i ].processEvent( event );
        }
    }

    /**
     * Update priority of children if any.
     */
    private synchronized void resetChildPriorities( final boolean recursive )
    {
        if( null == m_children )
        {
            return;
        }

        final Logger[] children = m_children;

        for( int i = 0; i < children.length; i++ )
        {
            children[ i ].resetPriority( recursive );
        }
    }

    /**
     * Update priority of this Logger.
     * If this loggers priority was manually set then ignore
     * otherwise get parents priority and update all children's priority.
     *
     */
    private synchronized void resetPriority( final boolean recursive )
    {
        if( recursive )
        {
            m_priorityForceSet = false;
        }
        else if( m_priorityForceSet )
        {
            return;
        }

        m_priority = m_parent.m_priority;
        resetChildPriorities( recursive );
    }

    /**
     * Retrieve logtarget array contained in logger.
     * This method is provided so that child Loggers can access a
     * copy of  parents LogTargets.
     *
     * @return the array of LogTargets
     */
    private synchronized LogTarget[] safeGetLogTargets()
    {
        if( null == m_logTargets )
        {
            if( null == m_parent )
            {
                return new LogTarget[ 0 ];
            }
            else
            {
                return m_parent.safeGetLogTargets();
            }
        }
        else
        {
            final LogTarget[] logTargets = new LogTarget[ m_logTargets.length ];
            for( int i = 0; i < logTargets.length; i++ )
            {
                logTargets[ i ] = m_logTargets[ i ];
            }

            return logTargets;
        }
    }

    /**
     * Update logTargets of children if any.
     */
    private synchronized void resetChildLogTargets( final boolean recursive )
    {
        if( null == m_children )
        {
            return;
        }

        for( int i = 0; i < m_children.length; i++ )
        {
            m_children[ i ].resetLogTargets( recursive );
        }
    }

    /**
     * Set ErrorHandlers of LogTargets if necessary.
     */
    private synchronized void setupErrorHandlers()
    {
        if( null == m_logTargets )
        {
            return;
        }

        for( int i = 0; i < m_logTargets.length; i++ )
        {
            final LogTarget target = m_logTargets[ i ];
            if( target instanceof ErrorAware )
            {
                ( (ErrorAware)target ).setErrorHandler( m_errorHandler );
            }
        }
    }

    /**
     * Update logTarget of this Logger.
     * If this loggers logTarget was manually set then ignore
     * otherwise get parents logTarget and update all children's logTarget.
     *
     */
    private synchronized void resetLogTargets( final boolean recursive )
    {
        if( recursive )
        {
            m_logTargetsForceSet = false;
        }
        else if( m_logTargetsForceSet )
        {
            return;
        }

        m_logTargets = m_parent.safeGetLogTargets();
        resetChildLogTargets( recursive );
    }
}
