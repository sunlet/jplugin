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
 * Rotation strategy based on size written to log file.
 * The strategy will signal that a rotation is needed if the
 * size goes above a set limit. Due to performance reasons
 * the limit is not strictly enforced, however, the strategy has
 * at most an error of the longest single data message written to the
 * logging system. The error will occur immediately after a rotation,
 * when the strategy is reset and the data that triggered the
 * rotation is written. The strategy's internal counter will then
 * be off with data.length() bytes.
 *
 * @author <a href="mailto:leo.sutic@inspireinfrastructure.com">Leo Sutic</a>
 * @author <a href="mailto:bh22351@i-one.at">Bernhard Huber</a>
 */
public class RotateStrategyBySize
    implements RotateStrategy
{
    private long m_maxSize;
    private long m_currentSize;

    /**
     * Rotate logs by size.
     * By default do log rotation before writing approx. 1MB of messages
     */
    public RotateStrategyBySize()
    {
        this( 1024 * 1024 );
    }

    /**
     *  Rotate logs by size.
     *
     *  @param maxSize rotate before writing maxSize [byte] of messages
     */
    public RotateStrategyBySize( final long maxSize )
    {
        m_currentSize = 0;
        m_maxSize = maxSize;
    }

    /**
     * Reset log size written so far.
     */
    public void reset()
    {
        m_currentSize = 0;
    }

    /**
     *  Check if now a log rotation is neccessary.
     *
     *  @param data the message about to be written to the log system
     *  @return boolean return true if log rotation is neccessary, else false
     *  @param file not used
     */
    public boolean isRotationNeeded( final String data, final File file )
    {
        m_currentSize += data.length();

        return m_currentSize >= m_maxSize;
    }
}

