package com.interest.message.service;

import com.interest.message.commom.model.PageResult;
import com.interest.message.commom.model.Request.MsgRecodeRequest;

public interface MsgRecordsService {

    /**
     * 查询未读消息条数
     * @param userid
     * @return
     */
    int getUnreadMsgCount(Integer userid);

    void insertMessage(MsgRecodeRequest msgRecodeRequest);

    /**
     * 查用户消息
     * @param pageSize
     * @param start
     * @return
     */
    PageResult getUserMegsResult(int pageSize, int start);

    void updateMsgRecordIsRead(int msgRecordId, int readSign);
}
