package com.java.exam.service.impl;


import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.entity.Profession;
import com.java.exam.mapper.ProfessionMapper;
import com.java.exam.service.ProfessionService;
import com.java.exam.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessionServiceImpl extends ServiceImpl<ProfessionMapper, Profession>
        implements ProfessionService {

    @Override
    public List<Tree<String>> getBySuGr( String queryText) {
        LambdaQueryWrapper<Profession> w = new LambdaQueryWrapper<>();
        w.like(StringUtils.isNotEmpty(queryText), Profession::getTitle, queryText);
        List<Profession> chapters = this.list(w);

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 最大递归深度
        treeNodeConfig.setDeep(3);
        //转换器
        List<Tree<String>> treeList = TreeUtil.build(chapters, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId().toString());
                    tree.setParentId(treeNode.getPid().toString());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getTitle());
                });
        if (ObjectUtils.isEmpty(treeList)){
            return new ArrayList<>();
        }
        return treeList;
    }

    @Override
    public void removeId(Integer id) {
        LambdaQueryWrapper<Profession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Profession::getPid,id);
        this.remove(wrapper);
        this.removeById(id);
    }
}




