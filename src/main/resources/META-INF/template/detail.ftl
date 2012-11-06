<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"   
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:spmaas="http://java.sun.com/jsf/composite/spmaas"
      >
    <body>
        <ui:composition>
        	<#list widgets as w>
  				${w}
			</#list>  
	    	
		</ui:composition>
    </body>
</html>