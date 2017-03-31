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
package com.lh.org.apache.log.output.io.rotate;

import java.io.File;

/**
 * Hierarchical rotation strategy.
 * This object is initialised with several rotation strategy objects.
 * The <code>isRotationNeeded</code> method checks the first rotation
 * strategy object. If a rotation is needed, this result is returned.
 * If not, the next rotation strategy object is checked, and so on.
 *
 * @author <a href="mailto:cziegeler@apache.org">Carsten Ziegeler</a>
 */
public class OrRotateStrategy
    implements RotateStrategy
{
    private RotateStrategy[] m_strategies;

    /** The rotation strategy used. This marker is required for the reset()
     *  method.
     */
    private int m_usedRotation = -1;

    /**
     * Constructor
     * @param strategies the set of rotation strategies
     */
    public OrRotateStrategy( final RotateStrategy[] strategies )
    {
        this.m_strategies = strategies;
    }

    /**
     * reset.
     */
    public void reset()
    {
        for( int i = 0; i < m_strategies.length; i++ )
        {
            m_strategies[ i ].reset();
        }
    }

    /**
     *  check if now a log rotation is neccessary.
     *  This object is initialised with several rotation strategy objects.
     *  The <code>isRotationNeeded</code> method checks the first rotation
     *  strategy object. If a rotation is needed, this result is returned.
     *  If not the next rotation strategy object is asked and so on.
     *  @param data the last message written to the log system
     *  @param file ???
     *  @return boolean return true if log rotation is neccessary, else false
     */
    public boolean isRotationNeeded( final String data, final File file )
    {
        m_usedRotation = -1;

        if( null != m_strategies )
        {
            final int length = m_strategies.length;
            for( int i = 0; i < length; i++ )
            {
                if( true == m_strategies[ i ].isRotationNeeded( data, file ) )
                {
                    m_usedRotation = i;
                    return true;
                }
            }
        }

        return false;
    }
}

