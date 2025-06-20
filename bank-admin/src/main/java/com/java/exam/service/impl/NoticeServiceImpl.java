package com.java.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.exam.entity.Notice;
import com.java.exam.exception.BaseException;
import com.java.exam.exception.ErrorCode;
import com.java.exam.mapper.NoticeMapper;
import com.java.exam.service.NoticeService;
import com.java.exam.utils.PageResponse;
import com.java.exam.utils.QueryWrapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public void setAllNoticeIsHistoryNotice() {
        Notice notice = new Notice();
        notice.setStatus(0);
        noticeMapper.update(notice,null);
    }

    @Override
    public String getCurrentNotice() {
        Notice status = noticeMapper.selectOne(new QueryWrapper<Notice>().eq("status", "1"));
        if (status == null){
            return null;
        }
        return status.getContent();
    }

    @Override
    public PageResponse<Notice> getAllNotices(String content, Integer pageNo, Integer pageSize) {
        IPage<Notice> noticeIPage = new Page<>(pageNo, pageSize);
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        QueryWrapperUtils.setLikeWrapper(wrapper, Collections.singletonMap("content", content));
        wrapper.orderByDesc("status", "update_time", "create_time");

        noticeIPage = noticeMapper.selectPage(noticeIPage, wrapper);

        return PageResponse.<Notice>builder().data(noticeIPage.getRecords()).total(noticeIPage.getTotal()).build();
    }

    @Override
    public void publishNotice(Notice notice) {
        if (notice.getStatus() == 1) {//  当前发布的是置顶公告
            //  1. 将当前所有公告设置为历史公告
            setAllNoticeIsHistoryNotice();
            //  2. 新增最新公告进去
            notice.setCreateTime(new Date());
           noticeMapper.insert(notice);
        } else if (notice.getStatus() == 0) {//  不发布最新公告
            notice.setCreateTime(new Date());
            noticeMapper.insert(notice);
        } else {
            throw new BaseException(ErrorCode.E_300001);
        }
    }

    @Override
    public void deleteNoticeByIds(String noticeIds) {
        // 转换成数组 需要操作的用户的id数组
        String[] ids = noticeIds.split(",");
        Notice currentNotice = noticeMapper.selectOne(new QueryWrapper<Notice>().eq("status", "1"));
        for (String id : ids) {
            if (currentNotice.getNId().equals(Integer.parseInt(id))) {
                continue;
            }
            noticeMapper.deleteById(Integer.parseInt(id));
        }
    }

    @Override
    public void updateNotice(Notice notice) {
        //  查询当前公告信息
        QueryWrapper<Notice> wrapper = new QueryWrapper<Notice>().eq("n_id", notice.getNId());
        Notice targetNotice = noticeMapper.selectOne(wrapper);

        if (notice.getStatus() == 1) {//  当前更新成为置顶公告
            //  将当前所有公告设置为历史公告
            setAllNoticeIsHistoryNotice();
            targetNotice.setUpdateTime(new Date());
            targetNotice.setContent(notice.getContent());
            targetNotice.setStatus(notice.getStatus());

            noticeMapper.update(targetNotice, wrapper);
        } else if (notice.getStatus() == 0) {//  不发布最新公告
            targetNotice.setUpdateTime(new Date());
            targetNotice.setContent(notice.getContent());
            targetNotice.setStatus(notice.getStatus());
            noticeMapper.update(targetNotice, wrapper);
        } else {
            throw new BaseException(ErrorCode.E_300002);
        }
    }
}
