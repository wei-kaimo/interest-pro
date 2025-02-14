package com.interest.blog.service;

import com.interest.blog.model.request.ArticleCommentRequest;
import com.interest.blog.commom.model.PageResult;
import com.interest.blog.commom.model.PageWrapper;
import com.interest.blog.commom.model.response.MsgContentVO;

import java.util.List;
import java.util.Set;

public interface ArticleCommentService {

    void putArticleCommentService(ArticleCommentRequest articleCommentRequest);

    PageResult getArticleCommentByArticleId(int articleId, PageWrapper pageWrapper);

    List<MsgContentVO> getMsgContentByIds(Set<Integer> ids);
}
