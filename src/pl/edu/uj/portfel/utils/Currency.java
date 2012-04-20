package pl.edu.uj.portfel.utils;

public class Currency {
	public String getLocalizedSymbol() {
		return "zł";
	}
	
	public String getLocalizedFractionSymbol() {
		return "gr";
	}
	
	public String getOfficialSymbol() {
		return "PLN";
	}
	
	public boolean isPrefix() { return false; }
	public boolean isSuffix() { return true; }
}
