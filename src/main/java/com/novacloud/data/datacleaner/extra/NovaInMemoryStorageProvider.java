package com.novacloud.data.datacleaner.extra;

import org.eobjects.analyzer.storage.RowAnnotationFactory;
import org.eobjects.analyzer.storage.StorageProvider;

import java.util.*;

/**
 * StorageProvider that actually doesn't store data on disk, but only in memory.
 * This implementation is prone to out of memory errors, but is on the other
 * hand very quick for small jobs.
 * 
 * 
 */
public  class NovaInMemoryStorageProvider implements StorageProvider {

	private final int _storedRowsThreshold;
	
	public NovaInMemoryStorageProvider() {
		this(Config.StoredAnnotatedRowsThreshold);
	}

	public NovaInMemoryStorageProvider(int storedRowsThreshold) {
		_storedRowsThreshold = storedRowsThreshold;
	}

	@Override
	public <E> List<E> createList(Class<E> valueType) throws IllegalStateException {
		return new ArrayList<E>();
	}

	@Override
	public <K, V> Map<K, V> createMap(Class<K> keyType, Class<V> valueType) throws IllegalStateException {
		return new HashMap<K, V>();
	}

	@Override
	public <E> Set<E> createSet(Class<E> valueType) throws IllegalStateException {
		return new HashSet<E>();
	}

	@Override
	public RowAnnotationFactory createRowAnnotationFactory() {
		return new NovaInMemoryRowAnnotationFactory(_storedRowsThreshold);
	}
}
