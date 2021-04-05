package JunitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Proxy;

import org.junit.Test;

import com.tody.SF.common.dao.Interface.Hello;
import com.tody.SF.common.service.HelloTarget;
import com.tody.SF.common.service.HelloUppercase;
import com.tody.SF.common.service.UppercaseHandler;

public class simpleProxyTest {


	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
		
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
}
