package com.interest.auth.authentication;

import com.interest.auth.exception.LoginFailureExcepiton;
import com.interest.auth.model.entity.UserDetailEntity;
import com.interest.auth.model.entity.UserEntity;
import com.interest.auth.model.entity.UserQQEntity;
import com.interest.auth.picture.PictureService;
import com.interest.auth.properties.QQProperties;
import com.interest.auth.service.UserDetailService;
import com.interest.auth.service.UserQQService;
import com.interest.auth.service.UserService;
import com.interest.auth.commom.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service(value = "qQAuthentication")
public class QQAuthentication implements MyAuthentication {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private UserQQService userQQService;

    @Autowired
    private QQProperties qqProperties;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String QQ_ACCESSS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    private static final String QQ_OPENID_URL = "https://graph.qq.com/oauth2.0/me";

    private static final String QQ_USER_URL = "https://graph.qq.com/user/get_user_info";

    @Override
    @Transactional
    public String getUserId(String code) {

        String appid = qqProperties.getAppid();
        String appkey = qqProperties.getAppkey();

        /* 获取access_token */
        String tokenUrl = QQ_ACCESSS_TOKEN_URL + "?grant_type=authorization_code&client_id=" + appid +
                "&client_secret=" + appkey + "&code=" + code + "&redirect_uri=http://www.lovemtt.com/qq";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(tokenUrl, String.class);
        String message = responseEntity.getBody().trim();
        String access_token = message.split("&")[0].split("=")[1];

        /* 获取openid */
        String openidUrl = QQ_OPENID_URL + "?access_token=" + access_token;
        responseEntity = restTemplate.getForEntity(openidUrl, String.class);
        message = responseEntity.getBody().trim();
        message = message.split("\\(")[1].split("\\)")[0];

        UserEntity userEntity = null;

        try {
            JSONObject callback = new JSONObject(message);
            String openid = callback.getString("openid");
            if (openid == null) {
                throw new LoginFailureExcepiton(callback.toString());
            }

            userEntity = userService.getEntityByQqid(openid);
            if (userEntity == null) {
                /* 获取qq用户信息 */
                String userInfoUrl = QQ_USER_URL + "?access_token=" + access_token +
                        "&oauth_consumer_key=" + appid + "&openid=" + openid;
                responseEntity = restTemplate.getForEntity(userInfoUrl, String.class);
                JSONObject qqUserInfo = new JSONObject(responseEntity.getBody().trim());

                return insertUser(qqUserInfo, openid);
            } else {
                return String.valueOf(userEntity.getId());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String insertUser(JSONObject qqUserInfo, String openid) throws JSONException {

        StringBuilder qqHeadImg = null;
        if (qqUserInfo.getString("figureurl_qq_2") != null && !"".equals(qqUserInfo.getString("figureurl_qq_2"))) {
            qqHeadImg = new StringBuilder(qqUserInfo.getString("figureurl_qq_2"));
        } else {
            qqHeadImg = new StringBuilder(qqUserInfo.getString("figureurl_qq_1"));
        }

        if (qqHeadImg.indexOf("https") < 0) {
            qqHeadImg.insert(4, "s");
        }

        String headImg = qqHeadImg.toString();


        UserEntity userEntity = new UserEntity();
        userEntity.setHeadimg(headImg);
        userEntity.setName(qqUserInfo.getString("nickname"));
        userEntity.setQqid(openid);
        userEntity.setCreateTime(DateUtil.currentTimestamp());
        userService.insertUserByQq(userEntity);

        UserQQEntity userQQEntity = new UserQQEntity();
        userQQEntity.setOpenid(openid);
        userQQEntity.setNickname(userEntity.getName());
        userQQEntity.setFigureurlQq1(qqHeadImg.toString());
        userQQEntity.setGender(qqUserInfo.getString("gender"));
        userQQEntity.setUserid(userEntity.getId());
        userQQService.insertEntity(userQQEntity);

        UserDetailEntity userDetailEntity = new UserDetailEntity();
        userDetailEntity.setUserid(userEntity.getId());
        userDetailService.insert(userDetailEntity);

        // 异步将网络资源下载到本地，并且更新数据库
        threadPoolTaskExecutor.execute(() -> {
            userService.updateUserHeadImg(userEntity.getId(), pictureService.saveImage(headImg, "/head", "jpg"));
        });
        return String.valueOf(userEntity.getId());
    }

}
