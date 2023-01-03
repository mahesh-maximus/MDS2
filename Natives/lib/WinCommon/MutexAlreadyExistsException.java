
public class MutexAlreadyExistsException extends Exception {



    public MutexAlreadyExistsException(String mutexName) {
        super(mutexName);
    }



}