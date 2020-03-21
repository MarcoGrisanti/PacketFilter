public class BlockPayloadRule implements Rule {

	private String payload;

	public BlockPayloadRule(String payload) {
		this.payload = payload;
	}

	public boolean check(String toCheck) {
		if (payload.equals(toCheck)) return true;
		return false;
	}

}