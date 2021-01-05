package com.interest.bbs.commom.model.response;

import lombok.Data;

@Data
public class MsgContentVO {

    private Integer id;

    private String title;

    private String replyContent;

    private Integer toId;

    private Integer replyUserId;

}
