package com.java.exam.controller;

import com.java.exam.entity.Profession;
import com.java.exam.loggin.Log;
import com.java.exam.service.ProfessionService;
import com.java.exam.utils.HttpResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = "专业课程相关接口")
@RequestMapping("/teacher/profession")
@RequiredArgsConstructor
public class ProfessionController {

    private final ProfessionService professionService;

    @GetMapping("/get")
    public HttpResult getBySuGr(String queryText){
        return HttpResult.success(professionService.getBySuGr(queryText));
    }

    @PostMapping("/add")
    public HttpResult add(@RequestBody Profession profession){
        professionService.save(profession);
        return HttpResult.success();
    }

    @PostMapping("/update")
    public HttpResult update(@RequestBody Profession profession){
        professionService.updateById(profession);
        return HttpResult.success();
    }

    @Log("删除课程")
    @GetMapping("/delete")
    public HttpResult delete(Integer id){
        professionService.removeId(id);
        return HttpResult.success();
    }

}
