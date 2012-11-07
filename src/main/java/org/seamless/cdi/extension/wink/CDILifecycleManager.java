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

import org.apache.wink.common.internal.i18n.Messages;
import org.apache.wink.common.internal.lifecycle.LifecycleManager;
import org.apache.wink.common.internal.lifecycle.ObjectCreationException;
import org.apache.wink.common.internal.lifecycle.ObjectFactory;
import org.apache.wink.common.internal.registry.metadata.ApplicationMetadataCollector;
import org.apache.wink.common.internal.registry.metadata.ProviderMetadataCollector;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CDILifecycleManager<T> implements LifecycleManager<T> {

    protected BeanManager beanManager = null;

    public CDILifecycleManager(BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    public ObjectFactory<T> createObjectFactory(T object) throws ObjectCreationException {
        if (object == null)
            throw new NullPointerException(Messages.getMessage("variableIsNull", "object"));
        return null;
    }

    public ObjectFactory<T> createObjectFactory(Class<T> cls) throws ObjectCreationException {
        if (cls == null)
            throw new NullPointerException(Messages.getMessage("variableIsNull", "cls"));

        if (isCDIManagedBean(cls, beanManager)) {
            if (ProviderMetadataCollector.isProvider(cls) || ApplicationMetadataCollector.isApplication(cls))
                return new CDISingletonObjectFactory<T>(cls, beanManager);
            return new CDIDefaultObjectFactory<T>(cls, beanManager);
        }
        return null;
    }

    static <T> boolean isCDIManagedBean(Class<T> cls, BeanManager beanManager) {
        Annotation[] annotations = cls.getAnnotations();
        List<Annotation> qualifierAnnotations = new ArrayList<Annotation>(1);
        for (Annotation a : annotations) {
            if (beanManager.isQualifier(a.annotationType()))
                qualifierAnnotations.add(a);
        }
        Set<Bean<?>> beans = beanManager.getBeans(
           cls,
           qualifierAnnotations.toArray(new Annotation[qualifierAnnotations.size()])
        );
        return !(beans == null || beans.isEmpty());
    }

}
