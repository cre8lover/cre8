<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE html>
<head>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/listimg/product_registration.css">
  <link href='https://netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css' rel='stylesheet' type='text/css'>
  <link href='//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.2.0/css/datepicker.min.css' rel='stylesheet' type='text/css'>
  <link href='//cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/1.8/css/bootstrap-switch.css' rel='stylesheet' type='text/css'>
  <link href='https://davidstutz.github.io/bootstrap-multiselect/css/bootstrap-multiselect.css' rel='stylesheet' type='text/css'>
  <script src='//cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js' type='text/javascript'></script>
  <script src='//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.0.0/js/bootstrap.min.js' type='text/javascript'></script>
  <script src='//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.2.0/js/bootstrap-datepicker.min.js' type='text/javascript'></script>
  <script src='//cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/1.8/js/bootstrap-switch.min.js' type='text/javascript'></script>
  <script src='https://davidstutz.github.io/bootstrap-multiselect/js/bootstrap-multiselect.js' type='text/javascript'></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    

</head>
<body>
<c:set value="${pro}" var="pro"/>
  <div class='container'>
    <div class='panel panel-primary dialog-panel'>
      <div class='panel-heading'>
        <h4>상품등록/수정(일반)</h4>
      </div>
      <div class='panel-body'>
        <form class='form-horizontal' enctype="multipart/form-data" method="post" name="proform" role='form' id="reg" action="<%= request.getContextPath() %>/cre/promodify">
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_pets'>판매상태</label>
            <div class='col-md-8'>
              <div class='make-switch' data-on-label='판매중' data-off-label='대기' id='id_pets_switch'>
                <input id='id_pets' type='checkbox' <c:if test="${pro.proStat eq 'PRO_ING'}"> checked </c:if>name="proStat" value="PRO_ING">
              </div>
            </div>
          </div>
<!--           <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_pets'>경매여부</label>
            <div class='col-md-8'>
              <div class='make-switch' data-on-label='경매' data-off-label='일반' id='id_pets_switch'>
                <input id='id_pets' type='checkbox' value='chk_hydro'>
              </div>
            </div>
          </div>  -->
          
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_accomodation'>카테고리</label>
            <div class='col-md-2'>
              <select class='form-control' id='id_accomodation' name="proHits">
                <option value="1" <c:if test="${pro.proHits eq 1 }"> selected </c:if>>의류</option>
                <option value="2" <c:if test="${pro.proHits eq 2 }"> selected </c:if>>가전/가구</option>
                <option value="3" <c:if test="${pro.proHits eq 3 }"> selected </c:if>>화장품</option>
                <option value="4" <c:if test="${pro.proHits eq 4 }"> selected </c:if>>인테리어</option>
                <option value="5" <c:if test="${pro.proHits eq 5 }"> selected </c:if>>여행</option>
                <option value="6" <c:if test="${pro.proHits eq 6 }"> selected </c:if>>기타</option>
              </select>
            </div>
          </div>
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_title'>상품명</label>
              <div class='col-md-6 indent-small'>
                <div class='form-group internal'>
                  <input class='form-control' id='id_first_name' name="itemName"placeholder='상품명' type='text' value="${pro.item.itemName }">
                            <div class='col-md-8'>
              <div class='col-md-2'>
                <div class='form-group internal'>

                </div>
              </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' >상품가격</label>
            <div class='col-md-8'>
              <div class='col-md-2'>
                <div class='form-group internal'>
                  <input class='form-control col-md-8' id='' placeholder='정가' type='number'  name="proPrice" value="${pro.proPrice}">
                </div>
              </div>
              <div class='col-md-6 indent-small'>
                <div class='form-group internal'>
                  <input class='form-control' id='id_children' placeholder='최종판매가격' type='number'name="proSaleprice" value="${pro.proSaleprice }">
                </div>
              </div>
            </div>
          </div>
          <div class='form-group'>
           
            <label class='control-label col-md-2 col-md-offset-2'>간략설명</label>
            <div class='col-md-6'>
              <div class='form-group'>
                <div class='col-md-11'>
                  <input class='form-control' placeholder='' type='text' name="itemDetail" value="${pro.item.itemDetail }">
                </div>
              </div>        
            </div>
          </div>
          <div class='form-group'>
           
            <label class='control-label col-md-2 col-md-offset-2'>수량</label>
            <div class='col-md-6'>
              <div class='form-group'>
                <div class='col-md-11'>
                  <input class='form-control' placeholder='' type='number' name="amount" value="${pro.proAmount }">
                </div>
              </div>        
            </div>
          </div>
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' >판매시작</label>
            <div class='col-md-8'>
              <div class='col-md-3'>
                <div class='form-group internal input-group'>
                  <input class='form-control datepicker' id='id_checkin' name="proOpendate" value="${pro.proOpendate }">
                  <span class='input-group-addon'>
                    <i class='glyphicon glyphicon-calendar'></i>
                  </span>
                </div>
              </div>
              <label class='control-label col-md-2' >판매종료</label>
              <div class='col-md-3'>
                <div class='form-group internal input-group'>
                  <input class='form-control datepicker' id='id_checkout' name="proClosedate"value="${pro.proClosedate }">
                  <span class='input-group-addon'>
                    <i class='glyphicon glyphicon-calendar'></i>
                  </span>
                </div>
              </div>
            </div>
          </div>
<!--           <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_equipment'>할인수단선택</label>
            <div class='col-md-8'>
              <div class='col-md-3'>
                <div class='form-group internal'>
                  <select class='form-control' id='id_equipment'>
                    <option>쿠폰할인</option>
                    <option>전체할인</option>
                    <option>카드할인</option>
                    <option>이벤트할인</option>
                  </select>
                </div>
              </div>
              <div class='col-md-9'>
                <div class='form-group internal'>
                  <label class='control-label col-md-3' for='id_slide'>포인트적립</label>
                  <div class='make-switch' data-off-label='NO' data-on-label='YES' id='id_slide_switch'>
                    <input id='id_slide' type='checkbox' value='chk_hydro'>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_service'>옵션관리</label>
            <div class='col-md-8'>
              <select class='multiselect' id='id_service' multiple='multiple'>
                <option value='hydro'>옵션1</option>
                <option value='water'>옵션2</option>
                <option value='sewer'>추가</option>
              </select>
            </div>
          </div>  -->

          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_comments'>사진첨부</label>
            <div class='col-md-6'>
              	<div class="filebox">
				    <input class="upload-name" value="첨부파일" placeholder="첨부파일">
				    <label for="file">파일찾기</label> 
				    <input type="file" id="file" name="filename">
				</div>
            </div>
            </div>
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_comments'>제품 상세</label>
            <div class='col-md-6'>
              <textarea class='form-control' id='id_comments' placeholder='Additional comments'name="proDetail" rows='3'>${pro.proDetail}</textarea>
            </div>
          </div>
          <input type="hidden" name="seqno" <c:if test="${pro.proSeqno != null }">value="${pro.proSeqno }"</c:if>>
          <input type="hidden" name="itemseqno" value="${pro.item.itemSeqno }">
          <div class='form-group'>
            <div class='col-md-offset-4 col-md-3'>
              <input class='btn-lg btn-primary' type='submit' id="proReg" value="상품등록">
            </div>
            <div class='col-md-3'>
              <button class='btn-lg btn-danger' style='float:right' type='submit' onclick="window.close()">취소</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</body>

<script>

    $("#proreg").click( function() {

    	var ans = confirm("상품이 등록되었습니다.");
            if (ans){
         $('#proReg').submit();
            }
      });


</script>
