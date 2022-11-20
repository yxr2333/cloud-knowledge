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
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -8440958610795020343L;

    public static final int CODE_SUCCESS = 200;

    public static final int CODE_ERROR = 500;

    public static final int CODE_WARNING = 501;

    public static final int CODE_NOT_PERMISSION = 403;

    public static final int CODE_NOE_LOGIN = 401;

    public static final int CODE_INVALID_REQUEST = 400;

    public int code;

    public String msg;

    public T data;

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

    public ApiResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public ApiResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult<T> success() {
        this.code = CODE_SUCCESS;
        return this;
    }

    public ApiResult<T> success(String msg) {
        this.code = CODE_SUCCESS;
        this.msg = msg;
        return this;
    }

    public ApiResult<T> success(String msg, T data) {
        this.code = CODE_SUCCESS;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ApiResult<T> success(T data) {
        this.code = CODE_SUCCESS;
        this.data = data;
        return this;
    }

    public ApiResult<T> error() {
        this.code = CODE_ERROR;
        this.msg = "出错了";
        return this;
    }

    public ApiResult<T> error(String msg) {
        this.code = CODE_ERROR;
        this.msg = msg;
        return this;
    }

    public ApiResult<T> error(String msg, T data) {
        this.code = CODE_ERROR;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ApiResult<T> error(T data) {
        this.code = CODE_ERROR;
        this.data = data;
        return this;
    }

    public ApiResult<T> fail() {
        this.code = CODE_ERROR;
        this.msg = "出错了";
        return this;
    }

    public ApiResult<T> fail(String msg) {
        this.code = CODE_ERROR;
        this.msg = msg;
        return this;
    }

    public ApiResult<T> fail(String msg, T data) {
        this.code = CODE_ERROR;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ApiResult<T> fail(T data) {
        this.code = CODE_ERROR;
        this.data = data;
        return this;
    }

    public ApiResult notLogin() {
        this.code = CODE_NOE_LOGIN;
        this.msg = "未登录，请先登录";
        return this;
    }

    public ApiResult<T> notBind(String msg, T data) {
        this.code = CODE_NOE_LOGIN;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ApiResult<T> noPermissions() {
        this.code = CODE_NOT_PERMISSION;
        this.msg = "没有权限";
        return this;
    }

    public ApiResult<T> build(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ApiResult<T> getByBoolean(boolean b) {
        return b ? success() : error();
    }


    public ApiResult<T> warning(String msg, T data) {
        this.code = CODE_WARNING;
        this.msg = msg;
        this.data = data;
        return this;
    }

    public ApiResult<T> warning(String msg) {
        this.code = CODE_WARNING;
        this.msg = msg;
        return this;
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
