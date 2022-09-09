<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/auction/auction.css">
<meta charset="UTF-8">
<title>Insert title here</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
</head>
<body>
<%@ include file="/header.jsp" %>
   <%@ include file="/menu.jsp" %>
   <div class="mypage">
   <br>
   <div style="text-align: center;">
   <div class="tabs">
   <h2>Auction</h2>
   
     <input  id="all" type="radio" name="tab_item" checked>
     <label class="tab_item" for="all">최신순</label>
     
     <input  id="programming" type="radio" name="tab_item">
     <label class="tab_item" for="programming">인기순</label>
     
     <input  id="design" type="radio" name="tab_item">
     <label class="tab_item" for="design">마감임박</label>
     
     <div class="tab_content" id="all_content">
      <div class="main">
      <div class="shopping-cart">
         <ol class="ui-list shopping-cart--list" id="shopping-cart--list">
         <c:set value="${auclist}" var="auclist"/>
         <c:forEach items="${auclist}" var="list">
         <c:set value="${list.item}" var="item"/>
            <li class="_grid shopping-cart--list-item" onclick="location.href='<%= request.getContextPath() %>/auc/auctionDetail?seqno=${list.aucSeqno}'">
               <div class="_column product-image"> 
                  <div><img class="auc_img" src="/upload/thumbnail/${item.itemImg}"></div>
               </div>
               <div class="_column product-info">
                  <h4 class="product-name"><b>${item.itemName}</b> <span class="khm_startprice">시작가 ₩${list.aucPrice}~</span> </h4>
                  <p class="product-desc"> ${item.itemDetail }
                  <div style="padding-top:1rem">
                     <div> 날짜 : ${list.aucStart } ~ ${list.aucFinish } <br><span style="color:#B40404;">남은시간 ${list.aucAmount }일</span></div>
                     <div class="price product-single-price">₩ ${list.aucCloseprice }</div>
                  </div>
               </div>
            </li>
            </c:forEach>
          </ol>
     </div>
     </div>
        </div>
        
        
        <div class="tab_content" id="programming_content">
      <div class="main">
      <div class="shopping-cart">
         <ol class="ui-list shopping-cart--list" id="shopping-cart--list">
         <c:set value="${hitlist}" var="auclist"/>
         <c:forEach items="${auclist}" var="list">
         <c:set value="${list.item}" var="item"/>
            <li class="_grid shopping-cart--list-item" onclick="location.href='<%= request.getContextPath() %>/auc/auctionDetail?seqno=${list.aucSeqno}'">
               <div class="_column product-image">
                  <%-- <img class="product-image--img" src="<%= request.getContextPath() %>/img/dd.jpg" alt="Item image" /> --%>
                  <div><img class="" src="/upload/thumbnail/${item.itemImg}"></div>
               </div>
               <div class="_column product-info">
                  <h4 class="product-name"><b>${item.itemName}</b> <span class="khm_startprice">시작가 ₩${list.aucPrice}~</span> </h4>
                  <p class="product-desc"> ${item.itemDetail }
                  <div style="padding-top:1rem">
                     <div> 날짜 : ${list.aucStart } ~ ${list.aucFinish } <br><span style="color:#B40404;">남은시간 ${list.aucAmount }일</span></div>
                     <div class="price product-single-price">₩ ${list.aucCloseprice }</div>
                  </div>
               </div>
            </li>
            </c:forEach>
          </ol>
     </div>
     </div>
     </div>
        
        <div class="tab_content" id="design_content">
                  <div class="main">
      <div class="shopping-cart">
         <ol class="ui-list shopping-cart--list" id="shopping-cart--list">
         <c:set value="${auclist}" var="auclist"/>
         <c:forEach items="${auclist}" var="list">
         <c:set value="${list.item}" var="item"/>
            <li class="_grid shopping-cart--list-item" onclick="location.href='<%= request.getContextPath() %>/auc/auctionDetail?seqno=${list.aucSeqno}'">
               <div class="_column product-image">
                  <%-- <img class="product-image--img" src="<%= request.getContextPath() %>/img/dd.jpg" alt="Item image" /> --%>
                  <div><img class="" src="/upload/thumbnail/${item.itemImg}"></div>
               </div>
               <div class="_column product-info">
                  <h4 class="product-name"><b>${item.itemName}</b> <span class="khm_startprice">시작가 ₩${list.aucPrice}~</span> </h4>
                  <p class="product-desc"> ${item.itemDetail }
                  <div style="padding-top:1rem">
                     <div> 날짜 : ${list.aucStart } ~ ${list.aucFinish } <br><span style="color:#B40404;">남은시간 ${list.aucAmount }일</span></div>
                     <div class="price product-single-price">₩ ${list.aucCloseprice }</div>
                  </div>
               </div>
            </li>
            </c:forEach>
          </ol>
     </div>
     </div>
   </div>
   
        
        
        
        
   </div>

</div>
<%@ include file="/footer.jsp" %>

</body>
</html>