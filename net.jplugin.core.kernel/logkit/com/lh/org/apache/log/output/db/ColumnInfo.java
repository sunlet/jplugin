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
package com.lh.org.apache.log.output.db;

/**
 * A descriptor for each column stored in table.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @author Peter Donald
 */
public class ColumnInfo
{
    ///Name of column
    private final String m_name;

    ///Type/Source of column
    private final int m_type;

    ///Auxilliary parameters (ie constant or sub-format)
    private final String m_aux; //may be null

    /**
     * Creation of a new column info instance.
     * @param name the column name
     * @param type the column type
     * @param aux the auxillary value
     */
    public ColumnInfo( final String name, final int type, final String aux )
    {
        m_name = name;
        m_type = type;
        m_aux = aux;
    }

    /**
     * Return the column name
     * @return the name of the column
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the column type as an integer
     * @return the type
     */
    public int getType()
    {
        return m_type;
    }

    /**
     * Return the auxillary column information.
     * @return the information
     */
    public String getAux()
    {
        return m_aux;
    }
}

