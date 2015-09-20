<html>
<form name="fm" method="post" action="/weapp/upload.do"  enctype="multipart/form-data">
	<input type="file" />
	<input type="submit"/>
</form>

<form id="form1" name="form1" method="post" action="/weapp/upload.do" enctype="multipart/form-data">
 <table border="0" align="center">
  <tr>
   <td>上传人：</td>
   <td>
    <input name="name" type="text" id="name" size="20" ></td>
  </tr>   
  <tr>
   <td>上传文件：</td>
   <td><input name="file" type="file" size="20" ></td>
  </tr>    
  <tr>   
   <td></td><td>
    <input type="submit" name="submit" value="提交" >
    <input type="reset" name="reset" value="重置" >
   </td>
  </tr>
 </table>
</form>

</html>
