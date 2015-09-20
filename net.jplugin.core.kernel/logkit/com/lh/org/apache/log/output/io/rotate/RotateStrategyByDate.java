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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Rotation stragety based on SimpleDateFormat.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author <a href="mailto:colus@apache.org">Eung-ju Park</a>
 * @version $Revision: 1.13 $ $Date: 2004/02/28 11:31:24 $
 */
public class RotateStrategyByDate
    implements RotateStrategy
{
    private SimpleDateFormat m_format;
    private Date m_date;
    private String m_current;

    /**
     * Creation of a new rotation strategy based on a date policy.
     */
    public RotateStrategyByDate()
    {
        this( "yyyyMMdd" );
    }

    /**
     * Creation of a new rotation strategy based on a date policy
     * using a supplied pattern.
     * @param pattern the message formatting pattern
     */
    public RotateStrategyByDate( final String pattern )
    {
        m_format = new SimpleDateFormat( pattern );
        m_date = new Date();
        m_current = m_format.format( m_date );
    }

    /**
     * Reset the strategy.
     */
    public void reset()
    {
        m_date.setTime( System.currentTimeMillis() );
        m_current = m_format.format( m_date );
    }

    /**
     * Test is a rotation is required.  Documentation pending ??
     *
     * @param data not used
     * @param file not used
     * @return TRUE if a rotation is required else FALSE
     */
    public boolean isRotationNeeded( final String data, final File file )
    {
        m_date.setTime( System.currentTimeMillis() );
        if( m_current.equals( m_format.format( m_date ) ) )
        {
            return false;
        }
        return true;
    }
}
