package pl.edu.uj.portfel.utils;


public class StringUtils {
	public static String reverse(String s) {
		StringBuffer sb = new StringBuffer();
		
		for(int idx = s.length(); idx > 0; idx--)
			sb.append(s.charAt(idx - 1));
		
		return sb.toString();
	}
	
	public static String getIntegral(String source) {
		int idx = source.indexOf('.');
		
		if(idx == -1)
			idx = source.indexOf(',');
		
		if(idx == -1)
			return source;
		
		return source.substring(0, idx);
	}
	
	public static String getFraction(String source) {
		int idx = source.indexOf('.');
		
		if(idx == -1)
			idx = source.indexOf(',');
		
		if(idx == -1)
			return "0";
		
		String candidate = source.substring(idx + 1);
		
		if(candidate.length() == 1)
			return candidate + "0";
		
		return candidate;
	}
	
	public static String commize(double number, char numSeparator, char separator) {
		StringBuffer sb = new StringBuffer();
		
		String original = Double.toString(number), integral = getIntegral(original), fraction = getFraction(original);
		
		integral = reverse(integral);
		for(int i = 0; i < integral.length(); i++) {
			sb.append(integral.charAt(i));
			
			if(0 == (1 + i) % 3 && i + 1 != integral.length())
				sb.append(numSeparator);
		}
		
		integral = reverse(sb.toString());
		sb.setLength(0);
		
		sb.append(integral);
		sb.append(separator);
		sb.append(fraction);
		
		return sb.toString();
	}
	
	public static String commize(long number, char numSeparator, char separator) {
		StringBuffer sb = new StringBuffer();
		
		String original = Long.toString(number);
		
		original = reverse(original);
		for(int i = 0; i < original.length(); i++) {
			sb.append(original.charAt(i));
			
			if(0 == (1 + i) % 3 && i + 1 != original.length())
				sb.append(numSeparator);
		}
		
		original = reverse(sb.toString());
		return original;
	}
	
	public static String getCanonicalCashValue(long cash, Currency currency) {
		long fraction = cash % 100;
		long value = cash / 100;
		
		String prefix = "", suffix = "";
		String prefix2 = "", suffix2 = "";

		if(currency.isPrefix()) {
			prefix = currency.getLocalizedSymbol();
			prefix2 = currency.getLocalizedFractionSymbol();
		} else if(currency.isSuffix()) {
			suffix = currency.getLocalizedSymbol();
			suffix2 = currency.getLocalizedFractionSymbol();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%s%d%s", prefix, value, suffix));
		if(fraction > 0)
			sb.append(String.format(" %s%d%s", prefix2, fraction, suffix2));
		
		return sb.toString();
	}
}
