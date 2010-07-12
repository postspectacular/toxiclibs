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

    private static Logger logger =
            Logger.getLogger(SingletonRegistry.class.getName());

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
