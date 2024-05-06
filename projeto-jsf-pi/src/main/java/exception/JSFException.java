package exception;

public class JSFException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   public JSFException(String msg)
   {
      super(msg);
   }

   public JSFException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
