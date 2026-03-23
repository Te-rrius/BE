package hansung.org.terrius.global.response.code;

public interface BaseResponseCode
{
    String getCode();

    int getHttpStatus();

    String getMessage();
}
