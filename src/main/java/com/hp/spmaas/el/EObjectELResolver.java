package com.hp.spmaas.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.ocpsoft.shade.org.apache.commons.beanutils.BeanUtils;

public class EObjectELResolver extends ELResolver {

	static class NestPropertyResolver {

		private Object result;
		private String resultProperty;

		public NestPropertyResolver(EObject base, String property) {

			EObject eObj = (EObject) base;
			String wholeProperty = property.toString();

			int dotIndx;
			Object result = eObj;

			while ((dotIndx = wholeProperty.indexOf(".")) != -1) {
				String shortProperty = wholeProperty.substring(0, dotIndx);
				wholeProperty = wholeProperty.substring(dotIndx + 1);
				EStructuralFeature propertyName =  eObj.eClass()
						.getEStructuralFeature(shortProperty);
				result = eObj.eGet(propertyName);
				if (result instanceof EObject) {
					eObj = (EObject) result;
				} else {
					break;
				}
			}

			this.result = result;
			this.resultProperty = wholeProperty;
		}

		public Object getResult() {
			return result;
		}

		public String getResultProperty() {
			return resultProperty;
		}

	}

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
		if (base instanceof EObject) {

			context.setPropertyResolved(true);

		}
		return null;
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (base instanceof EObject)
			try {

				NestPropertyResolver resolver = new NestPropertyResolver(
						(EObject) base, property.toString());
				Object result = resolver.result;

				if (result instanceof EObject) {
					EObject eObj = (EObject) result;
					EAttribute propertyName = (EAttribute) eObj
							.eClass()
							.getEStructuralFeature(resolver.getResultProperty());
					result = eObj.eGet(propertyName);
				} else {
					result = BeanUtils.getProperty(result,
							resolver.getResultProperty());
				}
				context.setPropertyResolved(true);
				return result;
			} catch (Exception e) {
				e.printStackTrace();

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
		if (base instanceof EObject)
			try {
				NestPropertyResolver resolver = new NestPropertyResolver(
						(EObject) base, property.toString());
				Object result = resolver.result;

				if (result instanceof EObject) {
					EObject eObj = (EObject) result;
					EAttribute propertyName = (EAttribute) eObj
							.eClass()
							.getEStructuralFeature(resolver.getResultProperty());
					eObj.eSet(propertyName, value);
				} else {
					BeanUtils.setProperty(result, resolver.getResultProperty(),
							value);
				}
				context.setPropertyResolved(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block

			}

	}

}
