package JunitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import com.tody.SF.pointcut.Bean;
import com.tody.SF.pointcut.Target;



public class PointCutTest {
	@Test
	public void methodSingnaturePointcut() throws SecurityException, NoSuchMethodException{
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		
		pointcut.setExpression("execution(public int " +
		"com.tody.SF.pointcut.Target.minus(int,int) " +
		"throws java.lang.RuntimeException)");
		//pointcut.setExpression("execution(int minus(int ,int)");
		
		assertThat(pointcut.getClassFilter().matches(Target.class) && pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null), is(true));
		
		assertThat(pointcut.getClassFilter().matches(Target.class) && pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null), is(false));
		
		assertThat(pointcut.getClassFilter().matches(Bean.class) && pointcut.getMethodMatcher().matches(Target.class.getMethod("method", int.class, int.class), null), is(false));
	}
}
