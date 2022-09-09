<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/member/meminfo.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/cre8.css">
<title>정보 수정</title>
</head>
<body>

	
<div class="jmh_body">

	<div class="body" style="height:100%">
 		<div class="leftSideBar"></div>
		<div class="content" style="width:100%;"> 
		<form action="<%= request.getContextPath() %>/mem/infoinsert" enctype="multipart/form-data" id = "insert" method="post">
		<div class="khm_box">
			<h2>기본정보</h2>
			<hr>
			
			<c:set value="${info}" var="info" />
			
			<table>
			<tbody>
			    <tr>
			      <th>이름</th>
			      <td colspan='2'>
			      	  <input type="text" name="name" value="${info.memName }" readonly style="background:lightgray; border: 0.1px solid;">
			      </td>
			    </tr>
			    <tr>
			    </tr>
			   
			    <tr>
			      <th rowspan='2'>연락처(택1 필수)</th>
			      <td>휴대전화</td>
			      <td>
	                <input type="text" name="phone" value="${info.memTel}" maxlength="16" placeholder="010-1234-5678">
	 			  </td>
			    </tr>
			    <tr>
			      <td>이메일</td>
			      <td>
					<input style="width:100%" type="text" value="${info.memEmail }" placeholder="ex)cre8@naver.com"  name="email">
				  </td>
			    </tr>
			  </tbody>
			</table>
		</div>
		
		<div class="khm_box">
			<h2>배송지관리</h2>
			<hr>
			
			<table>
			 <tbody>
			    <tr>
			      <th>
			      	배송지명
			      </th>
			      <td>
					<input type="text" name="cate"  placeholder="배송지명을 입력하세요" value="${info.addressSet.addCategory }">
				  </td>
			    </tr>
			    <tr>
			      <th>연락처</th>
			      <td>
				    <input type="text" name="mobile" id="mobile" maxlength="16" value="${info.addressSet.addPhone }" placeholder="010-1234-5678">
				  </td>
			    </tr>
			    <tr>
			      <th>이름</th>
			      <td>
					<input type="text" placeholder="이름을 입력하세요" value="${info.addressSet.addPerson }" name="person">
				  </td>
			    </tr>
			    <tr>
			      <th rowspan='2'>주소</th>
			      <td>
					<input type="text" id="address_kakao" value="${info.addressSet.addAddress }" name="address" readonly placeholder="주소를 입력하세요" />
				  </td>
			    </tr>
			    <tr>
			      <td>
			      	<input type="text" name="address_detail" value="${info.addressSet.addetail }" placeholder="상세주소를 입력하세요"/>
			      </td>
			    </tr>

			  </tbody>
			</table>
		</div>
		
		<div class="khm_box">
			<h2>추가 정보</h2>
			<hr>
			
			<table>
			<tbody>
			    <tr>
			      <th>
			      	SNS정보
			      </th>
			      <td>
					<input type="button" name="sns" value="sns" id='facebook'>
				  </td>
			    </tr>
			   
			    <tr>
			      <th>프로필변경</th>
			      <td>
					<div class="filebox">
					    <input class="upload-name" value="첨부파일" placeholder="첨부파일">
					    <label for="file"></label> 
					    <input type="file" name="filename" id="file">
					</div>
				  </td>
			    </tr>
			  </tbody>
			</table>
			<input type="submit" id="khm_memreg" value="정보 수정">
		</div>
		
		</form>
		</div>
		<div class="rightSideBar"></div> 
	</div>	
</div>			
	<div class="jmh_project">
</div>
		
 </body>
</html>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="<%= request.getContextPath() %>/js/meminfo.js"></script>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script>

    $("#khm_memreg").click( function() {

    	var ans = confirm("정보가 수정되었습니다.");
            if (ans){
         $('#insert').submit();
            }
      });


</script>