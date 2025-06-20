package com.java.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.entity.Answer;
import com.java.exam.entity.Question;
import com.java.exam.mapper.AnswerMapper;
import com.java.exam.mapper.QuestionBankMapper;
import com.java.exam.mapper.QuestionMapper;
import com.java.exam.service.QuestionService;
import com.java.exam.utils.PageResponse;
import com.java.exam.utils.StringUtils;
import com.java.exam.vo.QuestionImportVo;
import com.java.exam.vo.QuestionQueryVo;
import com.java.exam.vo.QuestionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.java.exam.utils.QueryWrapperUtils.setEqualsQueryWrapper;
import static com.java.exam.utils.QueryWrapperUtils.setLikeWrapper;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    private final QuestionMapper questionMapper;

    private final QuestionBankMapper questionBankMapper;

    private final AnswerMapper answerMapper;

    @Override
    public PageResponse<Question> getQuestion(QuestionQueryVo queryVo) {
        IPage<Question> questionPage = new Page<>(queryVo.getPageNo(), queryVo.getPageSize());

        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        Map<String, Object> likeQueryParams = new HashMap<>(2);
        likeQueryParams.put("qu_bank_name", queryVo.getQuestionBank());
        likeQueryParams.put("qu_content", queryVo.getQuestionContent());
        setLikeWrapper(wrapper, likeQueryParams);
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("qu_type", queryVo.getQuestionType()));

        if (StringUtils.isNotEmpty(queryVo.getProfessionIds())) {
            String[] ids = queryVo.getProfessionIds().split(",");
            wrapper.in("profession_id", Arrays.asList(ids));
        }

        questionPage = questionMapper.selectPage(questionPage, wrapper);
        return PageResponse.<Question>builder()
                .data(questionPage.getRecords())
                .total(questionPage.getTotal())
                .build();
    }

    @Override
    public QuestionVo getQuestionVoById(Integer id) {
        Question question = questionMapper.selectById(id);
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("question_id", id));
        return buildQuestionVoByQuestionAndAnswer(question, answer);
    }

    @Override
    public PageResponse<QuestionVo> getQuestionVoByIds(List<Integer> ids) {
        List<Question> questions = questionMapper.selectBatchIds(ids);
        List<Answer> answers = answerMapper.selectList(new QueryWrapper<Answer>().in("question_id", ids));
        List<QuestionVo> questionVos = questions.stream()
                .map(question -> {
                    Answer currentQuestionAnswer = answers.stream()
                            .filter(answer -> answer.getQuestionId().equals(question.getId()))
                            .findFirst()
                            .orElse(null);
                    return buildQuestionVoByQuestionAndAnswer(question, currentQuestionAnswer);
                }).collect(Collectors.toList());
        return PageResponse.<QuestionVo>builder()
                .data(questionVos)
                .total(questionVos.size())
                .build();
    }

    @Override
    public void deleteQuestionByIds(String questionIds) {
        String[] ids = questionIds.split(",");
        Map<String, Object> map = new HashMap<>();
        for (String id : ids) {
            map.clear();
            map.put("question_id", id);
            //  1. 删除数据库的题目信息
            questionMapper.deleteById(Integer.parseInt(id));
            // 2. 删除答案表对应当前题目id的答案
            answerMapper.deleteByMap(map);
        }
    }

    @Transactional
    @Override
    public void addQuestion(QuestionVo questionVo) {
        Question question = new Question();
        // 设置基础字段
        question.setQuType(questionVo.getQuestionType());
        setQuestionField(question, questionVo);
        // 设置题目插图
        if (questionVo.getImages().length != 0) {
            String QuImages = Arrays.toString(questionVo.getImages());
            question.setImage(QuImages.substring(1, QuImages.length() - 1).replaceAll(" ", ""));
        }
        buildBankName(questionVo, question);
        questionMapper.insert(question);
        Integer currentQuId = question.getId();
        // 设置答案对象
        StringBuilder multipleChoice = new StringBuilder();
        if (questionVo.getQuestionType() != 5) {// 不为简答题
            Answer answer = new Answer();
            answer.setQuestionId(currentQuId);
            StringBuilder answers = new StringBuilder();
            for (int i = 0; i < questionVo.getAnswer().length; i++) {
                buildAnswer(answers, questionVo, i, multipleChoice, answer);
            }
            buildMultiQuestionAnswer(questionVo, multipleChoice, answer, answers);
            answerMapper.insert(answer);
        }
    }

    @Override
    public void updateQuestion(QuestionVo questionVo) {
        Question question = new Question();
        // 设置基础字段
        question.setQuType(questionVo.getQuestionType());
        question.setId(questionVo.getQuestionId());
        setQuestionField(question, questionVo);
        // 设置题目插图
        if (questionVo.getImages() != null && questionVo.getImages().length != 0) {
            String QuImages = Arrays.toString(questionVo.getImages());
            question.setImage(QuImages.substring(1, QuImages.length() - 1).replaceAll(" ", ""));
        }
        buildBankName(questionVo, question);
        // 更新
        questionMapper.update(question, new UpdateWrapper<Question>().eq("id", questionVo.getQuestionId()));
        // 设置答案对象
        StringBuilder multipleChoice = new StringBuilder();
        if (questionVo.getQuestionType() != 5) {// 不为简答题
            Answer answer = new Answer();
            answer.setQuestionId(questionVo.getQuestionId());
            StringBuilder answers = new StringBuilder();
            for (int i = 0; i < questionVo.getAnswer().length; i++) {
                buildAnswer(answers, questionVo, i, multipleChoice, answer);
            }
            buildMultiQuestionAnswer(questionVo, multipleChoice, answer, answers);
            answerMapper.update(answer, new UpdateWrapper<Answer>().eq("question_id", questionVo.getQuestionId()));
        }
    }

    @Override
    public void importQuestion(QuestionImportVo questionImportVo) {
        List<QuestionVo> questionData = questionImportVo.getQuestionData();
        List<QuestionVo> collect = questionData.stream().peek(questionVo -> {
            questionVo.setProfessionId(questionImportVo.getProfessionId());
            questionVo.setBankId(questionImportVo.getBankId());
            questionVo.setCreatePerson(questionImportVo.getCreatePerson());
        }).collect(Collectors.toList());
        collect.forEach(this::addQuestion);
    }

    private void buildAnswer(StringBuilder answers, QuestionVo questionVo, int i, StringBuilder multipleChoice, Answer answer) {
        answers.append(questionVo.getAnswer()[i].getAnswer()).append(",");
        // 设置对的选项的下标值
        if (questionVo.getQuestionType() == 2) {// 多选
            if (questionVo.getAnswer()[i].getIsTrue().equals("true")) multipleChoice.append(i).append(",");
        } else if (questionVo.getQuestionType() == 4){// 填空题
            answer.setTrueOption("0");
        } else {// 单选和判断 都是仅有一个答案
            if (questionVo.getAnswer()[i].getIsTrue().equals("true")) {
                answer.setTrueOption(i + "");
                answer.setAnalysis(questionVo.getAnswer()[i].getAnalysis());
            }
        }
    }

    private void buildMultiQuestionAnswer(QuestionVo questionVo, StringBuilder multipleChoice, Answer answer, StringBuilder answers) {
        if (questionVo.getQuestionType() == 2)
            answer.setTrueOption(multipleChoice.substring(0, multipleChoice.toString().length() - 1));
        String handleAnswers = answers.toString();
        if (handleAnswers.length() != 0) handleAnswers = handleAnswers.substring(0, handleAnswers.length() - 1);
        // 设置所有的选项
        answer.setAllOption(handleAnswers);
    }

    private void buildBankName(QuestionVo questionVo, Question question) {
        StringBuilder bankNames = new StringBuilder();
        for (Integer integer : questionVo.getBankId()) {
            bankNames.append(questionBankMapper.selectById(integer).getBankName()).append(",");
        }
        String names = bankNames.toString();
        names = names.substring(0, names.length() - 1);
        question.setQuBankName(names);
    }

    private void setQuestionField(Question question, QuestionVo questionVo) {
        question.setCreateTime(new Date());
        question.setProfessionId(questionVo.getProfessionId());
        question.setLevel(questionVo.getQuestionLevel());
        question.setAnalysis(questionVo.getAnalysis());
        question.setQuContent(questionVo.getQuestionContent());
        question.setCreatePerson(questionVo.getCreatePerson());
        // 设置所属题库
        String bankIds = Arrays.toString(questionVo.getBankId());
        question.setQuBankId(bankIds.substring(1, bankIds.length() - 1).replaceAll(" ", ""));
    }

    private QuestionVo buildQuestionVoByQuestionAndAnswer(Question question, Answer answer) {
        QuestionVo questionVo = new QuestionVo();
        // 设置字段
        questionVo.setQuestionContent(question.getQuContent());
        questionVo.setAnalysis(question.getAnalysis());
        questionVo.setProfessionId(question.getProfessionId());
        questionVo.setQuestionType(question.getQuType());
        questionVo.setQuestionLevel(question.getLevel());
        questionVo.setQuestionId(question.getId());
        if (question.getImage() != null && !Objects.equals(question.getImage(), "")) {
            questionVo.setImages(question.getImage().split(","));
        }
        questionVo.setCreatePerson(question.getCreatePerson());
        // 设置所属题库
        if (!Objects.equals(question.getQuBankId(), "")) {
            String[] bids = question.getQuBankId().split(",");
            Integer[] bankIds = new Integer[bids.length];
            for (int i = 0; i < bids.length; i++) {
                bankIds[i] = Integer.parseInt(bids[i]);
            }
            questionVo.setBankId(bankIds);
        }
        if (answer != null) {
            String[] allOption = answer.getAllOption().split(",");
            QuestionVo.Answer[] qa = new QuestionVo.Answer[allOption.length];
            if (question.getQuType() == 2) {
                // 多选
                List<Integer> multiTrueOptions = Arrays.stream(answer.getTrueOption().split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    if (multiTrueOptions.contains(i)) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            } else {
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    if (i == Integer.parseInt(answer.getTrueOption())) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            }
            questionVo.setAnswer(qa);
        }
        return questionVo;
    }

}
