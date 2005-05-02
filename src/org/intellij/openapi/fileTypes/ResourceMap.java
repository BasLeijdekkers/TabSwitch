package org.intellij.openapi.fileTypes;

import java.util.HashMap;
import java.util.Map;

public abstract class ResourceMap {

    private final Map resourceMap = new HashMap();

    protected ResourceMap() {
    }

    protected final Object getResource() {
        Object key = getKey();
        Object resource = resourceMap.get(key);
        if (resource == null) {
            resource = createResource();
            resourceMap.put(key, resource);
        }
        return resource;
    }

    protected abstract Object getKey();

    protected abstract Object createResource();

}
