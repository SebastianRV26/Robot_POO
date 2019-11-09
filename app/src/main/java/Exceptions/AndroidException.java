package Exceptions;

public class AndroidException extends Exception{

    String message;

    public AndroidException (String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
