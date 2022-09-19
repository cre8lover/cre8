                -----5��------ 
                
--creatoradd [ũ�������� ����]
create or replace PROCEDURE p_creatoradd(
    p_cre_company in creator.cre_company%type,
    p_cre_phone   in creator.cre_phone%type,
    p_cre_name    in creator.cre_name%type,
    p_cre_address in creator.cre_address%type,
    p_cre_regnum  in creator.cre_regnum%type,
    p_cre_salenum in creator.cre_salenum%type,
    p_cre_pot     in creator.cre_pot%type,
    p_mem_id      in creator.mem_id%type,
    p_rs          out integer
)
is
begin
   insert into creator (cre_seqno, cre_company, cre_phone, cre_name, cre_address, cre_regnum, cre_salenum, cre_pot, mem_id)
    values (cre_seqno.nextval,p_cre_company, p_cre_phone,p_cre_name,p_cre_address,p_cre_regnum,p_cre_salenum,p_cre_pot,p_mem_id);
    p_rs := sql%rowcount;
end;
/
--�׽�Ʈ
declare 
    v_rs    integer;    
begin
p_creatoradd('�������۴�', '01055556666', '��ʶ�', '�����ÿ��뱸', '163-170-1233', '123-123-1234', '���� ������Դϴ�', 'ddd', v_rs );
 DBMS_OUTPUT.PUT_LINE('��������� :' || v_rs);
end;
commit;


--ũ������Ʈ ����
create or replace PROCEDURE p_cremodify(
    p_memid         in mem.mem_id%TYPE,   
    p_memtel        in mem.mem_tel%TYPE,
    p_mememail      in mem.mem_email%TYPE,
    p_memsnsinfo    in mem.mem_snsinfo%TYPE,
    p_crecompany    in creator.cre_company%TYPE,
    p_crephone      in creator.cre_phone%TYPE,
    p_crename       in creator.cre_name%TYPE,
    p_creaddress    in creator.cre_address%TYPE,
    p_creregnum     in creator.cre_regnum%TYPE,
    p_cresalenum    in creator.cre_salenum%TYPE,
    p_crepot        in creator.cre_pot%TYPE
)
is    
begin

    update mem set mem_tel = p_memtel, mem_email = p_mememail, MEM_SNSINFO = p_memsnsinfo where mem_id = p_memid;

    update creator set CRE_COMPANY =p_crecompany, CRE_PHONE = p_crephone, CRE_NAME = p_crename, CRE_ADDRESS = p_creaddress,
                           CRE_REGNUM = p_creregnum, CRE_SALENUM = p_cresalenum, CRE_POT = p_crepot where mem_id = p_memid;   

end;
/
--�׽�Ʈ
exec p_cremodify('ddd','01066666666','cmcm@naver,com','df@instargram','�������۴�','01099999999','��ʶ�','�����ÿ����','11-23232-423','123-124-4233','thankyou');
--mem���̺��� Ȯ�� ok



--mk [������Ʈ �����ִ°�]
create or replace PROCEDURE p_mk_list(
    p_cur           out sys_refcursor
)
is
begin
    open p_cur for
        select mar_seqno, mar_product, mar_img, mar_price from Marketing;
end;
/
--�׽�Ʈ
declare
    type p_list  is record(
        mar_seqno       marketing.mar_seqno%type,  
        mar_product     marketing.mar_product%type,
        mar_img         marketing.mar_img%type,
        mar_price       marketing.mar_price%type
    );
    v_cur sys_refcursor;
    p_m_list    p_list;
begin
    p_mk_list(v_cur);
    LOOP
        fetch v_cur into p_m_list;
        exit when v_cur%notfound;
        dbms_output.put_line(p_m_list.mar_seqno);
        dbms_output.put_line(p_m_list.mar_product);
    END LOOP;
end;
/





--mmk[�����ø���Ʈ�� �ϳ� Ŭ�������� ������ ����]
create or replace procedure p_mmk_list(
    p_mar_seqno  marketing.mar_seqno%type,   
    p_cur    out sys_refcursor
)
is
begin
    open p_cur for
    select mar_img,mar_price,mar_company,mar_detail from Marketing where mar_seqno=p_mar_seqno;
end;
/
--�׽�Ʈ
declare
     type p_list  is record(
        mar_img         marketing.mar_img%type,
        mar_price       marketing.mar_price%type,
        mar_company     marketing.mar_company%type,  
        mar_detail     marketing.mar_detail%type
    );
    v_cur sys_refcursor;
    p_m_list    p_list;
begin
    p_mk_list(v_cur);
    LOOP
        fetch v_cur into p_m_list;
        exit when v_cur%notfound;
        dbms_output.put_line(p_m_list.mar_company);
        dbms_output.put_line(p_m_list.mar_detail);
    END LOOP;
end;






--ũ�����Ͱ� �Ǹ��� �Ϲݹ�ǰ ���� 
create or replace NONEDITIONABLE PROCEDURE p_prolist(
    p_item_mem_id    in item.mem_id%type,
     p_cur    out sys_refcursor
    
)
IS         
BEGIN
    open p_cur for
                select (select item_name from item i where i.item_seqno = p.item_seqno) item_naem,
                (select item_detail from item i where i.item_seqno = p.item_seqno) item_detail,
                (select (select thumb_filename from att_thumb at where at.att_seqno = a.att_seqno) from att a where a.item_seqno = p.item_seqno) thumb_filename,
                p.pro_amount,p.pro_saleprice, p.pro_seqno
                from pro p
                where mem_id = p_item_mem_id
                and pro_stat not in 'PRO_END';                 
END;




-- productmodify ��ǰ ����
create or replace procedure p_productmodify(
    p_itemName          in item.item_name%type,
    p_itemDetail        in item.item_detail%type,
    p_itemseqno         in item.item_seqno%type,
   
    p_proStat           in pro.pro_stat%type,
    p_proPrice          in pro.pro_price%type,
    p_proHits           in pro.pro_hits%type,   
    p_proSaleprice      in pro.pro_saleprice%type,
    p_proOpendate       in pro.pro_opendate%type,
    p_proClosedate      in pro.pro_closedate%type,
    p_proDetail         in pro.pro_detail%type,
    p_proAmount         in pro.pro_amount%type,
    p_seqno             in pro.pro_seqno%type
)
is
begin
    update item set ITEM_NAME = p_itemName , 
		            ITEM_DETAIL = p_itemDetail 
		            where item_seqno = p_itemseqno;
                    
    update pro set pro_stat = p_proStat, 
		                pro_price = p_proPrice, 
		                pro_hits = p_proHits, 
		                pro_saleprice = p_proSaleprice, 
		                pro_opendate = p_proOpendate, 
		                pro_closedate = p_proClosedate, 
		                pro_detail = p_proDetail, 
		                pro_amount = p_proAmount 
		                where pro_seqno = p_seqno;

end;





--����߰�  aucadd
create or replace procedure p_aucadd(
    p_item_name     in item.item_name%type,
    p_id            in mem.mem_id%TYPE,
    p_auc_stat      in auc.auc_stat%TYPE,
    p_auc_price     in auc.auc_price%type,
    p_auc_start     in auc.auc_start%type,
    p_auc_finish    in auc.auc_finish%type,
    p_auc_shortdetail in auc.auc_shortdetail%type,
    p_auc_detail    in auc.auc_detail%type,
    p_itemseqno     in item.item_seqno%type,
     p_rs           out integer
)
is
    v_item_seqno item.item_seqno%type;
    v_auc_seqno  auc.auc_seqno%type;
begin
    v_item_seqno := item_seqno.nextval;
    v_auc_seqno := AUC_SEQNO.nextval;
    
    insert into item(item_seqno,item_name,mem_id) 
        values (v_item_seqno,p_item_name,p_id);

    
    
    insert into auc(auc_seqno, auc_stat, auc_price, auc_start, auc_finish, auc_shortdetail, auc_detail,item_seqno) 
        values (v_auc_seqno, p_auc_stat,p_auc_price,p_auc_start,p_auc_finish,p_auc_shortdetail,p_auc_detail,p_itemseqno);
    
    select max(auc_seqno) into v_auc_seqno from auc;
    p_rs := sql%rowcount;
end;

--�׽�Ʈ
declare 
    v_rs    integer;    
begin
p_aucadd('������','ddd', 'wait', '10000', '22-08-07', '22-08-19', '�ȳ�', '����', '1', v_rs );
 DBMS_OUTPUT.PUT_LINE(v_rs);
end;
commit;


--���ǵ����� aucdetail
create or replace PROCEDURE p_aucdetail(
    p_auc_seqno  in auc.auc_seqno%type,
    p_auc_cur    out SYS_REFCURSOR
)
is
begin
     open p_auc_cur for 
         select (select item_name from item i where i.item_seqno = a.item_seqno) as item_name,
                (select item_img from item i where i.item_seqno = a.item_seqno) as item_img,
                (select item_seqno from item i where i.item_seqno = a.item_seqno) as item_seqno,
                to_char(auc_start,'YYYY-MM-DD') as auc_start,
                to_char(auc_finish,'YYYY-MM-DD') as auc_finish,
                auc_stat,auc_price,auc_shortdetail,auc_detail,auc_seqno
                from auc a
                where auc_seqno = p_auc_seqno;          
end;
/
declare    
    type auction_t is record(
        item_name item.item_name%type,
        item_img item.item_img%type,
        item_seqno item.item_seqno%type,
        auc_start auc.auc_start%type,
        auc_auc_finish auc.auc_finish%type,
        auc_stat auc.auc_stat%type,
        auc_price auc.auc_price%type,
        auc_shortdetail auc.auc_shortdetail%type,
        auc_detail auc.auc_detail%type,
        auc_seqno auc.auc_seqno%type
    );
   
    v_auction_t  auction_t;
    v_auc_cur  SYS_REFCURSOR;
begin
     p_aucdetail(1,v_auc_cur);
    LOOP
    FETCH v_auc_cur  into v_auction_t;
    EXIT WHEN v_auc_cur%NOTFOUND;    
        DBMS_OUTPUT.PUT_LINE(v_auction_t.item_name);  
    END LOOP;
end;
/



--salesHistory �Ǹ��� ����

create or replace PROCEDURE p_salesHistory(
    p_mem_id in mem.mem_id%type,
    p_sale_cur   out SYS_REFCURSOR
)
is
begin
    open p_sale_cur FOR
    select distinct rownum,total,orderdetail_stat, item_name,item_img,pro_opendate,pro_stat from	 
            (select (select item_name from item i where i.item_seqno = p.item_seqno) as item_name, 
            (select item_img from item i where i.item_seqno = p.item_seqno) as item_img, 
            (select mem_id from item i where i.item_seqno = p.item_seqno) as mem_id, 
            pro_opendate,orderdetail_stat,(p.pro_price * amount) as total, pro_stat 
            from pro p , 
            (select pro_seqno, mo.amount, orderdetail_stat from  
            (select od.orderdetail_stat,o.order_seqno from orderdetail od , orders o 
            where od.order_seqno = o.order_seqno) o, 
            (select pro_seqno,order_seqno,amount from mini_order) mo 
            where o.order_seqno = mo.order_seqno) o 
            where p.pro_seqno = o.pro_seqno order by pro_opendate desc) where mem_id = p_mem_id;
end;
/
--�׽�Ʈ
        
declare    
    type sale_t is record(
        p_rn      number,
        p_total   number,
        p_orderdetail_stat  orderdetail.orderdetail_stat%type,
        p_item_name  item.item_name%type,
        p_item_img  item.item_img%type,
        p_pro_opendate  pro.pro_opendate%type,
        p_pro_stat    pro.pro_stat%type      
    );
   
    v_sale_t  sale_t;
    v_sale_cur  SYS_REFCURSOR;
begin
     p_salesHistory('ddd',v_sale_cur);
    LOOP
    FETCH v_sale_cur  into v_sale_t;
    EXIT WHEN v_sale_cur%NOTFOUND;    
        DBMS_OUTPUT.PUT_LINE(v_sale_t.p_item_name); 
        DBMS_OUTPUT.PUT_LINE(v_sale_t.p_pro_stat);
    END LOOP;
end;
/















