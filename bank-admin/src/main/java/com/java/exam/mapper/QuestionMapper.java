package com.java.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.exam.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper extends BaseMapper<Question> {


    @Select("<script> SELECT id FROM question where profession_id = #{pid} and qu_type = #{type} " +
            "<if test='level!=0'>" +
            "and level = #{level} " +
            "</if>" +
            "ORDER BY RAND() LIMIT #{num}; </script>")
    List<Integer> getRandomQuestionIds(@Param("type") Integer type,@Param("pid") Integer pid ,@Param("level") Integer level, @Param("num")Integer num);


}
