package pl.edu.uj.portfel.transaction;

public class TransactionListItem {
	private String name;
	private long id;
	private long amount;
	
	public void setName(String s) { name = s; }
	public void setId(long i) { id = i; }
	public String getName() { return name; }
	public long getId() { return id; }
	public void setAmount(long l) { amount = l; }
	public long getAmount() { return amount; }
}
