--以下统计6个APPID
SELECT 
	'300008987802' as app_id,
	'城市飞车（车神争霸）-mm' as app_name,
    t.total_num,
    (t.total_num - t.fail_num) AS succ_num,
    t.fail_num,
    (1 - t.fail_num / t.total_num) AS succ_rate,
    t.total_price / 100 AS total_price,
    (t.total_price - t.fail_price) / 100 AS succ_price,
    t.fail_price / 100 AS fail_price,
    (1 - t.fail_price / t.total_price) AS succ_price_rate
FROM
    (SELECT 
        (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987802') AS total_num,
            (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987802'
                        AND order_id = '00000000000000000000') AS fail_num,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987802') AS total_price,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987802'
                        AND order_id = '00000000000000000000') AS fail_price
    FROM DUAL) AS t
union
SELECT 
	'300008987800' as app_id,
	'城市飞车（送车模）-mm' as app_name,
    t.total_num,
    (t.total_num - t.fail_num) AS succ_num,
    t.fail_num,
    (1 - t.fail_num / t.total_num) AS succ_rate,
    t.total_price / 100 AS total_price,
    (t.total_price - t.fail_price) / 100 AS succ_price,
    t.fail_price / 100 AS fail_price,
    (1 - t.fail_price / t.total_price) AS succ_price_rate
FROM
    (SELECT 
        (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987800') AS total_num,
            (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987800'
                        AND order_id = '00000000000000000000') AS fail_num,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987800') AS total_price,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987800'
                        AND order_id = '00000000000000000000') AS fail_price
    FROM DUAL) AS t
union
SELECT 
	'300008987794' as app_id,
	'城市飞车（OPPO版）-mm' as app_name,
    t.total_num,
    (t.total_num - t.fail_num) AS succ_num,
    t.fail_num,
    (1 - t.fail_num / t.total_num) AS succ_rate,
    t.total_price / 100 AS total_price,
    (t.total_price - t.fail_price) / 100 AS succ_price,
    t.fail_price / 100 AS fail_price,
    (1 - t.fail_price / t.total_price) AS succ_price_rate
FROM
    (SELECT 
        (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987794') AS total_num,
            (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987794'
                        AND order_id = '00000000000000000000') AS fail_num,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987794') AS total_price,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008987794'
                        AND order_id = '00000000000000000000') AS fail_price
    FROM DUAL) AS t
union
SELECT 
	'300008924829' as app_id,
	'城市飞车（2015）-mm' as app_name,
    t.total_num,
    (t.total_num - t.fail_num) AS succ_num,
    t.fail_num,
    (1 - t.fail_num / t.total_num) AS succ_rate,
    t.total_price / 100 AS total_price,
    (t.total_price - t.fail_price) / 100 AS succ_price,
    t.fail_price / 100 AS fail_price,
    (1 - t.fail_price / t.total_price) AS succ_price_rate
FROM
    (SELECT 
        (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008924829') AS total_num,
            (SELECT 
                    COUNT(0)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008924829'
                        AND order_id = '00000000000000000000') AS fail_num,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008924829') AS total_price,
            (SELECT 
                    SUM(total_price)
                FROM
                    mm_app_order
                WHERE
                    app_id = '300008924829'
                        AND order_id = '00000000000000000000') AS fail_price
    FROM DUAL) AS t
union
SELECT 
	'653316037965' as app_id,
	'城市飞车-and' as app_name,
    t.total_num,
    t.succ_num,
    (t.total_num - t.succ_num) AS fail_num,
    (t.succ_num / t.total_num) AS succ_rate,
    t.total_price / 100 AS total_price,
    t.succ_price / 100 AS succ_price,
    (t.total_price - t.succ_price) / 100 AS fail_price,
    (t.succ_price / t.total_price) AS succ_price_rate
FROM
    (SELECT 
        (SELECT 
                    COUNT(0)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316037965') AS total_num,
            (SELECT 
                    COUNT(0)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316037965'
                        AND hRet = '0') AS succ_num,
            (SELECT 
                    SUM(price)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316037965') AS total_price,
            (SELECT 
                    SUM(price)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316037965'
                         AND hRet = '0') AS succ_price
    FROM DUAL) AS t
union
SELECT 
	'653316039050' as app_id,
	'果宝-and' as app_name,
    t.total_num,
    t.succ_num,
    (t.total_num - t.succ_num) AS fail_num,
    (t.succ_num / t.total_num) AS succ_rate,
    t.total_price / 100 AS total_price,
    t.succ_price / 100 AS succ_price,
    (t.total_price - t.succ_price) / 100 AS fail_price,
    (t.succ_price / t.total_price) AS succ_price_rate
FROM
    (SELECT 
        (SELECT 
                    COUNT(0)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316039050') AS total_num,
            (SELECT 
                    COUNT(0)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316039050'
                        AND hRet = '0') AS succ_num,
            (SELECT 
                    SUM(price)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316039050') AS total_price,
            (SELECT 
                    SUM(price)
                FROM
                    and_app_order
                WHERE
                    contentId = '653316039050'
                         AND hRet = '0') AS succ_price
    FROM DUAL) AS t;