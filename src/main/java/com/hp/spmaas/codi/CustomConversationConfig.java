package com.hp.spmaas.codi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;

import org.apache.myfaces.extensions.cdi.core.api.config.ConfigEntry;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.config.ConversationConfig;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.config.WindowContextConfig;

import com.hp.spmaas.cdi.tenant.TenantScoped;

@Specializes
@ApplicationScoped
public class CustomConversationConfig extends WindowContextConfig
{

	@Override
	@ConfigEntry
	public boolean isAddWindowIdToActionUrlsEnabled() {
		
		return false;
	}

	@Override
	@ConfigEntry
	public boolean isUrlParameterSupported() {
		
		return false;
	}
	
	
   
}