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
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 */
package org.seamless.cdi.extension.wink;

import org.apache.wink.common.RuntimeContext;
import org.apache.wink.common.internal.lifecycle.ObjectCreationException;
import org.apache.wink.common.internal.lifecycle.ObjectFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CDISingletonObjectFactory<T> implements ObjectFactory<T> {

    final protected T instance;
    final protected CreationalContext<?> creationalContext;
    protected boolean isReleased = false;

    public CDISingletonObjectFactory(Class<T> clazz, BeanManager beanManager) {
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
        Bean theBean = beans.iterator().next();

        creationalContext = beanManager.createCreationalContext(theBean);
        try {
            instance = (T) beanManager.getReference(theBean, clazz, creationalContext);
        } catch (Exception e) {
            throw new ObjectCreationException(e);
        }
    }

    public T getInstance(RuntimeContext context) {
        return instance;
    }

    public Class<T> getInstanceClass() {
        return (Class<T>) instance.getClass();
    }

    public void releaseAll(RuntimeContext context) {
        if (isReleased)
            return;
        isReleased = true;
        if (creationalContext != null)
            creationalContext.release();
    }

    public void releaseInstance(T instance, RuntimeContext context) {
    }

}
