package com.hp.spmaas;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

import org.apache.commons.beanutils.PropertyUtils;

public class EObjectELResolver extends ELResolver {

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		// TODO Auto-generated method stub
		return Object.class;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {

		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (base instanceof Pojo1 && property.toString().indexOf(".") != -1) {

			context.setPropertyResolved(true);

		}
		return null;
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (base instanceof Pojo1 && property.toString().indexOf(".") != -1)
			try {
				Object o = PropertyUtils.getProperty(base, property.toString());
				context.setPropertyResolved(true);
				return o;
			} catch (Exception e) {
				// TODO Auto-generated catch block

			}
		return null;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property,
			Object value) {
		if (base instanceof Pojo1 && property.toString().indexOf(".") != -1)
			try {
				PropertyUtils.setProperty(base, property.toString(), value);
				context.setPropertyResolved(true);

			} catch (Exception e) {
				// TODO Auto-generated catch block

			}

	}

}
