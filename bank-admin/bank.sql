/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE IF NOT EXISTS `answer` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '答案表的主键',
  `all_option` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '当前题目所有答案的信息',
  `analysis` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '答案解析',
  `question_id` int NOT NULL COMMENT '对应题目的id',
  `true_option` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '正确的选项对应的下标',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `question_id` (`question_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `answer`;
INSERT INTO `answer` (`id`, `all_option`, `analysis`, `question_id`, `true_option`) VALUES
	(1, '1,2', '1', 5, '0'),
	(15, '1', '1', 16, '0'),
	(16, '4,3', '4', 17, '0'),
	(100, ' java,.class,html,.exe', '', 118, '1'),
	(101, '3,3.5,4,5', '', 119, '0'),
	(102, 'public,private,static,protected', '', 120, '2'),
	(103, '正确,错误', '', 121, '1'),
	(104, '正确,错误', '', 122, '0'),
	(105, 'private,public,protected,static', NULL, 123, '0,2,3'),
	(106, 'const,NULL,false,this,native', NULL, 124, '1,2'),
	(107, 'true,false', NULL, 125, '0'),
	(108, '0', NULL, 126, '0');

CREATE TABLE IF NOT EXISTS `exam` (
  `exam_id` int NOT NULL AUTO_INCREMENT,
  `exam_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考试名称',
  `exam_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '考试描述',
  `type` int NOT NULL DEFAULT '1' COMMENT '1完全公开  2需要密码',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '需要密码考试的密码',
  `duration` int NOT NULL COMMENT '考试时长',
  `start_time` datetime DEFAULT NULL COMMENT '考试开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '考试结束时间',
  `total_score` int NOT NULL COMMENT '考试总分',
  `pass_score` int NOT NULL COMMENT '考试通过线',
  `status` int NOT NULL DEFAULT '1' COMMENT '1有效 2无效',
  PRIMARY KEY (`exam_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `exam`;
INSERT INTO `exam` (`exam_id`, `exam_name`, `exam_desc`, `type`, `password`, `duration`, `start_time`, `end_time`, `total_score`, `pass_score`, `status`) VALUES
	(30, '测试', '', 1, NULL, 10, NULL, NULL, 19, 10, 1);

CREATE TABLE IF NOT EXISTS `exam_question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_ids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考试的题目id列表',
  `exam_id` int NOT NULL COMMENT '考试的id',
  `scores` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '每一题的分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `exam_question`;
INSERT INTO `exam_question` (`id`, `question_ids`, `exam_id`, `scores`) VALUES
	(28, '118,119,120,121,122,123,124,125,126,127', 30, '1,1,1,1,1,1,1,1,1,10');

CREATE TABLE IF NOT EXISTS `exam_record` (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '考试记录的id',
  `user_id` int NOT NULL COMMENT '考试用户的id',
  `user_answers` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户的答案列表',
  `exam_id` int NOT NULL COMMENT '考试的id',
  `logic_score` int DEFAULT NULL COMMENT '考试的逻辑得分(除简答)',
  `exam_time` datetime NOT NULL COMMENT '考试时间',
  `question_ids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考试的题目信息',
  `total_score` int DEFAULT NULL COMMENT '考试总分数 (逻辑+简答)',
  `error_question_ids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '用户考试的错题',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `exam_record`;
INSERT INTO `exam_record` (`record_id`, `user_id`, `user_answers`, `exam_id`, `logic_score`, `exam_time`, `question_ids`, `total_score`, `error_question_ids`) VALUES
	(40, 22, '1-3-3-2-2-0-1---123123', 30, 1, '2024-04-14 00:22:42', '118,119,120,123,124,121,122,125,126,127', 8, '119,120,123,124,121,122,125,126');

CREATE TABLE IF NOT EXISTS `logging` (
  `id` int NOT NULL AUTO_INCREMENT,
  `operation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作描述',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求方法',
  `params` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '请求参数',
  `time` int DEFAULT NULL COMMENT '执行时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作用户',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `logging`;
INSERT INTO `logging` (`id`, `operation`, `method`, `params`, `time`, `create_user`, `create_time`) VALUES
	(1, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["20"]', 4037, 'admin', '2024-03-13 13:36:04'),
	(2, '删除课程', 'com.java.bank.controller.ProfessionController.delete()', '[230]', 17, 'admin', '2024-03-13 13:41:00'),
	(3, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["21"]', 18, 'admin', '2024-03-13 13:41:19'),
	(4, '删除题库', 'com.java.bank.controller.TeacherController.deleteQuestionBank()', '["1,2"]', 26, 'admin', '2024-03-13 13:41:31'),
	(5, '更改考试状态', 'com.java.bank.controller.TeacherController.operationExam()', '[3,"23"]', 20, 'root', '2024-03-13 13:49:42'),
	(6, '更改用户状态', 'com.java.bank.controller.AdminController.handleUser()', '[1,"22"]', 3, 'root', '2024-03-13 13:50:34'),
	(7, '更改用户状态', 'com.java.bank.controller.AdminController.handleUser()', '[2,"22"]', 16, 'root', '2024-03-13 13:50:37'),
	(8, '更改用户状态', 'com.java.bank.controller.AdminController.handleUser()', '[1,"22"]', 2, 'root', '2024-03-13 13:50:56'),
	(9, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["52,53,54,55,56,57,58,59"]', 65, 'admin', '2024-03-13 15:39:24'),
	(10, '更改考试状态', 'com.java.bank.controller.TeacherController.operationExam()', '[3,"22"]', 33, 'admin', '2024-03-13 15:40:56'),
	(11, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["60,61,62,63,64,65,66,67"]', 49, 'admin', '2024-03-13 15:41:03'),
	(12, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["68"]', 15, 'admin', '2024-03-13 15:42:33'),
	(13, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["69"]', 17, 'admin', '2024-03-13 15:51:44'),
	(14, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["70"]', 17, 'admin', '2024-03-13 15:54:00'),
	(15, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["71"]', 14, 'admin', '2024-03-13 15:54:55'),
	(16, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["72,73,74,75"]', 32, 'admin', '2024-03-13 15:56:19'),
	(17, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["88,89,90,91,92,93,94,95"]', 66, 'teacher', '2024-03-13 17:42:33'),
	(18, '删除课程', 'com.java.bank.controller.ProfessionController.delete()', '[231]', 38, 'admin', '2024-03-13 17:43:37'),
	(19, '更改考试状态', 'com.java.bank.controller.TeacherController.operationExam()', '[3,"26,25,24"]', 34, 'teacher', '2024-03-13 17:44:00'),
	(20, '更改用户状态', 'com.java.bank.controller.AdminController.handleUser()', '[2,"18"]', 23, 'admin', '2024-03-13 17:44:20'),
	(21, '更改用户状态', 'com.java.bank.controller.AdminController.handleUser()', '[1,"18"]', 1, 'admin', '2024-03-13 17:44:41'),
	(22, '更改用户状态', 'com.java.bank.controller.AdminController.handleUser()', '[2,"23,17"]', 45, 'admin', '2024-04-01 13:03:03'),
	(23, '更改用户状态', 'com.java.bank.controller.AdminController.handleUser()', '[1,"23,17"]', 47, 'admin', '2024-04-01 13:03:21'),
	(24, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["83"]', 24, 'admin', '2024-04-14 00:15:26'),
	(25, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["76,77,78,79,80,81,82,84,85,86"]', 82, 'admin', '2024-04-14 00:15:33'),
	(26, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["87,96,97,98,99,100,101,102,103,104"]', 88, 'admin', '2024-04-14 00:15:36'),
	(27, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["105,106,107"]', 33, 'admin', '2024-04-14 00:15:38'),
	(28, '更改考试状态', 'com.java.bank.controller.TeacherController.operationExam()', '[3,"28,27"]', 38, 'admin', '2024-04-14 00:15:50'),
	(29, '删除试题', 'com.java.bank.controller.TeacherController.deleteQuestion()', '["108,109,110,111,112,113,114,115,116,117"]', 87, 'admin', '2024-04-14 00:21:20'),
	(30, '更改考试状态', 'com.java.bank.controller.TeacherController.operationExam()', '[3,"29"]', 13, 'admin', '2024-04-14 00:21:46');

CREATE TABLE IF NOT EXISTS `notice` (
  `n_id` int NOT NULL AUTO_INCREMENT COMMENT '系统公告id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `create_time` datetime DEFAULT NULL COMMENT '公告创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新此公告时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0(不是当前系统公告) 1(是当前系统公告)',
  PRIMARY KEY (`n_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `notice`;
INSERT INTO `notice` (`n_id`, `content`, `create_time`, `update_time`, `status`) VALUES
	(8, '<p><b>欢迎使用题库管理系统！<span style="font-size: 14px; text-align: initial;">👏</span></b></p>', '2024-03-12 14:14:55', '2024-04-14 00:23:54', 1);

CREATE TABLE IF NOT EXISTS `profession` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` int DEFAULT NULL COMMENT '上级id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '专业课程名',
  `weight` int DEFAULT NULL COMMENT '排序',
  `create_user` int DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=231 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `profession`;
INSERT INTO `profession` (`id`, `pid`, `title`, `weight`, `create_user`, `create_time`) VALUES
	(203, 0, '软件工程', 2, NULL, NULL),
	(204, 0, '机械工程', 3, NULL, NULL),
	(205, 204, '机械制图', 1, NULL, NULL),
	(206, 204, '机械原理', 1, NULL, NULL),
	(207, 204, '机械设计', 1, NULL, NULL),
	(208, 204, '电工与电子技术', 1, NULL, NULL),
	(209, 0, '测控技术与仪器专业', 4, NULL, NULL),
	(210, 209, '电工学', 1, NULL, NULL),
	(211, 209, '电子技术基础', 1, NULL, NULL),
	(212, 209, '传感器原理及应用', 1, NULL, NULL),
	(213, 0, '材料科学与工程专业', 5, NULL, NULL),
	(214, 213, '大学物理化学', 1, NULL, NULL),
	(215, 213, '材料物理化学', 1, NULL, NULL),
	(216, 213, '量子与统计力学', 1, NULL, NULL),
	(217, 0, '公共必修课程', 1, NULL, NULL),
	(218, 217, '马克思主义哲学原理', 1, NULL, NULL),
	(219, 217, '毛泽东思想概论', 1, NULL, NULL),
	(220, 217, '大学英语', 1, NULL, NULL),
	(221, 203, 'c++程序设计基础', 1, NULL, NULL),
	(222, 203, '数据结构', 1, NULL, NULL),
	(223, 203, 'Java程序设计', 1, NULL, NULL),
	(224, 203, '计算机组成原理', 1, NULL, NULL),
	(225, 0, '理论与应用力学', 11, NULL, NULL),
	(226, 0, '	工程力学', 7, NULL, NULL),
	(227, 0, '机械设计制造及其自动化', 16, NULL, NULL),
	(228, 0, '法学', 15, NULL, NULL),
	(229, 0, '经济统计学', 18, NULL, NULL);

CREATE TABLE IF NOT EXISTS `question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qu_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `qu_type` int NOT NULL COMMENT '问题类型 1单选 2多选 3判断 4简答',
  `level` int NOT NULL DEFAULT '1' COMMENT '题目难度 1简单 2中等 3困难',
  `image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '图片',
  `qu_bank_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属题库id',
  `qu_bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属题库名称',
  `analysis` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '解析',
  `profession_id` int DEFAULT NULL COMMENT '所属课程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='试题';

DELETE FROM `question`;
INSERT INTO `question` (`id`, `qu_content`, `create_time`, `create_person`, `qu_type`, `level`, `image`, `qu_bank_id`, `qu_bank_name`, `analysis`, `profession_id`) VALUES
	(118, '编译Java Application 源程序文件将产生相应的字节码文件，这些字节码文件的扩展名为(  )。', '2024-04-14 00:21:38', 'admin', 1, 1, NULL, '5', 'java开发', '', 223),
	(119, '设 x = 1 , y = 2 , z = 3，则表达式 y＋＝z－－/＋＋x 的值是(  )', '2024-04-14 00:21:38', 'admin', 1, 2, NULL, '5', 'java开发', '', 223),
	(120, '不允许作为类及类成员的访问控制符的是( )', '2024-04-14 00:21:38', 'admin', 1, 2, NULL, '5', 'java开发', '', 223),
	(121, 'java语言中不用区分字母的大写小写', '2024-04-14 00:21:38', 'admin', 3, 1, NULL, '5', 'java开发', '', 223),
	(122, 'Java的字符类型采用的是Unicode编码，每个Unicode码占16个比特。', '2024-04-14 00:21:38', 'admin', 3, 3, NULL, '5', 'java开发', '', 223),
	(123, '不能用来修饰interface的有（）', '2024-04-14 00:21:38', 'admin', 2, 2, NULL, '5', 'java开发', '', 223),
	(124, '如下哪些不是java的关键字？（ ）', '2024-04-14 00:21:38', 'admin', 2, 1, NULL, '5', 'java开发', '', 223),
	(125, '若x = 5，y = 10，则x < y和x >= y的逻辑值分别为___和___。', '2024-04-14 00:21:38', 'admin', 4, 2, NULL, '5', 'java开发', '', 223),
	(126, '设 x = 2 ，则表达式 ( x + + )／3 的值是_______。', '2024-04-14 00:21:38', 'admin', 4, 3, NULL, '5', 'java开发', '', 223),
	(127, '用Java Application编写一个程序，输出 Hello! I love JAVA.', '2024-04-14 00:21:38', 'admin', 5, 3, NULL, '5', 'java开发', '', 223);

CREATE TABLE IF NOT EXISTS `question_bank` (
  `bank_id` int NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`bank_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `question_bank`;
INSERT INTO `question_bank` (`bank_id`, `bank_name`) VALUES
	(5, 'java开发');

CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL DEFAULT '1' COMMENT '1(学生) 2(教师) 3(管理员)',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
  `true_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `status` int NOT NULL DEFAULT '1' COMMENT '用户是否被禁用 1正常 2禁用',
  `create_date` datetime NOT NULL COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `user`;
INSERT INTO `user` (`id`, `role_id`, `username`, `true_name`, `password`, `email`, `status`, `create_date`) VALUES
	(1, 3, 'root', '超级管理员', '$2a$10$q6A16F5ht/l3l/JuN.23Ce5BeEBwTB9Xt25PYxoJCCWImPSkZWPYm', '1728239274@qq.com', 1, '2024-03-10 14:15:27'),
	(17, 1, 'student', '张三', '$2a$10$tv00xPpElrmwXhV6pIMGkO5/ivpRiahDgfPDqDoVq2Ae7atU4pfZO', '123123123@qq.com', 1, '2024-03-11 16:57:37'),
	(18, 2, 'teacher', '熊大', '$2a$10$d2wahvlxWUK5vHfBukyeU.EUKhDOMh.VRRprd2Z36LWfghZobT8Oa', '1728239274@qq.com', 1, '2024-03-11 16:58:35'),
	(22, 3, 'admin', 'admin', '$2a$10$tv00xPpElrmwXhV6pIMGkO5/ivpRiahDgfPDqDoVq2Ae7atU4pfZO', '1728239274@qq.com', 1, '2024-03-13 13:44:35'),
	(23, 1, 'zs', 'zs', '$2a$10$9F67ope0SXcmPsu4pEDt5eDcIwqjC0iUUMPAro.HOLWOhPLFyw4IS', NULL, 1, '2024-03-29 12:33:43'),
	(24, 1, 'student2', '张三', '$2a$10$vimuRGNLE2dlnOV1nHnuhexFxsLpEBDRUrnccOjj/mzt.GdDDKrJG', NULL, 1, '2024-04-14 00:25:30');

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` int NOT NULL,
  `role_id` int NOT NULL DEFAULT '1' COMMENT '1学生 2教师 3超级管理员',
  `role_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `menu_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主页的菜单信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

DELETE FROM `user_role`;
INSERT INTO `user_role` (`id`, `role_id`, `role_name`, `menu_info`) VALUES
	(1, 1, '学生', '[{"topMenuName":"系统介绍","topIcon":"el-icon-odometer","url":"/dashboard"},{"topMenuName":"在线考试","topIcon":"el-icon-menu","submenu":[{"name":"在线考试","icon":"el-icon-s-promotion","url":"/examOnline"},{"name":"我的成绩","icon":"el-icon-trophy","url":"/myGrade"},{"name":"我的题库","icon":"el-icon-notebook-2","url":"/myQuestionBank"}]}]'),
	(2, 2, '教师', '[{"topMenuName":"系统介绍","topIcon":"el-icon-odometer","url":"/dashboard"},{"topMenuName":"考试管理","topIcon":"el-icon-bangzhu","submenu":[{"name":"题库管理","icon":"el-icon-aim","url":"/questionBankMange"},{"name":"试题管理","icon":"el-icon-news","url":"/questionManage"},{"name":"考试管理","icon":"el-icon-tickets","url":"/examManage"},{"name":"阅卷管理","icon":"el-icon-view","url":"/markManage"},{"name":"试题导入","icon":"el-icon-upload2","url":"/questionImport"},{"name":"统计总览","icon":"el-icon-data-line","url":"/staticOverview"}]}]'),
	(3, 3, '管理员', '[{"topMenuName":"系统介绍","topIcon":"el-icon-odometer","url":"/dashboard"},{"topMenuName":"在线考试","topIcon":"el-icon-menu","submenu":[{"name":"在线考试","icon":"el-icon-s-promotion","url":"/examOnline"},{"name":"我的成绩","icon":"el-icon-trophy","url":"/myGrade"},{"name":"我的题库","icon":"el-icon-notebook-2","url":"/myQuestionBank"}]},{"topMenuName":"考试管理","topIcon":"el-icon-bangzhu","submenu":[{"name":"题库管理","icon":"el-icon-aim","url":"/questionBankMange"},{"name":"试题管理","icon":"el-icon-news","url":"/questionManage"},{"name":"考试管理","icon":"el-icon-tickets","url":"/examManage"},{"name":"阅卷管理","icon":"el-icon-view","url":"/markManage"},{"name":"试题导入","icon":"el-icon-upload2","url":"/questionImport"},{"name":"统计总览","icon":"el-icon-data-line","url":"/staticOverview"}]},{"topMenuName":"教学管理","topIcon":"el-icon-pie-chart","submenu":[{"name":"课程管理","icon":"el-icon-data-line","url":"/profession"}]},{"topMenuName":"系统设置","topIcon":"el-icon-setting","submenu":[{"name":"公告管理","icon":"el-icon-bell","url":"/noticeManage"},{"name":"角色管理","icon":"el-icon-s-custom","url":"/roleManage"},{"name":"用户管理","icon":"el-icon-user-solid","url":"/userManage"},{"name":"系统日志","icon":"el-icon-s-order","url":"/loggingManage"}]}]');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
