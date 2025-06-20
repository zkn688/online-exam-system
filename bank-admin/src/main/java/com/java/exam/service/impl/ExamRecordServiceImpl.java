package com.java.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.entity.*;
import com.java.exam.exception.BaseException;
import com.java.exam.exception.ErrorCode;
import com.java.exam.mapper.*;
import com.java.exam.security.LoginUser;
import com.java.exam.service.ExamRecordService;
import com.java.exam.utils.PageResponse;
import com.java.exam.utils.SecurityUtils;
import com.java.exam.utils.StringUtils;
import com.java.exam.vo.RecordVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.java.exam.utils.QueryWrapperUtils.setEqualsQueryWrapper;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamRecordService {

    private final UserMapper userMapper;

    private final ExamMapper examMapper;

    private final ExamRecordMapper examRecordMapper;

    private final ExamQuestionMapper examQuestionMapper;

    private final AnswerMapper answerMapper;

    @Override
    public PageResponse<ExamRecord> getUserGrade(String username, Integer examId, Integer pageNo, Integer pageSize) {
        User user = Optional.ofNullable(userMapper.selectOne(new QueryWrapper<User>().eq("username", username))).orElseThrow(() -> new BaseException(ErrorCode.E_100102));

        QueryWrapper<ExamRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId());
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("exam_id", examId));

        IPage<ExamRecord> page = examRecordMapper.selectPage(new Page<>(pageNo, pageSize), wrapper);

        return PageResponse.<ExamRecord>builder().data(page.getRecords()).total(page.getTotal()).build();
    }


    @Override
    public ExamRecord getExamRecordById(Integer recordId) {
        return examRecordMapper.selectById(recordId);
    }


    @Override
    public Integer addExamRecord(ExamRecord examRecord, HttpServletRequest request) {
        // 当前用户对象的信息
        LoginUser tokenVo = SecurityUtils.getLoginUser();
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", tokenVo.getUsername()));
        // 设置考试信息的字段
        examRecord.setUserId(user.getId());

        // 设置逻辑题目的分数
        // 查询所有的题目答案信息
        List<Answer> answers = answerMapper.selectList(new QueryWrapper<Answer>().in("question_id", Arrays.asList(examRecord.getQuestionIds().split(","))));
        // 查询考试的题目的分数
        HashMap<String, String> map = new HashMap<>();// key是题目的id  value是题目分值
        ExamQuestion examQuestion = examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examRecord.getExamId()));
        // 题目的id
        String[] ids = examQuestion.getQuestionIds().split(",");
        // 题目在考试中对应的分数
        String[] scores = examQuestion.getScores().split(",");
        for (int i = 0; i < ids.length; i++) {
            map.put(ids[i], scores[i]);
        }
        // 逻辑分数
        int logicScore = 0;
        // 是否存在简答题
        boolean haveShort = false;
        // 错题的id
        StringJoiner sf = new StringJoiner(",");
        // 用户的答案
        String[] userAnswers = examRecord.getUserAnswers().split("-");
        for (int i = 0; i < examRecord.getQuestionIds().split(",").length; i++) {
            int index = getIndex(answers, Integer.parseInt(examRecord.getQuestionIds().split(",")[i]));
            if (index != -1) {
                String aw = answers.get(index).getAllOption().replaceAll(",", ";");
                if (Objects.equals(userAnswers[i], aw) ||
                        Objects.equals(userAnswers[i], answers.get(index).getTrueOption())) {
                    logicScore += Integer.parseInt(map.get(examRecord.getQuestionIds().split(",")[i]));
                } else {
                    sf.add(examRecord.getQuestionIds().split(",")[i]);
                }
            } else {
                //存在简答题
                haveShort = true;
            }
        }

        //没有简答题直接给总分
        if (!haveShort) {
            examRecord.setTotalScore(logicScore);
        }

        examRecord.setLogicScore(logicScore);

        if (sf.length() > 0) {// 存在错的逻辑题
            examRecord.setErrorQuestionIds(sf.toString());
        }

        examRecord.setExamTime(new Date());
        examRecordMapper.insert(examRecord);
        return examRecord.getRecordId();
    }

    @Override
    public PageResponse<ExamRecord> getExamRecord(RecordVo recordVo) {
        QueryWrapper<ExamRecord> wrapper = new QueryWrapper<>();
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("exam_id", recordVo.getExamId()));

        wrapper.isNull(recordVo.getIsMark() == 0, "total_score");
        wrapper.isNotNull(recordVo.getIsMark() != 0, "total_score");

        if (StringUtils.isNotEmpty(recordVo.getSubsection())) {
            Exam exam = examMapper.selectOne(new QueryWrapper<Exam>().eq("exam_id", recordVo.getExamId()));
            switch (recordVo.getSubsection()) {
                case "0":
                    wrapper.eq("total_score", exam.getTotalScore());
                    break;
                case "1":
                    wrapper.ge("total_score", exam.getPassScore());
                    break;
                case "2":
                    wrapper.lt("total_score", exam.getPassScore());
                    break;
                default:
                    break;
            }
        }

        IPage<ExamRecord> page = examRecordMapper.selectPage(new Page<>(recordVo.getPageNo(), recordVo.getPageSize()), wrapper);

        return PageResponse.<ExamRecord>builder().data(page.getRecords()).total(page.getTotal()).build();
    }

    @Override
    public void setObjectQuestionScore(Integer totalScore, Integer examRecordId) {
        ExamRecord examRecord = examRecordMapper.selectOne(new QueryWrapper<ExamRecord>().eq("record_id", examRecordId));
        examRecord.setTotalScore(totalScore);
        examRecordMapper.update(examRecord, new UpdateWrapper<ExamRecord>().eq("record_id", examRecordId));
    }

    //根据题目id获取答案列表中的答案索引
    public static int getIndex(List<Answer> list, Integer questionId) {
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i).getQuestionId(), questionId)) {
                return i;
            }
        }
        return -1;
    }
}
