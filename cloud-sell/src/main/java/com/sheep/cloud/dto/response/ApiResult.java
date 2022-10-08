package com.sheep.cloud.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.ssssheep.summer.pojo.dto
 * @datetime 2022/7/30 Saturday
 */
@Data
@Builder
@NoArgsConstructor
public class ApiResult implements Serializable {

    private static final long serialVersionUID = -8440958610795020343L;

    public static final int CODE_SUCCESS = 200;

    public static final int CODE_ERROR = 500;

    public static final int CODE_WARNING = 501;

    public static final int CODE_NOT_PERMISSION = 403;

    public static final int CODE_NOE_LOGIN = 401;

    public static final int CODE_INVALID_REQUEST = 400;

    public static final int CODE_NOT_BIND = 402;

    public int code;

    public String msg;

    public Object data;

    public int getCode() {
        return code;
    }

    public ApiResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public ApiResult setData(Object data) {
        this.data = data;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(Class<T> cs) {
        return (T) data;
    }

    public ApiResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResult success() {
        return new ApiResult(CODE_SUCCESS, null, null);
    }

    public static ApiResult success(String msg) {
        return new ApiResult(CODE_SUCCESS, msg, null);
    }

    public static ApiResult success(String msg, Object data) {
        return new ApiResult(CODE_SUCCESS, msg, data);
    }

    public static ApiResult success(Object data) {
        return new ApiResult(CODE_SUCCESS, "success", data);
    }

    public static ApiResult error() {
        return new ApiResult(CODE_ERROR, "出错了", null);
    }

    public static ApiResult error(String msg) {
        return new ApiResult(CODE_ERROR, msg, null);
    }

    public static ApiResult error(String msg, Object data) {
        return new ApiResult(CODE_ERROR, msg, data);
    }

    public static ApiResult notLogin() {
        return new ApiResult(CODE_NOE_LOGIN, "未登录，请先进行登录", null);
    }

    public static ApiResult noPermissions() {
        return new ApiResult(CODE_NOT_PERMISSION, "权限不足", null);
    }

    public static ApiResult build(int code, String msg) {
        return new ApiResult(code, msg, null);
    }

    public static ApiResult getByBoolean(boolean b) {
        return b ? success() : error();
    }


    public static ApiResult warning(String msg, Object data) {
        return new ApiResult(CODE_WARNING, msg, data);
    }

    public static ApiResult warning(String msg) {
        return new ApiResult(CODE_WARNING, msg, null);
    }

    public static ApiResult notBind(String msg) {
        return new ApiResult(CODE_NOT_BIND, msg, null);
    }

    public static ApiResult notBind(String msg, Object data) {
        return new ApiResult(CODE_NOT_BIND, msg, data);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
