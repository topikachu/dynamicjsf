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

import org.apache.wink.common.RuntimeContext;
import org.apache.wink.common.internal.lifecycle.ObjectFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class CDIDefaultObjectFactory<T> implements ObjectFactory<T> {

    final protected Class<T> clazz;
    final protected BeanManager beanManager;
    private Bean<?> theBean;
    private IdentityHashMap<T, CreationalContext<T>> creationalContextMap;

    public CDIDefaultObjectFactory(Class<T> c, BeanManager beanManager) {
        this.clazz = c;
        this.beanManager = beanManager;
        theBean = null;
    }

    public T getInstance(RuntimeContext context) {
        if (theBean == null) {
            // cache the Bean object
            Annotation[] annotations = clazz.getAnnotations();
            List<Annotation> qualifierAnnotations = new ArrayList<Annotation>(1);
            for (Annotation a : annotations) {
                if (beanManager.isQualifier(a.annotationType())) {
                    qualifierAnnotations.add(a);
                }
            }
            Set<Bean<?>> beans = beanManager.getBeans(
               clazz, qualifierAnnotations.toArray(new Annotation[qualifierAnnotations.size()])
            );
            // TODO: I'm pretty sure this is wrong, shouldn't there be some proper resolution?
            theBean = beans.iterator().next();
        }

        CreationalContext<?> creationalContext = beanManager.createCreationalContext(theBean);
        T instance = (T) beanManager.getReference(theBean, clazz, creationalContext);

        if (context != null) {
            context.setAttribute(CreationalContext.class, creationalContext);
        } else {
            synchronized (this) {
                if (creationalContextMap == null) {
                    creationalContextMap = new IdentityHashMap<T, CreationalContext<T>>();
                }
                creationalContextMap.put(instance, (CreationalContext<T>) creationalContext);
            }
        }
        return instance;
    }

    public Class<T> getInstanceClass() {
        return clazz;
    }

    public void releaseAll(RuntimeContext context) {
        synchronized (this) {
            if (creationalContextMap != null) {
                for (CreationalContext<T> c : creationalContextMap.values()) {
                    c.release();
                }
                creationalContextMap = null;
            }
        }
    }

    public void releaseInstance(T instance, RuntimeContext context) {
        if (context != null) {
            CreationalContext<T> creationalContext =
               (CreationalContext<T>) context.getAttributes()
                  .remove(CreationalContext.class.getName());
            if (creationalContext != null) {
                creationalContext.release();
            }
        }
    }

}
