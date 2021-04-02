package JunitTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.tody.SF.Calculator;

public class CalcSumTest {
	Calculator calculator;
	String numFilePath;
	@Before
	public void setUp() {
		this.calculator = new Calculator();
		this.numFilePath = getClass().getResource("numbers.txt").getPath();
	}
	
	@Test
	public void sumOfNumbers() throws IOException {		
		assertThat(calculator.calcSum(this.numFilePath), is(10));
	}
	
	@Test
	public void multyplyOfNumbers() throws IOException {		
		assertThat(calculator.calcMultiply(this.numFilePath), is(24));
	}
	@Test
	public void concateateStrings() throws IOException {		
		assertThat(calculator.concatenate(this.numFilePath), is("1234"));
	}
}
