-- ----------------------------
-- 地块表 agriculture_land
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_land`;
CREATE TABLE `agriculture_land` (
  `land_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '地块ID' PRIMARY KEY,
  `land_name` varchar(50) NOT NULL DEFAULT '' COMMENT '地块名称',
  `land_type` char(1) NOT NULL DEFAULT 0 COMMENT '字典 agriculture_land_type',
  `land_area` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '地块面积',
  `stroke_weight` tinyint NOT NULL DEFAULT 0 COMMENT '边框宽度',
  `stroke_color` varchar(50) NOT NULL DEFAULT '' COMMENT '边框颜色',
  `stroke_opacity` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '边框透明度',
  `land_path` varchar(1000)  NOT NULL DEFAULT '' COMMENT '地块路径',
  `fill_color` varchar(50) NOT NULL DEFAULT '' COMMENT '地块背景颜色',
  `fill_opacity` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '地块透明度',
  `current_batch` bigint NOT NULL DEFAULT 0 COMMENT '当前种植批次',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='地块表';

-- ----------------------------
-- 批次表 agriculture_crop_batch
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_crop_batch`;
CREATE TABLE `agriculture_crop_batch` (
  `batch_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '批次ID' PRIMARY KEY,
  `batch_name` varchar(50) NOT NULL COMMENT '批次名称',
  `germplasm_id` bigint NOT NULL COMMENT '种质ID',
  `land_id` bigint NOT NULL DEFAULT 0 COMMENT '地块ID',
  `crop_area` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '种植面积（亩）',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `batch_head` bigint NOT NULL DEFAULT 0 COMMENT '负责人Id',
  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',

  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='作物批次表';

-- ----------------------------
-- 种质表 agriculture_germplasm
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_germplasm`;
CREATE TABLE `agriculture_germplasm` (
  `germplasm_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '种质ID' PRIMARY KEY,
  `crop_name` varchar(50) NOT NULL DEFAULT '' COMMENT '作物名称',
  `crop_en_name` varchar(50) NOT NULL DEFAULT '' COMMENT '作物英文名称',
  `germplasm_name` varchar(50) NOT NULL DEFAULT '' COMMENT '种质名称',
  `germplasm_en_name` varchar(50) NOT NULL DEFAULT '' COMMENT '种质英文名称',
  `germplasm_img` varchar(100) NOT NULL DEFAULT '' COMMENT '种质图片',
  `germplasm_des` varchar(500) NOT NULL DEFAULT '' COMMENT '宣传语',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='种质表';

-- ----------------------------
-- 种植方法表 agriculture_plant_method
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_plant_method`;
create TABLE `agriculture_plant_method`(
  `method_id` bigint UNSIGNED AUTO_INCREMENT COMMENT '方法ID' PRIMARY KEY,
  `germplasm_id` bigint NOT NULL COMMENT '种质ID',
  `method_name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  `method_img` varchar(100) NOT NULL DEFAULT '' COMMENT '图片',
  `method_des` varchar(1000) NOT NULL DEFAULT '' COMMENT '描述',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='种植方法表';

-- ----------------------------
-- 种质介绍表 agriculture_intro
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_germplasm_intro`;
create TABLE `agriculture_germplasm_intro`(
  `intro_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '介绍ID' PRIMARY KEY,
  `germplasm_id` bigint NOT NULL COMMENT '种质ID',
  `intro_name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  `intro_img` varchar(100) NOT NULL DEFAULT '' COMMENT '图片',
  `intro_des` varchar(1000) NOT NULL DEFAULT '' COMMENT '描述',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='种质介绍表';

-- ----------------------------
-- 雇员表 agriculture_employee
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_employee`;
create TABLE `agriculture_employee`(
  `employee_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '雇员ID' PRIMARY KEY,
  `employee_code` varchar(20) NOT NULL DEFAULT '' COMMENT '编码',
  `employee_name` varchar(10) NOT NULL DEFAULT '' COMMENT '姓名',
  `employee_type` char(1) NOT NULL DEFAULT '0' COMMENT '字典 agriculture_employee_type',
  `employee_tel` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `employee_sex` char(1) NOT NULL DEFAULT '0' COMMENT '字典 sys_user_sex',
  `employee_address` varchar(200) NOT NULL DEFAULT '' COMMENT '地址', 

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='雇员表';

-- ----------------------------
-- 基地信息表 agriculture_baseinfo
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_baseinfo`;
create TABLE `agriculture_baseinfo`(
  `base_id` bigint UNSIGNED AUTO_INCREMENT COMMENT '基地ID' PRIMARY KEY,
  `base_short_name` varchar(20) NOT NULL DEFAULT '' COMMENT '基地简称',
  `base_code` varchar(20) NOT NULL DEFAULT '' COMMENT '基地编号',
  `base_name` varchar(30) NOT NULL DEFAULT '' COMMENT '基地全称',
  `base_leader` varchar(10) NOT NULL DEFAULT '' COMMENT '基地负责人',
  `leader_tel` varchar(17) NOT NULL DEFAULT '' COMMENT '基地负责人电话',
  `base_address` varchar(500) NOT NULL DEFAULT '' COMMENT '基地负责人地址',
  `base_area` varchar(30) NOT NULL DEFAULT '' COMMENT '基地面积',
  `base_altitude` int NOT NULL DEFAULT 0 COMMENT '基地海拔',
  `base_img` varchar(500) NOT NULL DEFAULT '' COMMENT '现场图片',
  `base_des` varchar(1000) NOT NULL DEFAULT '' COMMENT '基地描述',
   
  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='基地信息表';


-- ----------------------------
-- 标准作业任务表 agriculture_standard_job
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_standard_job`;
CREATE TABLE `agriculture_standard_job` (
  `job_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '作业任务ID' PRIMARY KEY,
  `germplasm_id` bigint NOT NULL DEFAULT 0 COMMENT '种质ID',
  `job_name` varchar(50) NOT NULL DEFAULT '' COMMENT '作业任务名称',
  `cycle_unit` char(1) NOT NULL DEFAULT '0' COMMENT '作业周期单位（0代表周 1代表天）',
  `job_start` int NOT NULL DEFAULT '0' COMMENT '起始周/天',
  `job_finish` int NOT NULL DEFAULT '0' COMMENT '结束周/天',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='标准作业任务表';

-- ----------------------------
-- 批次任务表 agriculture_batch_task
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_batch_task`;
CREATE TABLE `agriculture_batch_task` (
  `task_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '任务ID' PRIMARY KEY,
  `batch_id` bigint UNSIGNED  COMMENT '批次ID',
  `task_head` bigint UNSIGNED COMMENT '任务负责人',
  `task_name` varchar(50) NOT NULL DEFAULT '' COMMENT '任务名称',
  `plan_start` datetime DEFAULT NULL COMMENT '计划开始日期',
  `plan_finish` datetime DEFAULT NULL COMMENT '计划结束日期',
  `actual_start` datetime DEFAULT NULL COMMENT '实际开始日期',
  `actual_finish` datetime DEFAULT NULL COMMENT '实际结束日期',
  `task_detail` varchar(2000) NOT NULL DEFAULT '' COMMENT '任务详情',
  `task_images` text COMMENT '图片资料',
  `task_videos` text COMMENT '视频资料',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='批次任务表';

-- ----------------------------
-- 机械类别表 agriculture_machine_type
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_machine_type`;
CREATE TABLE `agriculture_machine_type` (
  `machine_type_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '机械类别ID' PRIMARY KEY,
  `machine_type_name` varchar(50) NOT NULL DEFAULT '' COMMENT '机械类别名称',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='机械类别表';

-- ----------------------------
-- 机械信息表 agriculture_machine_info
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_machine_info`;
CREATE TABLE `agriculture_machine_info` (
  `machine_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '机械ID' PRIMARY KEY,
  `machine_code` varchar(20) NOT NULL DEFAULT '' COMMENT '机械编码',
  `machine_name` varchar(50) NOT NULL DEFAULT '' COMMENT '机械名称',
  `machine_type_id` bigint NOT NULL DEFAULT 0 COMMENT '机械类别',
  `measure_unit` varchar(10) NOT NULL DEFAULT '' COMMENT '计量单位',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='机械信息表';

-- ----------------------------
-- 农资类别表 agriculture_material_type
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_material_type`;
CREATE TABLE `agriculture_material_type` (
  `material_type_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '农资类别ID' PRIMARY KEY,
  `material_type_name` varchar(50) NOT NULL DEFAULT '' COMMENT '农资类别名称',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='农资类别表';

-- ----------------------------
-- 农资信息表 agriculture_material_info
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_material_info`;
CREATE TABLE `agriculture_material_info` (
  `material_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '农资ID' PRIMARY KEY,
  `material_code` varchar(20) NOT NULL DEFAULT '' COMMENT '农资编码',
  `material_name` varchar(50) NOT NULL DEFAULT '' COMMENT '农资名称',
  `material_type_id` bigint NOT NULL DEFAULT 0 COMMENT '农资类别',
  `measure_unit` varchar(10) NOT NULL DEFAULT '' COMMENT '计量单位',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='农资信息表';

-- ----------------------------
-- 批次任务工人表 agriculture_task_employee
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_task_employee`;
CREATE TABLE `agriculture_task_employee` (
  `id` bigint UNSIGNED AUTO_INCREMENT  COMMENT 'ID' PRIMARY KEY,
  `task_id` bigint NOT NULL DEFAULT 0 COMMENT '任务ID',
  `employee_id` bigint NOT NULL DEFAULT 0 COMMENT '雇员ID',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='批次任务工人表';

-- ----------------------------
-- 人工工时表 agriculture_cost_employee
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_cost_employee`;
CREATE TABLE `agriculture_cost_employee` (
  `cost_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT 'ID' PRIMARY KEY,
  `task_id` bigint NOT NULL DEFAULT 0 COMMENT '任务ID',
  `employee_id` bigint NOT NULL DEFAULT 0 COMMENT '雇员ID',
  `working_hours` decimal(8,2) NOT NULL DEFAULT 0 COMMENT '工时',
  `working_start` datetime DEFAULT NULL COMMENT '开始日期',
  `working_finish` datetime DEFAULT NULL COMMENT '结束日期',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='人工工时表';

-- ----------------------------
-- 机械工时表 agriculture_cost_machine
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_cost_machine`;
CREATE TABLE `agriculture_cost_machine` (
  `cost_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT 'ID' PRIMARY KEY,
  `task_id` bigint NOT NULL DEFAULT 0 COMMENT '任务ID',
  `machine_id` bigint NOT NULL DEFAULT 0 COMMENT '机械ID',
  `machine_count` int NOT NULL DEFAULT 0 COMMENT '机械数量',
  `working_hours` decimal(8,2) NOT NULL DEFAULT 0 COMMENT '工时',
  `working_start` datetime DEFAULT NULL COMMENT '开始日期',
  `working_finish` datetime DEFAULT NULL COMMENT '结束日期',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='机械工时表';

-- ----------------------------
-- 农资用量表 agriculture_cost_material
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_cost_material`;
CREATE TABLE `agriculture_cost_material` (
  `cost_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT 'ID' PRIMARY KEY,
  `task_id` bigint NOT NULL DEFAULT 0 COMMENT '任务ID',
  `material_id` bigint NOT NULL DEFAULT 0 COMMENT '农资ID',
  `material_count` int NOT NULL DEFAULT 0 COMMENT '使用数量',
  `measure_unit` varchar(10) NOT NULL DEFAULT '' COMMENT '计量单位',
  `working_start` datetime DEFAULT NULL COMMENT '开始日期',
  `working_finish` datetime DEFAULT NULL COMMENT '结束日期',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='农资用量表';

-- ----------------------------
-- 批次任务日志表 agriculture_task_log
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_task_log`;
CREATE TABLE `agriculture_task_log` (
  `log_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '日志ID' PRIMARY KEY,
  `task_id` bigint NOT NULL DEFAULT 0 COMMENT '任务ID',
  `oper_name` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人名称',
  `oper_id` bigint NOT NULL DEFAULT 0 COMMENT '操作人Id',
  `oper_des` varchar(500) NOT NULL DEFAULT '' COMMENT '操作描述',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='批次任务日志表';

-- ----------------------------
-- 店铺表 agriculture_trace_shop
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_trace_shop`;
CREATE TABLE `agriculture_trace_shop` (
  `shop_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '店铺ID' PRIMARY KEY,
  `shop_name` varchar(50) NOT NULL DEFAULT '' COMMENT '店铺名称',
  `shop_type` varchar(20) NOT NULL DEFAULT '0' COMMENT '店铺类型 1线上 2线下',
  `shop_img` varchar(250) NOT NULL DEFAULT '' COMMENT '店铺图片',
  `shop_address` varchar(50) NOT NULL DEFAULT '' COMMENT '店铺地址',
  `shop_coordinate` varchar(50) NOT NULL DEFAULT '' COMMENT '店铺坐标',
  `shop_contacts` varchar(20) NOT NULL DEFAULT '' COMMENT '店铺联系人',
  `shop_tel` varchar(20) NOT NULL DEFAULT '' COMMENT '店铺联系方式',
  `shop_url` varchar(50) NOT NULL DEFAULT '' COMMENT '店铺链接',
  `shop_des` varchar(100) NOT NULL DEFAULT '' COMMENT '店铺描述',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '2' COMMENT '状态 1可用 2不可用 trace_status',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='店铺表';

-- ----------------------------
-- 溯源人员表 agriculture_trace_staff
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_trace_staff`;
CREATE TABLE `agriculture_trace_staff` (
  `staff_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '员工ID' PRIMARY KEY,
  `staff_position` varchar(50) NOT NULL DEFAULT '' COMMENT '员工职位',
  `staff_name` varchar(50) NOT NULL DEFAULT '' COMMENT '员工名称',
  `staff_tel` varchar(20) NOT NULL DEFAULT '' COMMENT '员工联系方式',
  `staff_des` varchar(100) NOT NULL DEFAULT '' COMMENT '员工描述',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '2' COMMENT '状态 1可用 2不可用 trace_status',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='溯源人员表';

-- ----------------------------
-- 溯源产品表 agriculture_trace_sellpro
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_trace_sellpro`;
CREATE TABLE `agriculture_trace_sellpro` (
  `sellpro_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '产品ID' PRIMARY KEY,
  `sellpro_name` varchar(20) NOT NULL DEFAULT '' COMMENT '产品名称',
  `sellpro_area` varchar(20) NOT NULL DEFAULT '' COMMENT '产地',
  `sellpro_weight` varchar(20) NOT NULL DEFAULT '' COMMENT '产品重量',
  `sellpro_guige` varchar(20) NOT NULL DEFAULT '' COMMENT '产品规格',
  `sellpro_img` varchar(500) NOT NULL DEFAULT '' COMMENT '产品图片',

  `germplasm_id` varchar(100) NOT NULL DEFAULT ''  COMMENT '种质ID',
  `batch_id` varchar(100) NOT NULL DEFAULT ''  COMMENT '批次ID',
  `land_id` varchar(100) NOT NULL DEFAULT '' COMMENT '地块ID',
  `device_id` varchar(100) NOT NULL DEFAULT ''  COMMENT '环境设备ID',
  `camera_id` varchar(100) NOT NULL DEFAULT ''  COMMENT '监控设备ID',
  `staff_id` varchar(100) NOT NULL DEFAULT ''  COMMENT '溯源人员ID',
  `shop_id` varchar(100) NOT NULL DEFAULT ''  COMMENT '店铺ID',
  `h5_url`  varchar(500) NOT NULL DEFAULT '' COMMENT '溯源页面',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '2' COMMENT '状态 1可用 2不可用 trace_status',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='溯源产品表';

-- ----------------------------
-- 溯源版本表 agriculture_trace_version
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_trace_version`;
CREATE TABLE `agriculture_trace_version` (
  `version_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '溯源版本ID' PRIMARY KEY,
  `sellpro_id` bigint UNSIGNED NOT NULL DEFAULT 0  COMMENT '产品ID',
  `version_code` varchar(30) NOT NULL DEFAULT '' COMMENT '溯源版本编号',
  `expire_date` datetime DEFAULT NULL COMMENT '过期时间',
  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '2' COMMENT '状态 1可用 2不可用 trace_status',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='溯源版本表';

-- ----------------------------
-- 溯源码表 agriculture_trace_code
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_trace_code`;
CREATE TABLE `agriculture_trace_code` (
  `code_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '溯源码ID' PRIMARY KEY,
  `sellpro_id` bigint UNSIGNED NOT NULL DEFAULT 0  COMMENT '产品ID' ,
  `version_id` bigint UNSIGNED NOT NULL DEFAULT 0  COMMENT '溯源版本ID' ,
  `trace_code` varchar(500) NOT NULL DEFAULT ''  COMMENT '溯源码' ,
  `trace_url` varchar(500) NOT NULL DEFAULT ''  COMMENT '溯源链接' ,

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '2' COMMENT '状态 1可用 2不可用 trace_status',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='溯源码表';
-- ----------------------------
-- 溯源码查询记录表 agriculture_trace_record
-- ----------------------------
DROP TABLE IF EXISTS `agriculture_trace_record`;
CREATE TABLE `agriculture_trace_record` (
  `record_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '记录ID' PRIMARY KEY,
  `sellpro_id` bigint UNSIGNED NOT NULL DEFAULT 0  COMMENT '产品ID' ,
  `trace_code` varchar(500)  NOT NULL DEFAULT ''  COMMENT '溯源码' ,
  `query_date` datetime DEFAULT NULL  COMMENT '查询时间' ,
  `query_address` varchar(50) NOT NULL DEFAULT '' COMMENT '查询地点',
  `query_coordinate` varchar(50) NOT NULL DEFAULT '' COMMENT '查询坐标',
  `query_city` varchar(50) NOT NULL DEFAULT '' COMMENT '查询城市',
  `city_code` varchar(50) NOT NULL DEFAULT '' COMMENT '城市代码',

  `remark` varchar(2000) NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) NOT NULL DEFAULT '2' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` bigint NOT NULL DEFAULT 0 COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT 0 COMMENT '修改人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='溯源码查询记录表';

-- ----------------------------
-- 产品布局表 iot_product_layout
-- ----------------------------
DROP TABLE IF EXISTS `iot_product_layout`;
CREATE TABLE `iot_product_layout` (
  `layout_id` bigint UNSIGNED AUTO_INCREMENT  COMMENT '记录ID' PRIMARY KEY,
  `user_id` bigint UNSIGNED NOT NULL DEFAULT 0   COMMENT '用户ID' ,
  `product_id` bigint UNSIGNED NOT NULL DEFAULT 0   COMMENT '产品ID' ,
  `identifier` varchar(50) NOT NULL DEFAULT ''  COMMENT '标志符' ,
  `x` varchar(50) NOT NULL DEFAULT '' COMMENT 'x位置',
  `y` varchar(50) NOT NULL DEFAULT '' COMMENT 'y位置',
  `w` varchar(50) NOT NULL DEFAULT '' COMMENT '宽度',
  `h` varchar(50) NOT NULL DEFAULT '' COMMENT '高度'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='产品布局表';

-- ----------------------------
-- Table structure for ext_upgrade
-- ----------------------------
DROP TABLE IF EXISTS `ext_upgrade`;
CREATE TABLE `ext_upgrade` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `update_type` char(2) DEFAULT '0' COMMENT '升级类型',
  `is_force_update` char(1) DEFAULT '0' COMMENT '是否强制升级',
  `android_url` varchar(150) DEFAULT '' COMMENT 'apk升级地址',
  `ios_url` varchar(150) DEFAULT '' COMMENT 'ios升级地址',
  `con` varchar(100) DEFAULT '0' COMMENT '升级内容描述',
  `version` varchar(50) DEFAULT '' COMMENT '版本号',
  `wgt_version` varchar(50) DEFAULT '' COMMENT 'wgt版本号',
  `version_name` varchar(50) DEFAULT '' COMMENT '版本名称',
  `version_code` varchar(50) DEFAULT '' COMMENT '应用版本号',
  `test_user` varchar(200) DEFAULT '' COMMENT '测试用户',
  `is_current` char(1) DEFAULT '0' COMMENT '是否启用 0不启用 1启用',
   `remark` varchar(2000) DEFAULT '' COMMENT '备注',
    `status` char(1) DEFAULT '0' COMMENT '状态 ',
    `order_num` int(10) DEFAULT '0' COMMENT '排序',
    `create_by` varchar(128) DEFAULT '' COMMENT '创建者ID',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(128) DEFAULT '' COMMENT '修改人ID',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    PRIMARY KEY (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='App升级表';
-- ----------------------------
-- 设备定时任务日志表 iot_device_job_log
-- ----------------------------
DROP TABLE IF EXISTS `iot_device_job_log`;
CREATE TABLE `iot_device_job_log` (
    `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
    `job_name` varchar(64) NOT NULL COMMENT '任务名称',
    `job_id`   bigint(20) NOT NULL COMMENT '任务ID',
    `job_group` varchar(64) NOT NULL COMMENT '任务组名',
    `device_name` varchar(500) NOT NULL COMMENT '设备名称',
    `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
    `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
    `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设备定时任务日志表';