package com.thebubblenetwork.api.global.data;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Jacob on 31/12/2015.
 */
public class PlayerData extends DataObject {

    public static final String NICKNAME = "nickname", RANKBASE = "rank", STATSBASE = "stats", FRIENDSBASE = "friends", ITEMSBASE = "items", PACKS = "packs", KITBASE = "kits", CURRENCYBASE = "currency", NAME = "name", TOKENS = "tokens", KEYS = "keys", MAINRANK = RANKBASE + ".mainrank", SUBRANKS = RANKBASE + ".subranks", FRIENDSLIST = FRIENDSBASE + ".current", FRIENDINCOMINGRQ = FRIENDSBASE + ".incoming", PETNAME = "petname", GADGETS = "gadgets";

    public static String table = "playerdata";

    public PlayerData(Map<String, String> loaded) {
        super(loaded);
    }

    public UUID[] getUUIDList(String indentifier) throws InvalidBaseException {
        String[] list = getString(indentifier).split(",");
        Set<UUID> uuids = new HashSet<>();
        for (String s : list) {
            try {
                uuids.add(UUID.fromString(s));
            } catch (Exception ex) {
            }
        }
        return uuids.toArray(new UUID[uuids.size()]);
    }

    public Map<String, Integer> getMapRaw(final String indentifier) {
        return new InternalAbstractMap<String, Integer>() {
            public Integer getInternal(String s) {
                try {
                    return getNumber(indentifier + "." + s).intValue();
                } catch (InvalidBaseException e) {
                    return null;
                }
            }

            public Integer removeInternal(String key) {
                String s = getRaw().remove(indentifier + "." + key);
                if(s != null) {
                    try {
                        return NumberFormat.getInstance().parse(s).intValue();
                    } catch (ParseException e) {
                    }
                }
                return null;
            }

            public int size() {
                int size = 0;
                for(String s:getRaw().values()){
                    if(s.startsWith(indentifier)){
                        size++;
                    }
                }
                return size;
            }

            public Integer put(String key, Integer value) {
                String before = getRaw().put(indentifier + "." + key,String.valueOf(value));
                if(before != null) {
                    try {
                        return NumberFormat.getInstance().parse(before).intValue();
                    } catch (ParseException e) {
                    }
                }
                return null;
            }

            public void clear() {
                Iterator<String> iterator = getRaw().values().iterator();
                String s;
                while(iterator.hasNext()){
                    s = iterator.next();
                    if(s.startsWith(indentifier)){
                        iterator.remove();
                    }
                }
            }
        };
    }

    public Map<String, Integer> getMap(String id, String uid) {
        String indentifier = id + "." + uid.toLowerCase();
        return getMapRaw(indentifier);
    }

    public abstract static class InternalAbstractMap<K,V> implements Map<K,V>{
        public abstract V getInternal(K k);
        public abstract V removeInternal(K key);

        public boolean isEmpty() {
            return size() == 0;
        }

        @SuppressWarnings("unchecked")
        public boolean containsKey(Object key) {
            return false;
        }

        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        public V get(Object key) {
            return getInternal((K)key);
        }

        @SuppressWarnings("unchecked")
        public V remove(Object key) {
            return removeInternal((K)key);
        }

        public Set<K> keySet() {
            throw new UnsupportedOperationException();
        }

        public Collection<V> values() {
            throw new UnsupportedOperationException();
        }

        public Set<Entry<K, V>> entrySet() {
            throw new UnsupportedOperationException();
        }
    }
}
