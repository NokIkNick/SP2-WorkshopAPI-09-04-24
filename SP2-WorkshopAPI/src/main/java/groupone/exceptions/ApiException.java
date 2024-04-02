package groupone.exceptions;


public class ApiException extends Throwable{
    public ApiException(int code, String msg){
        super(msg+", "+code);
    }


}
