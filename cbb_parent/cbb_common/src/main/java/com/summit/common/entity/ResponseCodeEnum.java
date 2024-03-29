package com.summit.common.entity;

import com.summit.common.constant.ResponseCode;

public enum ResponseCodeEnum implements ResponseCode {

    //=============================系统通用报错================================

    CODE_0000("请求成功"),// 请求成功返回

    CODE_9991("对象不存在"),// 用于更新操作
    CODE_9992("对象已存在"),// 用于新增操作
    CODE_9993("非法的请求参数"),// 用于过滤GET请求
    CODE_9994("非法的文件类型"),// 用于上传文件
    CODE_9995("文件不能为空"),// 用于上传文件
    CODE_9996("文件大小超限"),// 用于上传文件

    CODE_9999("服务端未知异常"),// 未知异常返回
    //==============================配置管理相关================================
    CODE_4007("访问受限，需要token"),// 用于调用接口前校验token
    CODE_4008("无效的token"),// 用于token失效
    CODE_4009("旧密码错误"),// 用于修改密码
    CODE_4010("用户名或密码错误"),// 用于用户登录
    CODE_4011("登录状态失效，请重新登录"),// 用于登录状态失效
    CODE_4012("权限不足"),// 用于权限不足
    CODE_4013("两次输入的密码不一致"),// 用于用户注册或修改密码
    CODE_4014("密码解密失败"),// 用于修改密码
    CODE_4022("用户已存在"),// 用于用户注册
    CODE_4023("用户不存在或已删除"),// 用于用户登录
    CODE_4024("用户被锁定，请联系管理员进行解锁"),// 用于用户登录
    CODE_4025("消息发送失败"),// 用于邮件和短信发送
    CODE_4026("验证码不能为空"),// 用于验证码短信
    CODE_4027("验证码不正确"),// 用于验证码短信
    CODE_4028("验证码已失效"),// 用于验证码短信
    CODE_4029("该手机号在24小时内未发送过验证码"),// 用于验证码短信
    CODE_4030("密码不能为空"),// 用于登录、注册、密码找回、修改密码
    CODE_4031("用户未启用"),
    CODE_4032("用户已过期"),
    CODE_4033("天气数据请求失败");


    private String message;// 提示消息

    private ResponseCodeEnum(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
