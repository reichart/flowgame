<%@ page contentType="text/html; charset=UTF-8" session="false"%><%@
 taglib uri="/struts-tags" prefix="s" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "html-dtd/xhtml1-transitional.dtd">
<html>
<head>
<title>&nbsp;</title>
<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body bgcolor="pink">

<script type="text/javascript">
function toggle_invite_content() {
	  var content = document.getElementById('content');
	  if (content.style.display == 'none') {
	    content.style.display = '';
	  } else {
	    content.style.display = 'none';
	  }
}
</script>

<s:text var="appletWidth" name="applet.width" />
<s:text var="appletHeight" name="applet.height" />
<applet code="org.jdesktop.applet.util.JNLPAppletLauncher" width="${appletWidth}" height="${appletHeight}"
	archive="lib/flowgame-client.jar, lib/facebook-java-api-2.1.1-jsononly.jar, lib/json-20070829.jar, lib/commons-io-1.3.2.jar, lib/commons-httpclient-3.1.jar, lib/commons-codec-1.3.jar, lib/commons-logging-1.1.1.jar, lib/svgSalamander-tiny.jar, http://download.java.net/media/applet-launcher/applet-launcher.jar,http://download.java.net/media/java3d/webstart/release/j3d/latest/j3dcore.jar,http://download.java.net/media/java3d/webstart/release/j3d/latest/j3dutils.jar,http://download.java.net/media/java3d/webstart/release/vecmath/latest/vecmath.jar,http://download.java.net/media/jogl/builds/archive/jsr-231-webstart-current/jogl.jar,http://download.java.net/media/gluegen/webstart/gluegen-rt.jar,http://download.java.net/media/joal/webstart/joal.jar">
	<param name="codebase_lookup" value="false" />
	<param name="subapplet.classname" value="de.tum.in.flowgame.GameApplet" />
	<param name="subapplet.displayname" value="Flowgame Applet" />

	<param name="mayscript" value="true" />

	<param name="jnlpNumExtensions" value="2" />
	<param name="jnlpExtension1" value="http://download.java.net/media/java3d/webstart/release/java3d-latest.jnlp" />
	<param name="jnlpExtension2" value="http://download.java.net/media/joal/webstart/joal.jnlp" />

	<param name="progressbar" value="true" />
	<param name="noddraw.check" value="true" />
	<param name="noddraw.check.silent" value="true" />

	<param name="userId" value="${param.fb_sig_user}" />
	<param name="apiKey" value="${param.fb_sig_api_key}" />
	<param name="sessionKey" value="${param.fb_sig_session_key}" />
	<param name="sessionSecret" value="${param.fb_sig_ss}" />
	
	<div class="fberrorbox" id="nojava">
	<p>To play this game, you need Java&trade;<br />
	but it seems to be not yet installed on your computer.</p>

	<p>You can get it from this shiny button:</p>

	<p><a href="http://java.com/java/download/index.jsp?cid=jdp146161" target="_blank"><img
		width="170" height="100" alt="Get Java Software" style="border: none"
		src="http://java.com/en/img/everywhere/getjava_lg.gif?cid=jdp146161" /></a></p>
	
	<p>(Link will open in a new window/tab)</p>

	</div>

</applet>

<div id="content" style="display: none; position: absolute; top:0; left:0">
	<s:url var="invite" action="invite" includeParams="get" />
	<iframe src="${invite}" width="755" height="670" scrolling="no">Sorry, your browser doesn't support frames.</iframe>
</div>

</body>
</html>
