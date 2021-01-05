package com.interest.bbs.commom.model.Request;

import lombok.Data;

@Data
public class MsgRecodeRequest {

    private Integer ownerId;

    private Integer form;

    private Integer replyCardId;

    private Integer commentId;

    private String replyTime;

    private Integer isRead;

}
