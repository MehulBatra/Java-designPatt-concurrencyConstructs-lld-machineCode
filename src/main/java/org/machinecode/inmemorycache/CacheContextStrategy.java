package org.machinecode.inmemorycache;
/*
OPTIONAL CLASS CAN USE THIS OR FACTORY

Context Class (CacheContextStrategy)

Holds a reference to CacheT.
Allows dynamic switching via setCacheStrategy().

✅ These methods hide the caching mechanism behind CacheContext.
✅ They delegate calls to the underlying strategy while allowing dynamic switching.
 */
public class CacheContextStrategy {

        private CacheT cacheStrategy;

        public CacheContextStrategy(CacheT initialStrategy) {
            this.cacheStrategy = initialStrategy;
        }

        public void setCacheStrategy(CacheT newStrategy) {
            this.cacheStrategy = newStrategy;
            System.out.println("Switched to: " + newStrategy.toString());
        }

        public int get(int key) {
            return cacheStrategy.get(key);
        }

        public void put(int key, int value) {
            cacheStrategy.put(key, value);
        }

}
