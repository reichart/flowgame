<%@ page contentType="text/html; charset=UTF-8" session="false"%><%@
 taglib uri="/struts-tags" prefix="s" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "html-dtd/xhtml1-transitional.dtd">
<html>
<head>
<title>&nbsp;</title>
<link rel="stylesheet" type="text/css" href="style.css" />
<script type="text/javascript" src="script.js"></script>
</head>
<body onload="extend_cookie_dates()">

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
<script src="http://www.java.com/js/deployJava.js"></script>
<script>
	var attributes = {code:'de.tum.in.flowgame.client.GameApplet', width:${appletWidth}, height:${appletHeight}} ;
	var parameters = {jnlp_href: 'flowgame.jnlp',
					  userId: '${param.fb_sig_user}',
					  apiKey: '${param.fb_sig_api_key}',
					  sessionKey: '${param.fb_sig_session_key}',
					  sessionSecret: '${param.fb_sig_ss}'};
	var version = '1.5'; // for MacOSX
	deployJava.runApplet(attributes, parameters, version);
</script>
<noscript>
	<div class="fberrorbox" id="nojava">
	<p>To play this game, you need Java&trade;<br />
	but it seems to be not yet installed on your computer.</p>

	<p>You can get it from this shiny button:</p>

	<p><a href="http://java.com/java/download/index.jsp?cid=jdp146161" target="_blank"><img
		width="170" height="100" alt="Get Java Software" style="border: none"
		src="http://java.com/en/img/everywhere/getjava_lg.gif?cid=jdp146161" /></a></p>
	
	<p>(Link will open in a new window/tab)</p>

	</div>
</noscript>

<div id="content" style="display: none; position: absolute; top:0; left:0">
	<s:url var="invite" action="invite" includeParams="get" />
	<iframe src="${invite}" width="755" height="670" scrolling="no">Sorry, your browser doesn't support frames.</iframe>
</div>

<table id="links" style="width:${appletWidth}px">
	<tr>
		<td class="fbbluebox" id="problems"><a target="_top" href="https://www.facebook.com/flowspace?v=app_2373072738">Doesn't work?<br />Tell us!</a></td>
		<td class="fbbluebox" id="comments"><a target="_top" href="https://www.facebook.com/flowspace">Leave a comment!</a></td>
	</tr>
</table>

<div id="known" style="width:${appletWidth}px">

<div class="fbbluebox" style="font-weight: normal; margin-top: 1em">
<p><strong>Known issues</strong> that prevent this game from functioning:</p>
<ul>
	<li>Mac OS X comes with an <span title="as in 7 years outdated, from when Mac OS X 10.3 (Panther) came out in 2003">outdated</span>
		version of Java3D that can't easily be updated. No can do, sorry :(</li>

	<li>OpenJDK (e.g. on Ubuntu Linux) can't access Facebook (we're on it!).</li>
</ul>
</div>

</div>

</body>
</html>
