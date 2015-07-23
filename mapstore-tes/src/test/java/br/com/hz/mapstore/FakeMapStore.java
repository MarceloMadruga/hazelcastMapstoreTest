package br.com.hz.mapstore;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.hazelcast.core.MapStore;

public class FakeMapStore implements MapStore<String, String> {

	public String load(String key) {
		return "dummy";
	}

	public Map<String, String> loadAll(Collection<String> keys) {
		return new HashMap<String, String>();
	}

	public Iterable<String> loadAllKeys() {
		return new HashSet<String>();
	}

	public void store(String key, String value) {
	}

	public void storeAll(Map<String, String> map) {
	}

	public void delete(String key) {
	}

	public void deleteAll(Collection<String> keys) {
	}

}
