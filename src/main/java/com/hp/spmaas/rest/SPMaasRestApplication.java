package com.hp.spmaas.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class SPMaasRestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(DataModelService.class);
        classes.add(LayoutService.class);
        return classes;
    }
}
