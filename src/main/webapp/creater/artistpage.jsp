<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    

<!DOCTYPE html>
<html>
<head>
<%@ include file="/header.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/creater/artistpage.css">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%@ include file="/menu.jsp" %>
<body background="<%= request.getContextPath() %>/img/back.jpg">
      <div id="leftSideBar"> </div>
      <div class="body" >
      
      
      
<div class="tabs">
  <input type="radio" name="tabs"  id="tabone" checked="checked">
  <label for="tabone">일반상품</label>
  <div class="tab">
   <div class="mypage">
      <p class="mysale"><b>나의 판매내역</b></p><br><br><br><br><br>

         <div id="productView" class="product-grid">
            <c:set value ="${prolist}" var = "prolist" />
            <c:forEach items= "${prolist}" var="list">
                <div class="product span3">
         <a href="<%= request.getContextPath() %>/product/productDetail?seqno=${list.proSeqno}">
                    <div class="pro-img">
                        <img class="" ${list.item.itemImg } style="height:225px;">
                    </div>
                    <h3 class="product-name">
                        <p><b>${list.item.itemDetail }</b></p>
                    </h3>
                    <p class="product-info">
                        <p>총 판매량 : ${list.proAmount } </p>
                    </p>        
                    <p class="product-price">
                        <p>총 판매금액 : ${list.proSaleprice }
                    </p>
                    <div style="text-align: center;">
                      <button class="custom-btn btn-1" onclick = "newwindow('<%= request.getContextPath() %>/listimg/product_registration2.jsp','modify');">수정</button>
                  <button class="custom-btn btn-1" href = "#">삭제</button>
                  </div>
         </a>
                </div>
            </c:forEach>
         </div>
         
         
         <div class="info2">
            <img class="" src="<%= request.getContextPath() %>/img/user.png" style="height:150px;">
            <p class= name2><b>알파카&nbsp;</b>님</p>
            <div class="proreg">
            <a href ="javascript:newwindow('<%= request.getContextPath() %>/cre/product_registration','add');">등록</a>
            </div> 
            <p class="sum">총 정산금액</p>
            <p class="price2">${total }원</p>
            <div class="frame">
               <button class="custom-btn btn-2" onclick = "newwindow('<%= request.getContextPath() %>/cre/cremodify','modify');">정보수정</button>
               <button class="custom-btn btn-2" onclick = "location.href='<%= request.getContextPath() %>/cre/Adlist'">광고신청</button>
               <button class="custom-btn btn-2" onclick = "newwindow('<%= request.getContextPath() %>/cre/salesHistory','sale');">판매현황</button>
            </div>
         </div>
      
      
   </div>
  </div>
  
  <input type="radio" name="tabs" id="tabtwo">
  <label for="tabtwo">경매상품</label>
  <div class="tab">
<div class="mypage">
      <p class="mysale"><b>나의 경매내역</b></p><br><br><br><br><br>

         <div id="productView" class="product-grid">
            <c:set value ="${auclist}" var = "auclist" />
            <c:forEach items= "${auclist}" var="list">
                <div class="product span3">
                <a href="<%= request.getContextPath() %>/auc/auctionDetail?seqno=${list.aucSeqno}">
                    <div class="pro-img">
                        <img class="" ${list.aucImg } style="height:225px;">
                    </div>
                    <h3 class="product-name">
                        <p><b>${list.aucDetail }</b></p>
                    </h3>
                    <p class="product-info">
                        <p>시작 금액 : ${list.aucPrice } </p>
                    </p>        
                    <p class="product-price">
                        <p>현재 입찰가 : ${list.aucCloseprice }
                    </p>

                    
                    <div style="text-align: center;">
                       <c:if test="${list.aucStat eq 'WAIT'}">
                         <button class="custom-btn btn-1" onclick = "newwindow('<%= request.getContextPath() %>/listimg/product_registration2.jsp','modify');">수정</button>
                     <button class="custom-btn btn-1" href = "#">삭제</button>
                     </c:if>
                  </div>
                  </a>
                </div>
            </c:forEach>
   
         </div>
         
         
         <div class="info2">
            <img class="" src="<%= request.getContextPath() %>/img/user.png" style="height:150px;">
            <p class= name2><b>알파카&nbsp;</b>님</p>
            <div class="proreg">
            <a href ="javascript:newwindow('<%= request.getContextPath() %>/cre/auction_reg','add');">경매등록</a>
            </div> 
            <p class="sum">총 정산금액</p>
            <p class="price2">${total }원</p>
            <div class="frame">
               <button class="custom-btn btn-2" onclick = "newwindow('<%= request.getContextPath() %>/cre/cremodify','modify');">정보수정</button>
               <button class="custom-btn btn-2" onclick = "location.href='<%= request.getContextPath() %>/cre/Adlist'">광고신청</button>
               <button class="custom-btn btn-2" onclick = "newwindow('<%= request.getContextPath() %>/cre/salesHistory','sale');">판매현황</button>
            </div>
         </div>
      
      
   </div>
  </div>
</div>
</div>
   
<%--   <%@ include file="/footer.jsp" %>  --%>
</body>
<script src="<%= request.getContextPath() %>/js/artistpage.js"></script>


</html>