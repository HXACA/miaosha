package com.xliu.miaosha.exception;

import com.xliu.miaosha.result.CodeMsg;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/10 10:28
 */
public class GlobleException extends RuntimeException {

    private CodeMsg codeMsg;

    public GlobleException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}
