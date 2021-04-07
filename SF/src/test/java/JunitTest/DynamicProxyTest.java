package JunitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.config.AdvisorComponentDefinition;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import com.tody.SF.common.dao.Interface.Hello;
import com.tody.SF.common.service.HelloTarget;
import com.tody.SF.common.service.HelloUppercase;
import com.tody.SF.common.service.UppercaseHandler;

public class DynamicProxyTest {


	@Test
	public void simpleProxy() {
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader()
				, new Class[] {Hello.class}
				, new UppercaseHandler(new HelloTarget()));
		//assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		//assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		//assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
		
		Hello proxiHello = new HelloUppercase(new HelloTarget());
		assertThat(proxiHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
	
	@Test
	public void ProxyTest() {
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader()
			   ,new Class[] {Hello.class}
			   ,new UppercaseHandler(new HelloTarget()));
		
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
	
	@Test
	public void ProxyFactoryBeanTest() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		pfBean.addAdvice(new UppercaseAdvice());
		
		Hello proxiedHello = (Hello)pfBean.getObject();
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
	
	static class UppercaseAdvice implements MethodInterceptor{

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();//타깃오브젝트를 전달할 필요가 없다.
			return ret.toUpperCase();
		}
		
	}
	
	@Test
	public void pointcutAdvisorTest() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
		NameMatchMethodPointcut pointcut =new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");//sayH로 시작하는 모든메소드선택
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxideHello = (Hello)pfBean.getObject();
		
		assertThat(proxideHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxideHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxideHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
	
	@Test
	public void classNamePintcutAdvisor() {
		
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			public ClassFilter getClassFilter() {
				return new ClassFilter() {
					@Override
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		classMethodPointcut.setMappedName("sayH*");
		
		checkAdvicde(new HelloTarget(), classMethodPointcut, true);
		
		class HelloWord extends HelloTarget{};
		checkAdvicde(new HelloWord(), classMethodPointcut, false);
		class HelloToby extends HelloTarget{};
		checkAdvicde(new HelloToby(), classMethodPointcut, true);
		
	
	}
	
	private void checkAdvicde(Object target, Pointcut pointcut, boolean advicde) { 
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		
		Hello proxideHello = (Hello)pfBean.getObject();
		
		if(advicde) {
			assertThat(proxideHello.sayHello("Toby"), is("HELLO TOBY"));
			assertThat(proxideHello.sayHi("Toby"), is("HI TOBY"));
			assertThat(proxideHello.sayThankYou("Toby"), is("THANK YOU TOBY")); 	
		}
		else {
			assertThat(proxideHello.sayHello("Toby"), is("Hello Toby"));
			assertThat(proxideHello.sayHi("Toby"), is("Hi Toby"));
			assertThat(proxideHello.sayThankYou("Toby"), is("Thank You Toby")); 
			
		}
		
		
		
	}
}
