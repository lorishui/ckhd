--选择数据库
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0000_RDS'*/
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0001_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0002_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0003_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0004_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0005_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0006_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0007_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0008_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0009_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0010_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0011_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0012_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0013_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0014_RDS'*/ 
/*TDDL:node='OPEN_GAME_EVENT_1448544211633RVWKOPEN_GAME_EVENT_WRNY_0015_RDS'*/ 
--根据分表查询
select '000' as t,count(0) from ck_app_event_000 where insert_time <= '2016-08-20' 
union all 
select '001' as t,count(0) from ck_app_event_001 where insert_time <= '2016-08-20' 
union all 
select '002' as t,count(0) from ck_app_event_002 where insert_time <= '2016-08-20' 
union all 
select '003' as t,count(0) from ck_app_event_003 where insert_time <= '2016-08-20' 
union all 
select '004' as t,count(0) from ck_app_event_004 where insert_time <= '2016-08-20' 
union all 
select '005' as t,count(0) from ck_app_event_005 where insert_time <= '2016-08-20' 
union all 
select '006' as t,count(0) from ck_app_event_006 where insert_time <= '2016-08-20' 
union all 
select '007' as t,count(0) from ck_app_event_007 where insert_time <= '2016-08-20' 
union all 
select '008' as t,count(0) from ck_app_event_008 where insert_time <= '2016-08-20' 
union all 
select '009' as t,count(0) from ck_app_event_009 where insert_time <= '2016-08-20' 
union all 
select '010' as t,count(0) from ck_app_event_010 where insert_time <= '2016-08-20' 
union all 
select '011' as t,count(0) from ck_app_event_011 where insert_time <= '2016-08-20' 
union all 
select '012' as t,count(0) from ck_app_event_012 where insert_time <= '2016-08-20' 
union all 
select '013' as t,count(0) from ck_app_event_013 where insert_time <= '2016-08-20' 
union all 
select '014' as t,count(0) from ck_app_event_014 where insert_time <= '2016-08-20' 
union all 
select '015' as t,count(0) from ck_app_event_015 where insert_time <= '2016-08-20' 
union all 
select '016' as t,count(0) from ck_app_event_016 where insert_time <= '2016-08-20' 
union all 
select '017' as t,count(0) from ck_app_event_017 where insert_time <= '2016-08-20' 
union all 
select '018' as t,count(0) from ck_app_event_018 where insert_time <= '2016-08-20' 
union all 
select '019' as t,count(0) from ck_app_event_019 where insert_time <= '2016-08-20' 
union all 
select '020' as t,count(0) from ck_app_event_020 where insert_time <= '2016-08-20' 
union all 
select '021' as t,count(0) from ck_app_event_021 where insert_time <= '2016-08-20' 
union all 
select '022' as t,count(0) from ck_app_event_022 where insert_time <= '2016-08-20' 
union all 
select '023' as t,count(0) from ck_app_event_023 where insert_time <= '2016-08-20' 
union all 
select '024' as t,count(0) from ck_app_event_024 where insert_time <= '2016-08-20' 
union all 
select '025' as t,count(0) from ck_app_event_025 where insert_time <= '2016-08-20' 
union all 
select '026' as t,count(0) from ck_app_event_026 where insert_time <= '2016-08-20' 
union all 
select '027' as t,count(0) from ck_app_event_027 where insert_time <= '2016-08-20' 
union all 
select '028' as t,count(0) from ck_app_event_028 where insert_time <= '2016-08-20' 
union all 
select '029' as t,count(0) from ck_app_event_029 where insert_time <= '2016-08-20' 
union all 
select '030' as t,count(0) from ck_app_event_030 where insert_time <= '2016-08-20' 
union all 
select '031' as t,count(0) from ck_app_event_031 where insert_time <= '2016-08-20' 
union all 
select '032' as t,count(0) from ck_app_event_032 where insert_time <= '2016-08-20' 
union all 
select '033' as t,count(0) from ck_app_event_033 where insert_time <= '2016-08-20' 
union all 
select '034' as t,count(0) from ck_app_event_034 where insert_time <= '2016-08-20' 
union all 
select '035' as t,count(0) from ck_app_event_035 where insert_time <= '2016-08-20' 
union all 
select '036' as t,count(0) from ck_app_event_036 where insert_time <= '2016-08-20' 
union all 
select '037' as t,count(0) from ck_app_event_037 where insert_time <= '2016-08-20' 
union all 
select '038' as t,count(0) from ck_app_event_038 where insert_time <= '2016-08-20' 
union all 
select '039' as t,count(0) from ck_app_event_039 where insert_time <= '2016-08-20' 
union all 
select '040' as t,count(0) from ck_app_event_040 where insert_time <= '2016-08-20' 
union all 
select '041' as t,count(0) from ck_app_event_041 where insert_time <= '2016-08-20' 
union all 
select '042' as t,count(0) from ck_app_event_042 where insert_time <= '2016-08-20' 
union all 
select '043' as t,count(0) from ck_app_event_043 where insert_time <= '2016-08-20' 
union all 
select '044' as t,count(0) from ck_app_event_044 where insert_time <= '2016-08-20' 
union all 
select '045' as t,count(0) from ck_app_event_045 where insert_time <= '2016-08-20' 
union all 
select '046' as t,count(0) from ck_app_event_046 where insert_time <= '2016-08-20' 
union all 
select '047' as t,count(0) from ck_app_event_047 where insert_time <= '2016-08-20' 
union all 
select '048' as t,count(0) from ck_app_event_048 where insert_time <= '2016-08-20' 
union all 
select '049' as t,count(0) from ck_app_event_049 where insert_time <= '2016-08-20' 
union all 
select '050' as t,count(0) from ck_app_event_050 where insert_time <= '2016-08-20' 
union all 
select '051' as t,count(0) from ck_app_event_051 where insert_time <= '2016-08-20' 
union all 
select '052' as t,count(0) from ck_app_event_052 where insert_time <= '2016-08-20' 
union all 
select '053' as t,count(0) from ck_app_event_053 where insert_time <= '2016-08-20' 
union all 
select '054' as t,count(0) from ck_app_event_054 where insert_time <= '2016-08-20' 
union all 
select '055' as t,count(0) from ck_app_event_055 where insert_time <= '2016-08-20' 
union all 
select '056' as t,count(0) from ck_app_event_056 where insert_time <= '2016-08-20' 
union all 
select '057' as t,count(0) from ck_app_event_057 where insert_time <= '2016-08-20' 
union all 
select '058' as t,count(0) from ck_app_event_058 where insert_time <= '2016-08-20' 
union all 
select '059' as t,count(0) from ck_app_event_059 where insert_time <= '2016-08-20' 
union all 
select '060' as t,count(0) from ck_app_event_060 where insert_time <= '2016-08-20' 
union all 
select '061' as t,count(0) from ck_app_event_061 where insert_time <= '2016-08-20' 
union all 
select '062' as t,count(0) from ck_app_event_062 where insert_time <= '2016-08-20' 
union all 
select '063' as t,count(0) from ck_app_event_063 where insert_time <= '2016-08-20' 
union all 
select '064' as t,count(0) from ck_app_event_064 where insert_time <= '2016-08-20' 
union all 
select '065' as t,count(0) from ck_app_event_065 where insert_time <= '2016-08-20' 
union all 
select '066' as t,count(0) from ck_app_event_066 where insert_time <= '2016-08-20' 
union all 
select '067' as t,count(0) from ck_app_event_067 where insert_time <= '2016-08-20' 
union all 
select '068' as t,count(0) from ck_app_event_068 where insert_time <= '2016-08-20' 
union all 
select '069' as t,count(0) from ck_app_event_069 where insert_time <= '2016-08-20' 
union all 
select '070' as t,count(0) from ck_app_event_070 where insert_time <= '2016-08-20' 
union all 
select '071' as t,count(0) from ck_app_event_071 where insert_time <= '2016-08-20' 
union all 
select '072' as t,count(0) from ck_app_event_072 where insert_time <= '2016-08-20' 
union all 
select '073' as t,count(0) from ck_app_event_073 where insert_time <= '2016-08-20' 
union all 
select '074' as t,count(0) from ck_app_event_074 where insert_time <= '2016-08-20' 
union all 
select '075' as t,count(0) from ck_app_event_075 where insert_time <= '2016-08-20' 
union all 
select '076' as t,count(0) from ck_app_event_076 where insert_time <= '2016-08-20' 
union all 
select '077' as t,count(0) from ck_app_event_077 where insert_time <= '2016-08-20' 
union all 
select '078' as t,count(0) from ck_app_event_078 where insert_time <= '2016-08-20' 
union all 
select '079' as t,count(0) from ck_app_event_079 where insert_time <= '2016-08-20' 
union all 
select '080' as t,count(0) from ck_app_event_080 where insert_time <= '2016-08-20' 
union all 
select '081' as t,count(0) from ck_app_event_081 where insert_time <= '2016-08-20' 
union all 
select '082' as t,count(0) from ck_app_event_082 where insert_time <= '2016-08-20' 
union all 
select '083' as t,count(0) from ck_app_event_083 where insert_time <= '2016-08-20' 
union all 
select '084' as t,count(0) from ck_app_event_084 where insert_time <= '2016-08-20' 
union all 
select '085' as t,count(0) from ck_app_event_085 where insert_time <= '2016-08-20' 
union all 
select '086' as t,count(0) from ck_app_event_086 where insert_time <= '2016-08-20' 
union all 
select '087' as t,count(0) from ck_app_event_087 where insert_time <= '2016-08-20' 
union all 
select '088' as t,count(0) from ck_app_event_088 where insert_time <= '2016-08-20' 
union all 
select '089' as t,count(0) from ck_app_event_089 where insert_time <= '2016-08-20' 
union all 
select '090' as t,count(0) from ck_app_event_090 where insert_time <= '2016-08-20' 
union all 
select '091' as t,count(0) from ck_app_event_091 where insert_time <= '2016-08-20' 
union all 
select '092' as t,count(0) from ck_app_event_092 where insert_time <= '2016-08-20' 
union all 
select '093' as t,count(0) from ck_app_event_093 where insert_time <= '2016-08-20' 
union all 
select '094' as t,count(0) from ck_app_event_094 where insert_time <= '2016-08-20' 
union all 
select '095' as t,count(0) from ck_app_event_095 where insert_time <= '2016-08-20' 
union all 
select '096' as t,count(0) from ck_app_event_096 where insert_time <= '2016-08-20' 
union all 
select '097' as t,count(0) from ck_app_event_097 where insert_time <= '2016-08-20' 
union all 
select '098' as t,count(0) from ck_app_event_098 where insert_time <= '2016-08-20' 
union all 
select '099' as t,count(0) from ck_app_event_099 where insert_time <= '2016-08-20' 
union all 
select '100' as t,count(0) from ck_app_event_100 where insert_time <= '2016-08-20' 
union all 
select '101' as t,count(0) from ck_app_event_101 where insert_time <= '2016-08-20' 
union all 
select '102' as t,count(0) from ck_app_event_102 where insert_time <= '2016-08-20' 
union all 
select '103' as t,count(0) from ck_app_event_103 where insert_time <= '2016-08-20' 
union all 
select '104' as t,count(0) from ck_app_event_104 where insert_time <= '2016-08-20' 
union all 
select '105' as t,count(0) from ck_app_event_105 where insert_time <= '2016-08-20' 
union all 
select '106' as t,count(0) from ck_app_event_106 where insert_time <= '2016-08-20' 
union all 
select '107' as t,count(0) from ck_app_event_107 where insert_time <= '2016-08-20' 
union all 
select '108' as t,count(0) from ck_app_event_108 where insert_time <= '2016-08-20' 
union all 
select '109' as t,count(0) from ck_app_event_109 where insert_time <= '2016-08-20' 
union all 
select '110' as t,count(0) from ck_app_event_110 where insert_time <= '2016-08-20' 
union all 
select '111' as t,count(0) from ck_app_event_111 where insert_time <= '2016-08-20' 
union all 
select '112' as t,count(0) from ck_app_event_112 where insert_time <= '2016-08-20' 
union all 
select '113' as t,count(0) from ck_app_event_113 where insert_time <= '2016-08-20' 
union all 
select '114' as t,count(0) from ck_app_event_114 where insert_time <= '2016-08-20' 
union all 
select '115' as t,count(0) from ck_app_event_115 where insert_time <= '2016-08-20' 
union all 
select '116' as t,count(0) from ck_app_event_116 where insert_time <= '2016-08-20' 
union all 
select '117' as t,count(0) from ck_app_event_117 where insert_time <= '2016-08-20' 
union all 
select '118' as t,count(0) from ck_app_event_118 where insert_time <= '2016-08-20' 
union all 
select '119' as t,count(0) from ck_app_event_119 where insert_time <= '2016-08-20' 
union all 
select '120' as t,count(0) from ck_app_event_120 where insert_time <= '2016-08-20' 
union all 
select '121' as t,count(0) from ck_app_event_121 where insert_time <= '2016-08-20' 
union all 
select '122' as t,count(0) from ck_app_event_122 where insert_time <= '2016-08-20' 
union all 
select '123' as t,count(0) from ck_app_event_123 where insert_time <= '2016-08-20' 
union all 
select '124' as t,count(0) from ck_app_event_124 where insert_time <= '2016-08-20' 
union all 
select '125' as t,count(0) from ck_app_event_125 where insert_time <= '2016-08-20' 
union all 
select '126' as t,count(0) from ck_app_event_126 where insert_time <= '2016-08-20' 
union all 
select '127' as t,count(0) from ck_app_event_127 where insert_time <= '2016-08-20' 
union all 
select '128' as t,count(0) from ck_app_event_128 where insert_time <= '2016-08-20' 
union all 
select '129' as t,count(0) from ck_app_event_129 where insert_time <= '2016-08-20' 
union all 
select '130' as t,count(0) from ck_app_event_130 where insert_time <= '2016-08-20' 
union all 
select '131' as t,count(0) from ck_app_event_131 where insert_time <= '2016-08-20' 
union all 
select '132' as t,count(0) from ck_app_event_132 where insert_time <= '2016-08-20' 
union all 
select '133' as t,count(0) from ck_app_event_133 where insert_time <= '2016-08-20' 
union all 
select '134' as t,count(0) from ck_app_event_134 where insert_time <= '2016-08-20' 
union all 
select '135' as t,count(0) from ck_app_event_135 where insert_time <= '2016-08-20' 
union all 
select '136' as t,count(0) from ck_app_event_136 where insert_time <= '2016-08-20' 
union all 
select '137' as t,count(0) from ck_app_event_137 where insert_time <= '2016-08-20' 
union all 
select '138' as t,count(0) from ck_app_event_138 where insert_time <= '2016-08-20' 
union all 
select '139' as t,count(0) from ck_app_event_139 where insert_time <= '2016-08-20' 
union all 
select '140' as t,count(0) from ck_app_event_140 where insert_time <= '2016-08-20' 
union all 
select '141' as t,count(0) from ck_app_event_141 where insert_time <= '2016-08-20' 
union all 
select '142' as t,count(0) from ck_app_event_142 where insert_time <= '2016-08-20' 
union all 
select '143' as t,count(0) from ck_app_event_143 where insert_time <= '2016-08-20' 
union all 
select '144' as t,count(0) from ck_app_event_144 where insert_time <= '2016-08-20' 
union all 
select '145' as t,count(0) from ck_app_event_145 where insert_time <= '2016-08-20' 
union all 
select '146' as t,count(0) from ck_app_event_146 where insert_time <= '2016-08-20' 
union all 
select '147' as t,count(0) from ck_app_event_147 where insert_time <= '2016-08-20' 
union all 
select '148' as t,count(0) from ck_app_event_148 where insert_time <= '2016-08-20' 
union all 
select '149' as t,count(0) from ck_app_event_149 where insert_time <= '2016-08-20' 
union all 
select '150' as t,count(0) from ck_app_event_150 where insert_time <= '2016-08-20' 
union all 
select '151' as t,count(0) from ck_app_event_151 where insert_time <= '2016-08-20' 
union all 
select '152' as t,count(0) from ck_app_event_152 where insert_time <= '2016-08-20' 
union all 
select '153' as t,count(0) from ck_app_event_153 where insert_time <= '2016-08-20' 
union all 
select '154' as t,count(0) from ck_app_event_154 where insert_time <= '2016-08-20' 
union all 
select '155' as t,count(0) from ck_app_event_155 where insert_time <= '2016-08-20' 
union all 
select '156' as t,count(0) from ck_app_event_156 where insert_time <= '2016-08-20' 
union all 
select '157' as t,count(0) from ck_app_event_157 where insert_time <= '2016-08-20' 
union all 
select '158' as t,count(0) from ck_app_event_158 where insert_time <= '2016-08-20' 
union all 
select '159' as t,count(0) from ck_app_event_159 where insert_time <= '2016-08-20' 
union all 
select '160' as t,count(0) from ck_app_event_160 where insert_time <= '2016-08-20' 
union all 
select '161' as t,count(0) from ck_app_event_161 where insert_time <= '2016-08-20' 
union all 
select '162' as t,count(0) from ck_app_event_162 where insert_time <= '2016-08-20' 
union all 
select '163' as t,count(0) from ck_app_event_163 where insert_time <= '2016-08-20' 
union all 
select '164' as t,count(0) from ck_app_event_164 where insert_time <= '2016-08-20' 
union all 
select '165' as t,count(0) from ck_app_event_165 where insert_time <= '2016-08-20' 
union all 
select '166' as t,count(0) from ck_app_event_166 where insert_time <= '2016-08-20' 
union all 
select '167' as t,count(0) from ck_app_event_167 where insert_time <= '2016-08-20' 
union all 
select '168' as t,count(0) from ck_app_event_168 where insert_time <= '2016-08-20' 
union all 
select '169' as t,count(0) from ck_app_event_169 where insert_time <= '2016-08-20' 
union all 
select '170' as t,count(0) from ck_app_event_170 where insert_time <= '2016-08-20' 
union all 
select '171' as t,count(0) from ck_app_event_171 where insert_time <= '2016-08-20' 
union all 
select '172' as t,count(0) from ck_app_event_172 where insert_time <= '2016-08-20' 
union all 
select '173' as t,count(0) from ck_app_event_173 where insert_time <= '2016-08-20' 
union all 
select '174' as t,count(0) from ck_app_event_174 where insert_time <= '2016-08-20' 
union all 
select '175' as t,count(0) from ck_app_event_175 where insert_time <= '2016-08-20' 
union all 
select '176' as t,count(0) from ck_app_event_176 where insert_time <= '2016-08-20' 
union all 
select '177' as t,count(0) from ck_app_event_177 where insert_time <= '2016-08-20' 
union all 
select '178' as t,count(0) from ck_app_event_178 where insert_time <= '2016-08-20' 
union all 
select '179' as t,count(0) from ck_app_event_179 where insert_time <= '2016-08-20' 
union all 
select '180' as t,count(0) from ck_app_event_180 where insert_time <= '2016-08-20' 
union all 
select '181' as t,count(0) from ck_app_event_181 where insert_time <= '2016-08-20' 
union all 
select '182' as t,count(0) from ck_app_event_182 where insert_time <= '2016-08-20' 
union all 
select '183' as t,count(0) from ck_app_event_183 where insert_time <= '2016-08-20' 
union all 
select '184' as t,count(0) from ck_app_event_184 where insert_time <= '2016-08-20' 
union all 
select '185' as t,count(0) from ck_app_event_185 where insert_time <= '2016-08-20' 
union all 
select '186' as t,count(0) from ck_app_event_186 where insert_time <= '2016-08-20' 
union all 
select '187' as t,count(0) from ck_app_event_187 where insert_time <= '2016-08-20' 
union all 
select '188' as t,count(0) from ck_app_event_188 where insert_time <= '2016-08-20' 
union all 
select '189' as t,count(0) from ck_app_event_189 where insert_time <= '2016-08-20' 
union all 
select '190' as t,count(0) from ck_app_event_190 where insert_time <= '2016-08-20' 
union all 
select '191' as t,count(0) from ck_app_event_191 where insert_time <= '2016-08-20' 
union all 
select '192' as t,count(0) from ck_app_event_192 where insert_time <= '2016-08-20' 
union all 
select '193' as t,count(0) from ck_app_event_193 where insert_time <= '2016-08-20' 
union all 
select '194' as t,count(0) from ck_app_event_194 where insert_time <= '2016-08-20' 
union all 
select '195' as t,count(0) from ck_app_event_195 where insert_time <= '2016-08-20' 
union all 
select '196' as t,count(0) from ck_app_event_196 where insert_time <= '2016-08-20' 
union all 
select '197' as t,count(0) from ck_app_event_197 where insert_time <= '2016-08-20' 
union all 
select '198' as t,count(0) from ck_app_event_198 where insert_time <= '2016-08-20' 
union all 
select '199' as t,count(0) from ck_app_event_199 where insert_time <= '2016-08-20' 
union all 
select '200' as t,count(0) from ck_app_event_200 where insert_time <= '2016-08-20' 
union all
select '201' as t,count(0) from ck_app_event_201 where insert_time > '2016-08-20' 
union all 
select '202' as t,count(0) from ck_app_event_202 where insert_time > '2016-08-20' 
union all 
select '203' as t,count(0) from ck_app_event_203 where insert_time > '2016-08-20' 
union all 
select '204' as t,count(0) from ck_app_event_204 where insert_time > '2016-08-20' 
union all 
select '205' as t,count(0) from ck_app_event_205 where insert_time > '2016-08-20' 
union all 
select '206' as t,count(0) from ck_app_event_206 where insert_time > '2016-08-20' 
union all 
select '207' as t,count(0) from ck_app_event_207 where insert_time > '2016-08-20' 
union all 
select '208' as t,count(0) from ck_app_event_208 where insert_time > '2016-08-20' 
union all 
select '209' as t,count(0) from ck_app_event_209 where insert_time > '2016-08-20' 
union all 
select '210' as t,count(0) from ck_app_event_210 where insert_time > '2016-08-20' 
union all 
select '211' as t,count(0) from ck_app_event_211 where insert_time > '2016-08-20' 
union all 
select '212' as t,count(0) from ck_app_event_212 where insert_time > '2016-08-20' 
union all 
select '213' as t,count(0) from ck_app_event_213 where insert_time > '2016-08-20' 
union all 
select '214' as t,count(0) from ck_app_event_214 where insert_time > '2016-08-20' 
union all 
select '215' as t,count(0) from ck_app_event_215 where insert_time > '2016-08-20' 
union all 
select '216' as t,count(0) from ck_app_event_216 where insert_time > '2016-08-20' 
union all 
select '217' as t,count(0) from ck_app_event_217 where insert_time > '2016-08-20' 
union all 
select '218' as t,count(0) from ck_app_event_218 where insert_time > '2016-08-20' 
union all 
select '219' as t,count(0) from ck_app_event_219 where insert_time > '2016-08-20' 
union all 
select '220' as t,count(0) from ck_app_event_220 where insert_time > '2016-08-20' 
union all 
select '221' as t,count(0) from ck_app_event_221 where insert_time > '2016-08-20' 
union all 
select '222' as t,count(0) from ck_app_event_222 where insert_time > '2016-08-20' 
union all 
select '223' as t,count(0) from ck_app_event_223 where insert_time > '2016-08-20' 
union all 
select '224' as t,count(0) from ck_app_event_224 where insert_time > '2016-08-20' 
union all 
select '225' as t,count(0) from ck_app_event_225 where insert_time > '2016-08-20' 
union all 
select '226' as t,count(0) from ck_app_event_226 where insert_time > '2016-08-20' 
union all 
select '227' as t,count(0) from ck_app_event_227 where insert_time > '2016-08-20' 
union all 
select '228' as t,count(0) from ck_app_event_228 where insert_time > '2016-08-20' 
union all 
select '229' as t,count(0) from ck_app_event_229 where insert_time > '2016-08-20' 
union all 
select '230' as t,count(0) from ck_app_event_230 where insert_time > '2016-08-20' 
union all 
select '231' as t,count(0) from ck_app_event_231 where insert_time > '2016-08-20' 
union all 
select '232' as t,count(0) from ck_app_event_232 where insert_time > '2016-08-20' 
union all 
select '233' as t,count(0) from ck_app_event_233 where insert_time > '2016-08-20' 
union all 
select '234' as t,count(0) from ck_app_event_234 where insert_time > '2016-08-20' 
union all 
select '235' as t,count(0) from ck_app_event_235 where insert_time > '2016-08-20' 
union all 
select '236' as t,count(0) from ck_app_event_236 where insert_time > '2016-08-20' 
union all 
select '237' as t,count(0) from ck_app_event_237 where insert_time > '2016-08-20' 
union all 
select '238' as t,count(0) from ck_app_event_238 where insert_time > '2016-08-20' 
union all 
select '239' as t,count(0) from ck_app_event_239 where insert_time > '2016-08-20' 
union all 
select '240' as t,count(0) from ck_app_event_240 where insert_time > '2016-08-20' 
union all 
select '241' as t,count(0) from ck_app_event_241 where insert_time > '2016-08-20' 
union all 
select '242' as t,count(0) from ck_app_event_242 where insert_time > '2016-08-20' 
union all 
select '243' as t,count(0) from ck_app_event_243 where insert_time > '2016-08-20' 
union all 
select '244' as t,count(0) from ck_app_event_244 where insert_time > '2016-08-20' 
union all 
select '245' as t,count(0) from ck_app_event_245 where insert_time > '2016-08-20' 
union all 
select '246' as t,count(0) from ck_app_event_246 where insert_time > '2016-08-20' 
union all 
select '247' as t,count(0) from ck_app_event_247 where insert_time > '2016-08-20' 
union all 
select '248' as t,count(0) from ck_app_event_248 where insert_time > '2016-08-20' 
union all 
select '249' as t,count(0) from ck_app_event_249 where insert_time > '2016-08-20' 
union all 
select '250' as t,count(0) from ck_app_event_250 where insert_time > '2016-08-20' 
union all 
select '251' as t,count(0) from ck_app_event_251 where insert_time > '2016-08-20' 
union all 
select '252' as t,count(0) from ck_app_event_252 where insert_time > '2016-08-20' 
union all 
select '253' as t,count(0) from ck_app_event_253 where insert_time > '2016-08-20' 
union all 
select '254' as t,count(0) from ck_app_event_254 where insert_time > '2016-08-20' 
union all 
select '255' as t,count(0) from ck_app_event_255 where insert_time > '2016-08-20' 
union all 
select '256' as t,count(0) from ck_app_event_256 where insert_time > '2016-08-20' 
union all 
select '257' as t,count(0) from ck_app_event_257 where insert_time > '2016-08-20' 
union all 
select '258' as t,count(0) from ck_app_event_258 where insert_time > '2016-08-20' 
union all 
select '259' as t,count(0) from ck_app_event_259 where insert_time > '2016-08-20' 
union all 
select '260' as t,count(0) from ck_app_event_260 where insert_time > '2016-08-20' 
union all 
select '261' as t,count(0) from ck_app_event_261 where insert_time > '2016-08-20' 
union all 
select '262' as t,count(0) from ck_app_event_262 where insert_time > '2016-08-20' 
union all 
select '263' as t,count(0) from ck_app_event_263 where insert_time > '2016-08-20' 
union all 
select '264' as t,count(0) from ck_app_event_264 where insert_time > '2016-08-20' 
union all 
select '265' as t,count(0) from ck_app_event_265 where insert_time > '2016-08-20' 
union all 
select '266' as t,count(0) from ck_app_event_266 where insert_time > '2016-08-20' 
union all 
select '267' as t,count(0) from ck_app_event_267 where insert_time > '2016-08-20' 
union all 
select '268' as t,count(0) from ck_app_event_268 where insert_time > '2016-08-20' 
union all 
select '269' as t,count(0) from ck_app_event_269 where insert_time > '2016-08-20' 
union all 
select '270' as t,count(0) from ck_app_event_270 where insert_time > '2016-08-20' 
union all 
select '271' as t,count(0) from ck_app_event_271 where insert_time > '2016-08-20' 
union all 
select '272' as t,count(0) from ck_app_event_272 where insert_time > '2016-08-20' 
union all 
select '273' as t,count(0) from ck_app_event_273 where insert_time > '2016-08-20' 
union all 
select '274' as t,count(0) from ck_app_event_274 where insert_time > '2016-08-20' 
union all 
select '275' as t,count(0) from ck_app_event_275 where insert_time > '2016-08-20' 
union all 
select '276' as t,count(0) from ck_app_event_276 where insert_time > '2016-08-20' 
union all 
select '277' as t,count(0) from ck_app_event_277 where insert_time > '2016-08-20' 
union all 
select '278' as t,count(0) from ck_app_event_278 where insert_time > '2016-08-20' 
union all 
select '279' as t,count(0) from ck_app_event_279 where insert_time > '2016-08-20' 
union all 
select '280' as t,count(0) from ck_app_event_280 where insert_time > '2016-08-20' 
union all 
select '281' as t,count(0) from ck_app_event_281 where insert_time > '2016-08-20' 
union all 
select '282' as t,count(0) from ck_app_event_282 where insert_time > '2016-08-20' 
union all 
select '283' as t,count(0) from ck_app_event_283 where insert_time > '2016-08-20' 
union all 
select '284' as t,count(0) from ck_app_event_284 where insert_time > '2016-08-20' 
union all 
select '285' as t,count(0) from ck_app_event_285 where insert_time > '2016-08-20' 
union all 
select '286' as t,count(0) from ck_app_event_286 where insert_time > '2016-08-20' 
union all 
select '287' as t,count(0) from ck_app_event_287 where insert_time > '2016-08-20' 
union all 
select '288' as t,count(0) from ck_app_event_288 where insert_time > '2016-08-20' 
union all 
select '289' as t,count(0) from ck_app_event_289 where insert_time > '2016-08-20' 
union all 
select '290' as t,count(0) from ck_app_event_290 where insert_time > '2016-08-20' 
union all 
select '291' as t,count(0) from ck_app_event_291 where insert_time > '2016-08-20' 
union all 
select '292' as t,count(0) from ck_app_event_292 where insert_time > '2016-08-20' 
union all 
select '293' as t,count(0) from ck_app_event_293 where insert_time > '2016-08-20' 
union all 
select '294' as t,count(0) from ck_app_event_294 where insert_time > '2016-08-20' 
union all 
select '295' as t,count(0) from ck_app_event_295 where insert_time > '2016-08-20' 
union all 
select '296' as t,count(0) from ck_app_event_296 where insert_time > '2016-08-20' 
union all 
select '297' as t,count(0) from ck_app_event_297 where insert_time > '2016-08-20' 
union all 
select '298' as t,count(0) from ck_app_event_298 where insert_time > '2016-08-20' 
union all 
select '299' as t,count(0) from ck_app_event_299 where insert_time > '2016-08-20' 
union all 
select '300' as t,count(0) from ck_app_event_300 where insert_time > '2016-08-20' 
union all 
select '301' as t,count(0) from ck_app_event_301 where insert_time > '2016-08-20' 
union all 
select '302' as t,count(0) from ck_app_event_302 where insert_time > '2016-08-20' 
union all 
select '303' as t,count(0) from ck_app_event_303 where insert_time > '2016-08-20' 
union all 
select '304' as t,count(0) from ck_app_event_304 where insert_time > '2016-08-20' 
union all 
select '305' as t,count(0) from ck_app_event_305 where insert_time > '2016-08-20' 
union all 
select '306' as t,count(0) from ck_app_event_306 where insert_time > '2016-08-20' 
union all 
select '307' as t,count(0) from ck_app_event_307 where insert_time > '2016-08-20' 
union all 
select '308' as t,count(0) from ck_app_event_308 where insert_time > '2016-08-20' 
union all 
select '309' as t,count(0) from ck_app_event_309 where insert_time > '2016-08-20' 
union all 
select '310' as t,count(0) from ck_app_event_310 where insert_time > '2016-08-20' 
union all 
select '311' as t,count(0) from ck_app_event_311 where insert_time > '2016-08-20' 
union all 
select '312' as t,count(0) from ck_app_event_312 where insert_time > '2016-08-20' 
union all 
select '313' as t,count(0) from ck_app_event_313 where insert_time > '2016-08-20' 
union all 
select '314' as t,count(0) from ck_app_event_314 where insert_time > '2016-08-20' 
union all 
select '315' as t,count(0) from ck_app_event_315 where insert_time > '2016-08-20' 
union all 
select '316' as t,count(0) from ck_app_event_316 where insert_time > '2016-08-20' 
union all 
select '317' as t,count(0) from ck_app_event_317 where insert_time > '2016-08-20' 
union all 
select '318' as t,count(0) from ck_app_event_318 where insert_time > '2016-08-20' 
union all 
select '319' as t,count(0) from ck_app_event_319 where insert_time > '2016-08-20' 
union all 
select '320' as t,count(0) from ck_app_event_320 where insert_time > '2016-08-20' 
union all 
select '321' as t,count(0) from ck_app_event_321 where insert_time > '2016-08-20' 
union all 
select '322' as t,count(0) from ck_app_event_322 where insert_time > '2016-08-20' 
union all 
select '323' as t,count(0) from ck_app_event_323 where insert_time > '2016-08-20' 
union all 
select '324' as t,count(0) from ck_app_event_324 where insert_time > '2016-08-20' 
union all 
select '325' as t,count(0) from ck_app_event_325 where insert_time > '2016-08-20' 
union all 
select '326' as t,count(0) from ck_app_event_326 where insert_time > '2016-08-20' 
union all 
select '327' as t,count(0) from ck_app_event_327 where insert_time > '2016-08-20' 
union all 
select '328' as t,count(0) from ck_app_event_328 where insert_time > '2016-08-20' 
union all 
select '329' as t,count(0) from ck_app_event_329 where insert_time > '2016-08-20' 
union all 
select '330' as t,count(0) from ck_app_event_330 where insert_time > '2016-08-20' 
union all 
select '331' as t,count(0) from ck_app_event_331 where insert_time > '2016-08-20' 
union all 
select '332' as t,count(0) from ck_app_event_332 where insert_time > '2016-08-20' 
union all 
select '333' as t,count(0) from ck_app_event_333 where insert_time > '2016-08-20' 
union all 
select '334' as t,count(0) from ck_app_event_334 where insert_time > '2016-08-20' 
union all 
select '335' as t,count(0) from ck_app_event_335 where insert_time > '2016-08-20' 
union all 
select '336' as t,count(0) from ck_app_event_336 where insert_time > '2016-08-20' 
union all 
select '337' as t,count(0) from ck_app_event_337 where insert_time > '2016-08-20' 
union all 
select '338' as t,count(0) from ck_app_event_338 where insert_time > '2016-08-20' 
union all 
select '339' as t,count(0) from ck_app_event_339 where insert_time > '2016-08-20' 
union all 
select '340' as t,count(0) from ck_app_event_340 where insert_time > '2016-08-20' 
union all 
select '341' as t,count(0) from ck_app_event_341 where insert_time > '2016-08-20' 
union all 
select '342' as t,count(0) from ck_app_event_342 where insert_time > '2016-08-20' 
union all 
select '343' as t,count(0) from ck_app_event_343 where insert_time > '2016-08-20' 
union all 
select '344' as t,count(0) from ck_app_event_344 where insert_time > '2016-08-20' 
union all 
select '345' as t,count(0) from ck_app_event_345 where insert_time > '2016-08-20' 
union all 
select '346' as t,count(0) from ck_app_event_346 where insert_time > '2016-08-20' 
union all 
select '347' as t,count(0) from ck_app_event_347 where insert_time > '2016-08-20' 
union all 
select '348' as t,count(0) from ck_app_event_348 where insert_time > '2016-08-20' 
union all 
select '349' as t,count(0) from ck_app_event_349 where insert_time > '2016-08-20' 
union all 
select '350' as t,count(0) from ck_app_event_350 where insert_time > '2016-08-20' 
union all 
select '351' as t,count(0) from ck_app_event_351 where insert_time > '2016-08-20' 
union all 
select '352' as t,count(0) from ck_app_event_352 where insert_time > '2016-08-20' 
union all 
select '353' as t,count(0) from ck_app_event_353 where insert_time > '2016-08-20' 
union all 
select '354' as t,count(0) from ck_app_event_354 where insert_time > '2016-08-20' 
union all 
select '355' as t,count(0) from ck_app_event_355 where insert_time > '2016-08-20' 
union all 
select '356' as t,count(0) from ck_app_event_356 where insert_time > '2016-08-20' 
union all 
select '357' as t,count(0) from ck_app_event_357 where insert_time > '2016-08-20' 
union all 
select '358' as t,count(0) from ck_app_event_358 where insert_time > '2016-08-20' 
union all 
select '359' as t,count(0) from ck_app_event_359 where insert_time > '2016-08-20' 
union all 
select '360' as t,count(0) from ck_app_event_360 where insert_time > '2016-08-20' 
union all 
select '361' as t,count(0) from ck_app_event_361 where insert_time > '2016-08-20' 
union all 
select '362' as t,count(0) from ck_app_event_362 where insert_time > '2016-08-20' 
union all 
select '363' as t,count(0) from ck_app_event_363 where insert_time > '2016-08-20' 
union all 
select '364' as t,count(0) from ck_app_event_364 where insert_time > '2016-08-20' 