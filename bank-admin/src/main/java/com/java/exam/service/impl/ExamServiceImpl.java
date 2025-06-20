package com.java.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.entity.Exam;
import com.java.exam.entity.ExamQuestion;
import com.java.exam.entity.ExamRecord;
import com.java.exam.exception.BaseException;
import com.java.exam.exception.ErrorCode;
import com.java.exam.mapper.ExamMapper;
import com.java.exam.mapper.ExamQuestionMapper;
import com.java.exam.mapper.ExamRecordMapper;
import com.java.exam.mapper.QuestionMapper;
import com.java.exam.service.ExamService;
import com.java.exam.utils.PageResponse;
import com.java.exam.vo.AddExamByQuestionVo;
import com.java.exam.vo.AddSmartExamVo;
import com.java.exam.vo.ExamQueryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.java.exam.utils.QueryWrapperUtils.setEqualsQueryWrapper;
import static com.java.exam.utils.QueryWrapperUtils.setLikeWrapper;


@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    private final QuestionMapper questionMapper;

    private final ExamMapper examMapper;

    private final ExamRecordMapper examRecordMapper;

    private final ExamQuestionMapper examQuestionMapper;

    @Override
    public PageResponse<Exam> getExamPage(ExamQueryVo examQueryVo) {
        QueryWrapper<Exam> wrapper = new QueryWrapper<>();

        setEqualsQueryWrapper(wrapper, Collections.singletonMap("type", examQueryVo.getExamType()));
        setLikeWrapper(wrapper, Collections.singletonMap("exam_name", examQueryVo.getExamName()));
        if (examQueryVo.getStartTime() != null) {
            wrapper.gt("start_time", examQueryVo.getStartTime().substring(0, examQueryVo.getStartTime().indexOf("T")));
        }
        if (examQueryVo.getEndTime() != null) {
            wrapper.lt("end_time", examQueryVo.getEndTime().substring(0, examQueryVo.getEndTime().indexOf("T")));
        }
        wrapper.orderByDesc("exam_id");
        IPage<Exam> page = examMapper.selectPage(new Page<>(examQueryVo.getPageNo(), examQueryVo.getPageSize()), wrapper);

        return PageResponse.<Exam>builder().data(page.getRecords()).total(page.getTotal()).build();
    }

    @Override
    public AddExamByQuestionVo getExamInfoById(Integer examId) {
        // 构造传递给前端的考试组合对象
        Exam exam = Optional.ofNullable(examMapper.selectById(examId))
                .orElseThrow(() -> new BaseException(ErrorCode.E_400001));
        AddExamByQuestionVo addExamByQuestionVo = AddExamByQuestionVo.builder()
                .examDesc(exam.getExamDesc())
                .examDuration(exam.getDuration())
                .examId(examId)
                .examName(exam.getExamName())
                .passScore(exam.getPassScore())
                .totalScore(exam.getTotalScore())
                .startTime(exam.getStartTime())
                .endTime(exam.getEndTime())
                .type(exam.getType())
                .password(exam.getPassword())
                .status(exam.getStatus())
                .build();

        // 考试中题目的对象
        ExamQuestion examQuestion = examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
        addExamByQuestionVo.setQuestionIds(examQuestion.getQuestionIds());
        addExamByQuestionVo.setScores(examQuestion.getScores());
        return addExamByQuestionVo;
    }

    @Override
    public void operationExam(Integer type, String ids) {
        String[] id = ids.split(",");
        switch (type) {
            case 1:
                setExamStatus(id, 1);
                break;
            case 2:
                setExamStatus(id, 2);
                break;
            case 3:
                Map<String, Object> map = new HashMap<>();
                for (String s : id) {
                    map.clear();
                    map.put("exam_id", Integer.parseInt(s));
                    examMapper.deleteByMap(map);
                    examQuestionMapper.deleteByMap(map);
                    examRecordMapper.deleteByMap(map);
                }
                break;
            default:
                throw new BaseException(ErrorCode.E_100106);
        }
    }

    @Transactional
    @Override
    public void addExamBySmart(AddSmartExamVo addSmartExam) {
        Exam exam = new Exam();
        exam.setStatus(addSmartExam.getStatus());
        exam.setDuration(addSmartExam.getExamDuration());
        if (addSmartExam.getEndTime() != null) exam.setEndTime(addSmartExam.getEndTime());
        if (addSmartExam.getStartTime() != null) exam.setStartTime(addSmartExam.getStartTime());
        exam.setExamDesc(addSmartExam.getExamDesc());
        exam.setExamName(addSmartExam.getExamName());
        exam.setType(addSmartExam.getType());
        // 设置及格分数
        exam.setPassScore(addSmartExam.getPassScore());
        // 设置总成绩
        exam.setTotalScore(addSmartExam.getTotalScore());
        // 设置密码如果有
        if (addSmartExam.getPassword() != null) {
            exam.setPassword(addSmartExam.getPassword());
        }
        ExamQuestion examQuestion = new ExamQuestion();
        //题目id字符串
        StringJoiner questionIds = new StringJoiner(",");
        //题目分数字符串
        StringJoiner questionScores = new StringJoiner(",");

        //考试难度
        Integer level = addSmartExam.getLevel();
        //考试科目
        Integer pid = Integer.parseInt(addSmartExam.getPid());
        //添加单选题
        if (addSmartExam.getSingleNum() != 0) {
            HashSet<Integer> ids = getTheRandomTopicIds(pid, level, addSmartExam.getSingleNum(), 1);
            ids.forEach(id -> {
                questionIds.add(id.toString());
                questionScores.add(addSmartExam.getSingleScore().toString());
            });
        }
        //添加多选题
        if (addSmartExam.getMultipleNum() != 0) {
            HashSet<Integer> ids = getTheRandomTopicIds(pid, level, addSmartExam.getMultipleNum(), 2);
            ids.forEach(id -> {
                questionIds.add(id.toString());
                questionScores.add(addSmartExam.getMultipleScore().toString());
            });
        }
        //添加判断题
        if (addSmartExam.getJudgeNum() != 0) {
            HashSet<Integer> ids = getTheRandomTopicIds(pid, level, addSmartExam.getJudgeNum(), 3);
            ids.forEach(id -> {
                questionIds.add(id.toString());
                questionScores.add(addSmartExam.getJudgeScore().toString());
            });
        }
        //添加填空题
        if (addSmartExam.getFillingNum() != 0) {
            HashSet<Integer> ids = getTheRandomTopicIds(pid, level, addSmartExam.getFillingNum(), 4);
            ids.forEach(id -> {
                questionIds.add(id.toString());
                questionScores.add(addSmartExam.getFillingScore().toString());
            });
        }
        //添加简答题
        if (addSmartExam.getShortNum() != 0) {
            HashSet<Integer> ids = getTheRandomTopicIds(pid, level, addSmartExam.getShortNum(), 5);
            ids.forEach(id -> {
                questionIds.add(id.toString());
                questionScores.add(addSmartExam.getShortScore().toString());
            });
        }

        //设置考试题目id
        examQuestion.setQuestionIds(questionIds.toString());
        //设置考试题目的分数
        examQuestion.setScores(questionScores.toString());

        examMapper.insert(exam);
        examQuestion.setExamId(exam.getExamId());
        examQuestionMapper.insert(examQuestion);
    }

    private HashSet<Integer> getTheRandomTopicIds(Integer pid, Integer level, Integer num, Integer type) {
        List<Integer> idList = questionMapper.getRandomQuestionIds(type, pid, level, num);
        HashSet<Integer> ids = new HashSet<>(idList);
        if (level != 0 && ids.size() < num) {
            ids.addAll(questionMapper.getRandomQuestionIds(type, pid, 0, num - ids.size()));
        }
        if (ids.size() < num){
            throw new BaseException(ErrorCode.E_500004);
        }
        return ids;
    }

    @Transactional
    @Override
    public void addExamByQuestionList(AddExamByQuestionVo addExamByQuestionVo) {
        Exam exam = new Exam();
        exam.setTotalScore(addExamByQuestionVo.getTotalScore());
        exam.setType(addExamByQuestionVo.getType());
        exam.setPassScore(addExamByQuestionVo.getPassScore());
        if (addExamByQuestionVo.getEndTime() != null) exam.setEndTime(addExamByQuestionVo.getEndTime());
        if (addExamByQuestionVo.getStartTime() != null) exam.setStartTime(addExamByQuestionVo.getStartTime());
        exam.setExamDesc(addExamByQuestionVo.getExamDesc());
        exam.setExamName(addExamByQuestionVo.getExamName());
        exam.setDuration(addExamByQuestionVo.getExamDuration());
        // 设置密码如果有
        if (addExamByQuestionVo.getPassword() != null) {
            exam.setPassword(addExamByQuestionVo.getPassword());
        }
        exam.setStatus(addExamByQuestionVo.getStatus());
        // 设置id
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setScores(addExamByQuestionVo.getScores());
        examQuestion.setQuestionIds(addExamByQuestionVo.getQuestionIds());

        examMapper.insert(exam);
        examQuestion.setExamId(exam.getExamId());
        examQuestionMapper.insert(examQuestion);
    }

    @Override
    public void updateExamInfo(AddExamByQuestionVo addExamByQuestionVo) {
        Exam exam = new Exam();
        exam.setTotalScore(addExamByQuestionVo.getTotalScore());
        exam.setType(addExamByQuestionVo.getType());
        exam.setPassScore(addExamByQuestionVo.getPassScore());
        exam.setEndTime(addExamByQuestionVo.getEndTime());
        exam.setStartTime(addExamByQuestionVo.getStartTime());
        exam.setExamDesc(addExamByQuestionVo.getExamDesc());
        exam.setExamName(addExamByQuestionVo.getExamName());
        exam.setDuration(addExamByQuestionVo.getExamDuration());
        // 设置密码如果有
        if (addExamByQuestionVo.getPassword() != null) {
            exam.setPassword(addExamByQuestionVo.getPassword());
        } else {
            exam.setPassword(null);
        }
        exam.setStatus(addExamByQuestionVo.getStatus());
        exam.setExamId(addExamByQuestionVo.getExamId());
        // 设置考试的题目和分值信息
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExamId(addExamByQuestionVo.getExamId());
        examQuestion.setScores(addExamByQuestionVo.getScores());
        examQuestion.setQuestionIds(addExamByQuestionVo.getQuestionIds());

        examMapper.update(exam, new UpdateWrapper<Exam>().eq("exam_id", exam.getExamId()));
        examQuestionMapper.update(examQuestion, new UpdateWrapper<ExamQuestion>().eq("exam_id", exam.getExamId()));
    }

    @Override
    public String getQuestionIdsByExamId(Integer examId) {
        ExamQuestion examQuestion = examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
        return examQuestion.getQuestionIds();
    }

    private void setExamStatus(String[] id, int status) {
        for (String s : id) {
            Exam exam = examMapper.selectOne(new QueryWrapper<Exam>().eq("exam_id", Integer.parseInt(s)));
            exam.setStatus(status);
            examMapper.update(exam, new UpdateWrapper<Exam>().eq("exam_id", s));
        }
    }

    @Override
    public List<String> getExamPassRateEchartData() {
        List<Exam> exams = examMapper.selectList(null);
        List<ExamRecord> examRecords = examRecordMapper.selectList(new QueryWrapper<ExamRecord>().isNotNull("total_score"));
        // 考试的名称
        String[] examNames = new String[exams.size()];
        // 考试通过率
        double[] passRates = new double[exams.size()];

        double total;
        double pass;
        for (int i = 0; i < exams.size(); i++) {
            examNames[i] = exams.get(i).getExamName();
            total = 0;
            pass = 0;
            for (ExamRecord examRecord : examRecords) {
                if (Objects.equals(examRecord.getExamId(), exams.get(i).getExamId())) {
                    total++;
                    if (examRecord.getTotalScore() >= exams.get(i).getPassScore()) pass++;
                }
            }
            passRates[i] = pass / total;
        }
        for (int i = 0; i < passRates.length; i++) {
            if (Double.isNaN(passRates[i])) passRates[i] = 0;
        }
        List<String> list = new ArrayList<>();
        String res1 = Arrays.toString(examNames);
        String res2 = Arrays.toString(passRates);
        list.add(res1.substring(1, res1.length() - 1).replaceAll(" ", ""));
        list.add(res2.substring(1, res2.length() - 1).replaceAll(" ", ""));
        return list;
    }

    @Override
    public List<String> getExamNumbersEchartData() {
        List<Exam> exams = examMapper.selectList(null);
        List<ExamRecord> examRecords = examRecordMapper.selectList(null);
        // 考试的名称
        String[] examNames = new String[exams.size()];
        // 考试的考试次数
        String[] examNumbers = new String[exams.size()];

        int cur;
        for (int i = 0; i < exams.size(); i++) {
            examNames[i] = exams.get(i).getExamName();
            cur = 0;
            for (ExamRecord examRecord : examRecords) {
                if (Objects.equals(examRecord.getExamId(), exams.get(i).getExamId())) {
                    cur++;
                }
            }
            examNumbers[i] = cur + "";
        }
        List<String> list = new ArrayList<>();
        String res1 = Arrays.toString(examNames);
        String res2 = Arrays.toString(examNumbers);
        list.add(res1.substring(1, res1.length() - 1).replaceAll(" ", ""));
        list.add(res2.substring(1, res2.length() - 1).replaceAll(" ", ""));
        return list;
    }
}
