package com.summit.manager.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MsgInfo {
    //编号
    private String msgNo;
    //消息分组编号
    private String groupId;
    //标题
    private String title;
    //内容
    private String content;
    //创建时间
    private Date createTime;
    //发送人
    private String sendUserId;
    //状态[1待发送、2发送中、3发送成功、4发送失败]
    private Integer status;
    //发送时间
    private Date sendTime;
    //类型
    private Integer type;

    //查询使用的用户编号
    private String userId;
    //系统编码
    private String sysNo;
    //接收人编码
    private String receUserId;
    //接收人编码集合字符串
    private String receUserIds;
    //查询使用的是否已读
    private Integer isRead;
    //阅读时间
    private Date readTime;
    //分组编码集合字符串
    private String groupIds;
    //分组编码集合
    private List<String> groupIdList;
    //接收短信或邮件的内容[不传默认为content内容]
    private String receContent;
    //接收短信的内容[不传默认为receContent内容]
    private String smsContent;
    //邮件的附件[{"name":"test.txt","path":"http://xx.xx.x/dafsdf.txt"}]
    private String receEmailFiles;
    //接收手机[多个;分隔]
    private String recePhones;
    //接收邮箱[多个;分隔]
    private String receEmails;
    //类型名称
    private String typeName;
    //是否打开邮件短信校验[0否、1是]
    private Integer isOpenCheck;
}
