<%@ page contentType="text/html; charset=UTF-8" session="false"%><%@
 taglib uri="/struts-tags" prefix="s" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
<title>&nbsp;</title>
<link rel="stylesheet" type="text/css" href="style.css" />
</head>

<body>

<s:url var="canvas" action="canvas" forceAddSchemeHostAndPort="true" includeParams="none" />
<s:url var="endinvite" action="endinvite" forceAddSchemeHostAndPort="true" includeParams="none" />

<fb:serverfbml style="width:755px;">
<script type="text/fbml">
    <fb:fbml>  
        <fb:request-form
			type="Flowspace"
			content="&lt;fb:name uid=&quot;${param.fb_sig_user}&quot; firstnameonly=&quot;true&quot; linked=&quot;false&quot;/&gt; wants you to play Flowspace!&lt;fb:req-choice url='http://www.facebook.com/login.php?v=1.0&api_key=${param.fb_sig_api_key}&next=${canvas}&canvas=' label='Play Now' /&gt;" 
			invite="true" 
            action="${endinvite}"
			target="_self">

            <fb:multi-friend-selector max="20" 
                actiontext="Spiele Flowgame!" 
                showborder="true" 
                rows="5" cols="4"
				bypass="cancel"
                exclude_ids=""/><!-- TODO exclude fb.app_users -->
                
        </fb:request-form>
    </fb:fbml>
</script>
</fb:serverfbml>

<!-- Set background color and make it transparent, so that this works in IE -->
<div style="position: absolute; left:547px; top:23px; width:54px; height:24px; background-color:#ffff00; opacity:0.0; filter:Alpha(opacity=0)" onclick="parent.toggle_invite_content();">
</div>

<div style="position: absolute; left:547px; top:604px; width:54px; height:24px; background-color:#ffff00; opacity:0.0; filter:Alpha(opacity=0)" onclick="parent.toggle_invite_content();">
</div>

<div id="fb-root"></div>

<script type="text/javascript">
  window.fbAsyncInit = function() {
    FB.init({
      apiKey : '${param.fb_sig_api_key}',
      status : true, // check login status
      cookie : true, // enable cookies to allow the server to access the session
      xfbml  : true  // parse XFBML
    });
  };

  (function() {
    var e = document.createElement('script');
    e.type = 'text/javascript';
    e.src = 'http://static.ak.fbcdn.net/connect/en_US/core.js';
    e.async = true;
    document.getElementById('fb-root').appendChild(e);
  }());
</script>

</body>
</html>