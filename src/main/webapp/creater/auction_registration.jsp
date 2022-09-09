<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE html>
<head>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/creater/auction_registration.css">
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
<c:set value="${auc}" var="pro"/>
  <div class='container'>
    <div class='panel panel-primary dialog-panel'>
      <div class='panel-heading'>
        <h4>상품등록(경매)</h4>
      </div>
      <div class='panel-body'>
        <form class='form-horizontal' enctype="multipart/form-data" method="post" id="reg" role='form' action="<%= request.getContextPath() %>/cre/auction_modify">
        <!--   <div class='form-group'>
           <label class='control-label col-md-2 col-md-offset-2' for='id_pets'>노출상태</label>
            <div class='col-md-8'>
              <div class='make-switch' data-on-label='노출' data-off-label='미노출' id='id_pets_switch'>
                <input id='id_pets' type='checkbox' value='chk_hydro'>
              </div>
            </div> 
          </div> -->
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_pets'>경매상태</label>
            <div class='col-md-8'>
              <div class='make-switch' data-on-label='경매중' data-off-label='대기중' id='id_pets_switch'>
                <input id='id_pets' type='checkbox' name="auc_stat"<c:if test="${pro.aucStat eq 'AUC_ING'}"> checked </c:if>  value='AUC_ING'>
              </div>
            </div>
          </div>
          
          <!-- <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_accomodation'>카테고리</label>
            <div class='col-md-2'>
              <select class='form-control' id='id_accomodation'>
                <option>의류</option>
                <option>가전/가구</option>
                <option>화장품</option>
                <option>인테리어</option>
                <option>여행</option>
                <option>기타</option>
              </select>
            </div>
          </div> -->
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_title' >상품명</label>
            <div class='col-md-8'>
              <div class='col-md-2'>
                <div class='form-group internal'>

                </div>
              </div>
              <div class='col-md-6 indent-small'>
                <div class='form-group internal'>
                  <input class='form-control' id='id_first_name' name="item_name" placeholder='상품명' type='text' value="${pro.item.itemName }">
                </div>
              </div>
            </div>
          </div>
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' >상품가격</label>
            <div class='col-md-8'>
              <div class='col-md-2'>
                <div class='form-group internal'>
                  <input class='form-control col-md-8' id='' placeholder='시작가' type='number' name="auc_price" value="${pro.aucPrice}">
                </div>
              </div>
              <div class='col-md-6 indent-small'>
                <div class='form-group internal'>
                </div>
              </div>
            </div>
          </div>
          <div class='form-group'>
            
            <label class='control-label col-md-2 col-md-offset-2'>간략설명</label>
            <div class='col-md-6'>
              <div class='form-group'>
                <div class='col-md-11'>
                  <input class='form-control' placeholder='' type='text' name="auc_shortdetail" value="${pro.aucShortdetail}">
                </div>
              </div>        
            </div>
          </div>
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' >경매시작일</label>
            <div class='col-md-8'>
              <div class='col-md-3'>
                <div class='form-group internal input-group'>
                  <input class='form-control datepicker' id='id_checkin' name="auc_start" value="${pro.aucStart }">
                  <span class='input-group-addon'>
                    <i class='glyphicon glyphicon-calendar'></i>
                  </span>
                </div>
              </div>
              <label class='control-label col-md-2' >경매종료일</label>
              <div class='col-md-3'>
                <div class='form-group internal input-group'>
                  <input class='form-control datepicker' id='id_checkout' name="auc_finish" value="${pro.aucFinish }">
                  <span class='input-group-addon'>
                    <i class='glyphicon glyphicon-calendar'></i>
                  </span>
                </div>
              </div>
            </div>
          </div>
         
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_comments'>사진첨부</label>
            <div class='col-md-6'>
              	<div class="filebox">
				    <input class="upload-name" value="첨부파일" placeholder="첨부파일">
				    <label for="file">파일찾기</label> 
				    <input type="file" id="file" name="filename">
				</div>
            </div>
          <div class='form-group'>
            <label class='control-label col-md-2 col-md-offset-2' for='id_comments' >제품 상세</label>
            <div class='col-md-6'>
              <textarea class='form-control' id='id_comments' placeholder='Additional comments' rows='3' name="auc_detail">${pro.aucDetail}</textarea>
            </div>
          </div>
          <input type="hidden" name="seqno" <c:if test="${pro.aucSeqno != null }">value="${pro.aucSeqno }"</c:if>>
          <input type="hidden" name="itemseqno" value="${pro.item.itemSeqno }">
          <div class='form-group'>
            <div class='col-md-offset-4 col-md-3'>
              <input class='btn-lg btn-primary' id="insert" value="상품등록" type='submit'>
            </div>
            <div class='col-md-3'>
              <button class='btn-lg btn-danger' style='float:right' onclick="window.close()">취소</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</body>
<script>

    $("#insert").click( function() {

    	var ans = confirm("상품이 등록되었습니다.");
            if (ans){
         $('#insert').submit();
            }
      });


</script>
