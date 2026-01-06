<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
   <meta name="Author" content="Tom Herbert">
   <meta name="GENERATOR" content="Mozilla/4.61 [en] (Win95; I) [Netscape]">
   <title>MCInet</title>
</head>
<body text="#000000" bgcolor="#DFdfa0" link="#0000EE" vlink="#551A8B" alink="#FF0000" marginwidth=0 marginheight=0 topmargin=0 leftmargin=0>
<center>
<pre>
<img src="mad_cat_r1_c1.gif"><BR><img src="mad_cat_r2_c1.gif"><img src="mad_cat_r2_c2.gif" name="centerpic" border=0><img src="mad_cat_r2_c3.gif" border=0><BR><img src="mad_cat_r3_c1.gif" border=0>
</pre>
</center>
<center>
<table valign=top>
<tr>
<td width=350 bgcolor=white>
<font color=red><h2>The Mad Cats</h2></font>
<font size=2>
Sed and Awk are the Mad Cats.  They are named for a couple of Unix programs-- Awk is depicted in the Mad Cat logo.  (I think are next pet will be Perl :-) ).
<br>
<br>
This site has no particular purpose except as an avenue of our own self expression and freedom of speech.  This site will be updated from time to time.
</font>
</td>
<td width=50>
</td>
<td width=350 bgcolor=white valign=top>
<font color=red><h2>Original Applets!</h2></font>
<font size=4>
<a href="scribble.html">Scribble</a>
</font>
Doodling all over your screen
<br>
<br>
<font size=4>
<a href="set.html">Set</a>
</font>
Based on the card game "Set"
</td>
</tr>
</table>
<BR>
<HR><CENTER>
Comments or suggestions for this site may be sent to <A HREF="mailto:feedback@mcinet.com">feedback@mcinet.com</A>
</CENTER>
<br>
<center>
<a href="http://www.herbertland.com"><img src="icons/menu/return_sign.gif" border=0></a>
</center>

</body>
<script LANGUAGE="JavaScript1.2">

min_pic = 1;
max_pic = 3;
current = 0;
sinterval = 250;

function newImage(arg) {
	if (document.images) {
		rslt = new Image();
		rslt.src = arg;
		return rslt;
	}
}

var preloadFlag = false;
function preloadImages() {
	if (document.images) {
		eye1_image = newImage("http://www.mcinet.com/eye1.gif");
		eye2_image = newImage("http://www.mcinet.com/eye2.gif");
		eye3_image = newImage("http://www.mcinet.com/eye3.gif");
		preloadFlag = true;
	}
}

function change() {
	if (document.images && (preloadFlag == true)) {
		current += 1;
		if (current > max_pic) {
			current = min_pic;
		}
		document["centerpic"].src = "eye" + current + ".gif";
	}
}

function doinit() {
	preloadImages();
	setInterval("change()", sinterval);
}

window.onload = doinit;
</script>

</html>
