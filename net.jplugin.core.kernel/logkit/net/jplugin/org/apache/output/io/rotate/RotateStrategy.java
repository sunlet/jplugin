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
package net.jplugin.org.apache.output.io.rotate;

import java.io.File;

/**
 * Strategy that checks condition under which file rotation is needed.
 *
 * @author <a href="mailto:leo.sutic@inspireinfrastructure.com">Leo Sutic</a>
 * @author <a href="mailto:bh22351@i-one.at">Bernhard Huber</a>
 */
public interface RotateStrategy
{
    /**
     * Reset cumulative rotation history data.
     * Called after rotation.
     */
    void reset();

    /**
     * Check if a log rotation is neccessary at this time.
     *
     * @param data the serialized version of the message about to be written
     *             to the log system
     * @param file the File that we are writing to
     * @return boolean return true if log rotation is neccessary, else false
     */
    boolean isRotationNeeded( String data, File file );
}

