package br.com.hz.mapstore;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class MapStoreTest {
	Config config = null;

	private HazelcastInstance getInstance() {
		HazelcastInstance instance = Hazelcast
				.newHazelcastInstance(localConfig());
		return instance;
	}

	Config localConfig() {
		if (config != null) {
			return config;
		}
		config = new Config();
		
		//Group config
		GroupConfig groupConfig = new GroupConfig()
		.setName("testGroup")
		.setPassword("pass");
		config.setGroupConfig(groupConfig);

		//Network config
		config.getNetworkConfig()
			.setPort(5901)
			.setPortAutoIncrement(false)
			.getJoin()
			.getTcpIpConfig()
			.setMembers(Arrays.asList(new String[] { "localhost:5901" }));

		//My map config
		MapConfig mapConfig = config.getMapConfig("myMap")
				.setEvictionPolicy(EvictionPolicy.LFU)
				.setMaxIdleSeconds(600);
		
		//My map mapstore config
		MapStoreConfig mapStoreConfig = new MapStoreConfig()
				.setImplementation(new FakeMapStore())
				.setEnabled(true)
				.setInitialLoadMode(InitialLoadMode.LAZY);
		
		mapConfig.setMapStoreConfig(mapStoreConfig);

		return config;
	}

	@Test
	public void test() {
		
		for (int i = 0; i < 10; i++) {
			try {
				// Initialize node
				HazelcastInstance instance = getInstance();
				
				IMap<String, String> map = instance.getMap("myMap");
				String key = UUID.randomUUID().toString();
				map.put(key, UUID.randomUUID().toString());
				map.get(key);
				
				// Stop node
				instance.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
