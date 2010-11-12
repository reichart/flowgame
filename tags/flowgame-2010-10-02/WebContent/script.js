function set_cookie2 ( name, value, expiredays, path, domain, secure )
{
  var cookie_string = name + "=" + escape ( value );
  if ( expiredays )
  {
	var expires=new Date();
	expires.setDate(expires.getDate() + expiredays);
    cookie_string += "; expires=" + expires.toGMTString();
  }
  if ( path )
        cookie_string += "; path=" + escape ( path );
  if ( domain )
        cookie_string += "; domain=" + escape ( domain );
  if ( secure )
        cookie_string += "; secure";
  document.cookie = cookie_string;
}

function set_cookie ( name, value )
{
	set_cookie2(name, value, 365);
}

function extend_cookie_dates ()
{
	var cookie_string = document.cookie;
	var temp = new Array();
	temp = cookie_string.split(';');

	for ( var i = 0; i < temp.length; i++) {
		set_cookie (temp[0].split('=')[0], temp[0].split('=')[1]);
	}	
}

function get_cookie ( cookie_name )
{
  var results = document.cookie.match ( '(^|;) ?' + cookie_name + '=([^;]*)(;|$)' );

  if ( results )
    return ( unescape ( results[2] ) );
  else
    return null;
}