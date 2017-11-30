package io.coinshell.exception.codes;

public enum ExceptionCode {

    NONE(0),
    UNKNOWN(1),
    SERVICE_EXCEPTION(100);




    int code;

    private ExceptionCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }


    public static ExceptionCode fromCode(int code) {

        for (ExceptionCode p : ExceptionCode.values()) {
            if (code == p.code) {
                return p;
            }
        }

        return null;
    }


}
