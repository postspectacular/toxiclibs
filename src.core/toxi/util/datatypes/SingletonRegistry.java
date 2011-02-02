/*
 *   __               .__       .__  ._____.           
 * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
 * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
 *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
 *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
 *                   \/       \/             \/     \/ 
 *
 * Copyright (c) 2006-2011 Karsten Schmidt
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * http://creativecommons.org/licenses/LGPL/2.1/
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 */

package toxi.util.datatypes;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Implements a registry for dynamic singleton management. Use this registry
 * instead of using "new" to enforce singletons of any class with a visible
 * default constructor. The registry itself is implemented as singleton.
 */
public class SingletonRegistry {

    /**
     * The singleton instance of the registry itself.
     */
    public static final SingletonRegistry REGISTRY = new SingletonRegistry();

    private static HashMap<String, Object> map = new HashMap<String, Object>();

    private static Logger logger = Logger.getLogger(SingletonRegistry.class
            .getName());

    /**
     * Creates or returns an instance of the class requested by name.
     * 
     * @param className
     * @return class singleton instance
     */
    public static synchronized Object getInstanceOf(String className) {
        Object instance = map.get(className);
        if (instance != null) {
            return instance;
        }
        try {
            instance = Class.forName(className).newInstance();
            map.put(className, instance);
            logger.info("Created singleton: " + instance);
        } catch (ClassNotFoundException cnf) {
            logger.severe("Couldn't find class: " + className);
        } catch (InstantiationException ie) {
            logger.severe("Couldn't instantiate the class: " + className);
        } catch (IllegalAccessException ia) {
            logger.severe("Couldn't access class: " + className);
        }
        return instance;
    }

    /**
     * Alternative, more conventional accessor to the singleton instance of the
     * registry itself.
     * 
     * @return registry instance
     */
    public static SingletonRegistry getRegistry() {
        return REGISTRY;
    }

    protected SingletonRegistry() {
    }
}
