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
 * Strategy for naming log files.
 * For a given base file name an implementation calculates
 * the real file name.
 *
 * @author <a href="mailto:bh22351@i-one.at">Bernhard Huber</a>
 * @author Peter Donald
 */
public interface FileStrategy
{
    /**
     * Get the next log file to rotate to.
     *
     * @return the file to rotate to
     */
    File nextFile();
}


