package com.example.wxhk.util;

import com.example.wxhk.model.PrivateChatMsg;
import com.example.wxhk.tcp.vertx.InitWeChat;
import io.vertx.core.json.JsonObject;
import org.dromara.hutool.core.util.XmlUtil;
import org.dromara.hutool.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 常见方法
 * @author wt
 * @date 2023/05/29
 */
public class HttpSendUtil {

    protected  static final Log log = Log.get();
    public static JsonObject 通过好友请求(PrivateChatMsg msg){
        Document document = XmlUtil.parseXml(msg.getContent());
        String encryptusername = document.getDocumentElement().getAttribute("encryptusername");
        String ticket = document.getDocumentElement().getAttribute("ticket");
        return HttpSyncUtil.exec(HttpAsyncUtil.Type.通过好友申请, new JsonObject().put("v3", encryptusername).put("v4", ticket).put("permission", "0"));
    }
    public static JsonObject 确认收款(PrivateChatMsg msg){
        try {
            String content = msg.getContent();
            Document document = XmlUtil.parseXml(content);
            Node paysubtype = document.getElementsByTagName("paysubtype").item(0);
            if("1".equals(paysubtype.getTextContent().trim())){
                // 手机发出去的
                String textContent = document.getElementsByTagName("receiver_username").item(0).getTextContent();
                if(!InitWeChat.WXID_MAP.contains(textContent)){
                    return new JsonObject().put("spick",true);
                }
                Node transcationid = document.getDocumentElement().getElementsByTagName("transcationid").item(0);
                Node transferid = document.getDocumentElement().getElementsByTagName("transferid").item(0);
                return HttpSyncUtil.exec(HttpAsyncUtil.Type.确认收款, new JsonObject().put("wxid",msg.getFromUser())
                        .put("transcationId",transcationid.getTextContent())
                        .put("transferId",transferid.getTextContent()));

            }
            // 如果是确认接受收款,则跳过
            return new JsonObject();

        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }


    public static String 获取当前登陆微信id(){
        JsonObject exec = HttpSyncUtil.exec(HttpAsyncUtil.Type.获取登录信息, new JsonObject());
        return exec.getJsonObject("data").getString("wxid");
    }

}
