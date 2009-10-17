<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<s:url var="canvas" action="canvas" forceAddSchemeHostAndPort="true" includeParams="none" />
<s:url var="applet" action="applet" forceAddSchemeHostAndPort="true" includeParams="none" />
<s:text var="appletWidth" name="applet.width" />
<s:text var="appletHeight" name="applet.height" />
<fb:if-is-app-user>
	<fb:iframe src="${applet}" smartsize="false" resizable="false" width="${appletWidth}" height="${appletHeight}" frameborder="0" scrolling="no" />
	<fb:else>
		<fb:redirect url="http://www.facebook.com/login.php?v=1.0&api_key=${param.fb_sig_api_key}&next=${canvas}&canvas=" />
	</fb:else>
</fb:if-is-app-user>
