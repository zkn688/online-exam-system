package com.java.exam.service;


import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.exam.entity.Profession;

import java.util.List;

public interface ProfessionService extends IService<Profession> {

    List<Tree<String>> getBySuGr(String queryText);

    void removeId(Integer id);
}
