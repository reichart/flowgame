<%@ page contentType="text/html; charset=UTF-8" session="false" %>
<applet code="org.jdesktop.applet.util.JNLPAppletLauncher" width="600" height="400"
	archive="lib/flowgame.jar, lib/facebook-java-api-2.1.1-jsononly.jar, lib/json-20070829.jar, lib/commons-logging-1.1.1.jar,lib/svgSalamander-tiny.jar, http://download.java.net/media/applet-launcher/applet-launcher.jar,http://download.java.net/media/java3d/webstart/release/j3d/latest/j3dcore.jar,http://download.java.net/media/java3d/webstart/release/j3d/latest/j3dutils.jar,http://download.java.net/media/java3d/webstart/release/vecmath/latest/vecmath.jar,http://download.java.net/media/jogl/builds/archive/jsr-231-webstart-current/jogl.jar,http://download.java.net/media/gluegen/webstart/gluegen-rt.jar,http://download.java.net/media/joal/webstart/joal.jar">
	<param name="codebase_lookup" value="false" />
	<param name="subapplet.classname" value="de.tum.in.flowgame.GameApplet" />
	<param name="subapplet.displayname" value="Flowgame Applet" />

	<param name="jnlpNumExtensions" value="2" />
	<param name="jnlpExtension1" value="http://download.java.net/media/java3d/webstart/release/java3d-latest.jnlp" />
	<param name="jnlpExtension2" value="http://download.java.net/media/joal/webstart/joal.jnlp" />

	<param name="progressbar" value="true" />
	<param name="noddraw.check" value="true" />
	<param name="noddraw.check.silent" value="true" />

	<param name="server" value="http://vxart.de:8080/flowgame/" />
	<param name="apiKey" value="${param.fb_sig_api_key}" />
	<param name="sessionKey" value="${param.fb_sig_session_key}" />
	<param name="sessionSecret" value="${param.fb_sig_ss}" />

<div id="nojava">
	<p>To play this game, you need Java&trade;<br />
	but it seems to be not yet installed on your computer.</p>

	<p>You can get it from this shiny button:</p>

	<p><a href="http://java.com/java/download/index.jsp?cid=jdp146161"><img
		width="170" height="100" alt="Get Java Software" style="border: none"
		src="http://java.com/en/img/everywhere/getjava_lg.gif?cid=jdp146161" /></a></p>
</div>

</applet>
