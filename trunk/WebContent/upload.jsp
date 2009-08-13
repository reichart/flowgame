<%@taglib prefix="s" uri="/struts-tags"%>

<s:form action="upload" method="post" enctype="multipart/form-data"
	cssClass="centered" theme="simple" id="upload">
	<div class="errors"><s:actionerror /></div>
	<s:file name="file" size="50" />
	<s:submit value="%{getText('label.upload')}" />
</s:form>