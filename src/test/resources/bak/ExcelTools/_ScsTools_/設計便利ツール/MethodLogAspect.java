import murata.co.log.LoggableLoggingAspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author T.Ueda
 * @version $Id$
 * @since JDK5.0
 */
@Aspect
public class MethodLogAspect {

    /** Log instance*/
    protected static org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory
            .getLog(LoggableLoggingAspect.class);

    @Around("execution(* murata.co.editor.Editor+.*(..)) || execution(* murata.co.bsp.BaseBSP+.*(..))")
    public Object debugAdvice(ProceedingJoinPoint pjp) {

        System.out.printf("***** START ***** %s \n", pjp.getSignature());
        for (Object arg : pjp.getArgs())
            System.out.printf("***** Åy%1sÅz :%2s \n", arg.getClass()
                .getSimpleName(), arg);

        try {

            Object obj = pjp.proceed();

            System.out.printf("***** RETURN ***** %s \n", pjp.getSignature());
            System.out.printf("***** %1s \n", obj);

            return obj;
        } catch (Throwable e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
