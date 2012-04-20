package pl.edu.uj.portfel.test;

import pl.edu.uj.portfel.utils.StringUtils;

public class StringUtilsTest extends TestNode {

	@Override
	public boolean run() {
		if(! StringUtils.reverse("12345").equals("54321"))
			return false;

		if(! StringUtils.reverse("1").equals("1"))
			return false;
		
		if(! StringUtils.reverse("12").equals("21"))
			return false;
		
		//
		
		assertEquals("1 234 567,01", StringUtils.commize(1234567.01, ' ', ',')); 
		assertEquals("1,00", StringUtils.commize(1., ' ', ',')); 
		assertEquals("1,01", StringUtils.commize(1.01, ' ', ','));
		assertEquals("1,10", StringUtils.commize(1.1, ' ', ','));
		assertEquals("123,10", StringUtils.commize(123.1, ' ', ','));
		assertEquals("1 234,10", StringUtils.commize(1234.1, ' ', ','));
		
		return true;
	}
}
