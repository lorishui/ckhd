/*
 * 按渠道统计MM一天订单金额，成功金额，订单数，成功订单数，用户数，成功订单用户数
 */
SELECT channel_id,
       SUM(total_price) as amt,
       SUM(CASE WHEN returnStatus= '0' THEN total_price ELSE 0 END) as succ_amt,
       COUNT(1) as num,
       SUM(CASE WHEN returnStatus= '0' THEN 1 ELSE 0 END) as succ_num,
       count(distinct fee_msisdn) as user_num,
       COUNT(distinct CASE WHEN returnStatus= '0' THEN fee_msisdn ELSE null END) as succ_user_num
  FROM mm_app_order
 WHERE `ckapp_id`= '1013'
   and `create_date`>= '2016-06-11'
   and `create_date`< '2016-06-12'
 GROUP BY channel_id
 
/*
 * 按渠道统计咪咕一天订单金额，成功金额，订单数，成功订单数，用户数，成功订单用户数
 */
SELECT channel_id,
       SUM(total_price) as amt,
       SUM(CASE WHEN returnStatus= '0' THEN total_price ELSE 0 END) as succ_amt,
       COUNT(1) as num,
       SUM(CASE WHEN returnStatus= '0' THEN 1 ELSE 0 END) as succ_num,
       count(distinct fee_msisdn) as user_num,
       COUNT(distinct CASE WHEN returnStatus= '0' THEN fee_msisdn ELSE null END) as succ_user_num
  FROM mm_app_order
 WHERE `ckapp_id`= '1013'
   and `create_date`>= '2016-06-11'
   and `create_date`< '2016-06-12'
 GROUP BY channel_id