/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.seamless.cdi.extension.wink;

import org.apache.wink.common.internal.lifecycle.CreationUtils;
import org.apache.wink.common.internal.registry.metadata.ApplicationMetadataCollector;
import org.apache.wink.common.internal.registry.metadata.ClassMetadata;
import org.apache.wink.common.internal.registry.metadata.ProviderMetadataCollector;
import org.apache.wink.common.internal.registry.metadata.ResourceMetadataCollector;
import org.apache.wink.common.internal.runtime.RuntimeContextTLS;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Set;
import java.util.logging.Logger;

public class WinkInjectionTarget<T> implements InjectionTarget<T> {

    final private static Logger log = Logger.getLogger(WinkInjectionTarget.class.getName());

    protected ClassMetadata classMetadata;

    final private InjectionTarget<T> delegate;

    static <T> ClassMetadata collectClassMetadata(final Class<T> cls) {
        ClassMetadata classMetadata = null;
        if (ProviderMetadataCollector.isProvider(cls)) {
            classMetadata = ProviderMetadataCollector.collectMetadata(cls);
        } else if (ResourceMetadataCollector.isResource(cls)) {
            classMetadata = ResourceMetadataCollector.collectMetadata(cls);
        } else if (ApplicationMetadataCollector.isApplication(cls)) {
            classMetadata = ApplicationMetadataCollector.collectMetadata(cls);
        }
        return classMetadata;
    }

    public WinkInjectionTarget(InjectionTarget<T> delegate) {
        this.delegate = delegate;
    }

    public void inject(final T instance, final CreationalContext<T> creationalContext) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                public Object run() throws PrivilegedActionException {
                    if (classMetadata == null)
                        classMetadata = collectClassMetadata(instance.getClass());
                    try {
                        CreationUtils.injectFields(
                           instance,
                           classMetadata,
                           RuntimeContextTLS.getRuntimeContext()
                        );
                    } catch (IOException ex) {
                        throw new PrivilegedActionException(ex);
                    }
                    return null;
                }
            });
        } catch (PrivilegedActionException ex) {
            throw new RuntimeException(ex);
        }
        delegate.inject(instance, creationalContext);
    }

    public void postConstruct(T instance) {
        delegate.postConstruct(instance);
    }

    public void preDestroy(T instance) {
        delegate.preDestroy(instance);
    }

    public void dispose(T instance) {
        delegate.dispose(instance);
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return delegate.getInjectionPoints();
    }

    public T produce(CreationalContext<T> creationalContext) {
        return delegate.produce(creationalContext);
    }
}
