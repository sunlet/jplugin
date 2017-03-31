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


import com.lh.org.apache.log.LogTarget;

import com.lh.org.apache.log.util.Closeable;

/**
 * Abstract base class for targets that wrap other targets. The class
 * provides functionality for optionally closing a wrapped target that
 * implements  <code>org.apache.log.util.Closeable</code>.
 *
 * @see com.lh.org.apache.log.util.Closeable
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 */
public abstract class AbstractWrappingTarget
    extends AbstractTarget
{
    private final boolean m_closeWrapped;
    private final LogTarget m_wrappedLogTarget;

    /**
     * Creation of a new wrapping log target.
     *
     * @param logTarget the underlying target
     * @param closeWrappedTarget boolean flag indicating whether the wrapped log target
     *        should be closed when this target is closed. Note: This flag has no
     *        effect unless the underlying target implements <code>org.apache.log.util.Closeable</code>.
     * @see com.lh.org.apache.log.util.Closeable
     */
    public AbstractWrappingTarget( final LogTarget logTarget, final boolean closeWrappedTarget )
    {
        m_wrappedLogTarget = logTarget;
        m_closeWrapped = closeWrappedTarget;
    }

    /**
     * Creation of a new wrapping log target. The underlying log target will
     * <b>not</b> be closed when this target is closed.
     *
     * @param logTarget the underlying target
     */
    public AbstractWrappingTarget( final LogTarget logTarget )
    {
        this( logTarget, false );
    }

    public void close()
    {
        super.close();

        if( m_closeWrapped && m_wrappedLogTarget instanceof Closeable )
        {
            ( (Closeable)m_wrappedLogTarget ).close();
        }
    }
    
    /**
     * Return the target for subclasses
     */
    protected final LogTarget getLogTarget() 
    {
        return m_wrappedLogTarget;   
    }
}