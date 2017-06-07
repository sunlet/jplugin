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
import java.io.FilenameFilter;

class BaseFileNameFilter
    implements FilenameFilter
{
    private String m_baseFileName;

    BaseFileNameFilter( final String baseFileName )
    {
        m_baseFileName = baseFileName;
    }

    public boolean accept( File file, String name )
    {
        return ( name.startsWith( m_baseFileName ) );
    }
}
