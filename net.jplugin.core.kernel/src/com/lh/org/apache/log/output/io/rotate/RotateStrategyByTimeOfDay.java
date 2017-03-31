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
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Rotation stragety based on a specific time of day.
 *
 * @author <a href="mailto:leif@apache.org">Leif Mortenson</a>
 */
public class RotateStrategyByTimeOfDay
    implements RotateStrategy
{
    /** Constant that stores the the number of ms in 24 hours. */
    private static final long TIME_24_HOURS = 24 * 3600 * 1000;

    /** Time in ms that the current rotation started. */
    private long m_currentRotation;

    /**
     * Rotate logs at specific time of day.
     * By default do log rotation at 00:00:00 every day.
     */
    public RotateStrategyByTimeOfDay()
    {
        this( 0 );
    }

    /**
     * Rotate logs at specific time of day.
     *
     * @param time Offset in milliseconds into the day to perform the log rotation.
     */
    public RotateStrategyByTimeOfDay( final long time )
    {
        // Calculate the time at the beginning of the current day and add the time to that.
        final GregorianCalendar cal = new GregorianCalendar();
        cal.set( Calendar.MILLISECOND, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        m_currentRotation = cal.getTime().getTime() + time;

        // Make sure that the current rotation time is in the past.
        if( m_currentRotation > System.currentTimeMillis() )
        {
            m_currentRotation -= TIME_24_HOURS;
        }
    }

    /**
     * reset interval history counters.
     */
    public void reset()
    {
        final long now = System.currentTimeMillis();

        // Make sure the currentRotation time is set so that the current system
        //  time is within 24 hours.
        while( m_currentRotation + TIME_24_HOURS < now )
        {
            m_currentRotation += TIME_24_HOURS;
        }
    }

    /**
     * Check if now a log rotation is neccessary.
     * If the time of the current rotation + 24 hours is less than the current time.
     *  If not then a rotation is needed.
     *
     * @param data the last message written to the log system
     * @param file not used
     * @return boolean return true if log rotation is neccessary, else false
     */
    public boolean isRotationNeeded( final String data, final File file )
    {
        final long now = System.currentTimeMillis();
        if( m_currentRotation + TIME_24_HOURS < now )
        {
            // Needs to be rotated.
            return true;
        }
        else
        {
            return false;
        }
    }
}

