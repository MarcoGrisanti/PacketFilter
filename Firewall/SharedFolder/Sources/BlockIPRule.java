public class BlockIPRule implements Rule {

	private String ip;

	public BlockIPRule(String ip) {
		this.ip = ip;
	}

	public boolean check(String toCheck) {
		if (ip.equals(toCheck)) return true;
		return false;
	}

}