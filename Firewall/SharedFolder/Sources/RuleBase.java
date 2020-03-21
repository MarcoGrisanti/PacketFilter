import java.util.*;

public class RuleBase {

	private List<BlockPayloadRule> dangerousPayloads = new ArrayList<BlockPayloadRule>(Arrays.asList( new BlockPayloadRule("11100011"),
																					new BlockPayloadRule("10010110"),
																					new BlockPayloadRule("11111100")));

	private List<BlockPayloadRule> sensiblePayloads = new ArrayList<BlockPayloadRule>(Arrays.asList(  new BlockPayloadRule("11111111"),
																					new BlockPayloadRule("00000000"),
																					new BlockPayloadRule("11100111"),
																					new BlockPayloadRule("00011000"),
																					new BlockPayloadRule("10101010"),
																					new BlockPayloadRule("01010101"),
																					new BlockPayloadRule("11110000"),
																					new BlockPayloadRule("00001111")));

	private List<BlockIPRule> blockedIPs = new ArrayList<BlockIPRule>(Arrays.asList(new BlockIPRule("/10.0.0.5")));

	public boolean checkRequest(String ip) {
		for (BlockIPRule r: blockedIPs) if (r.check(ip)) return false;
		return true;
	}

	public boolean checkRequest(String ip, String payload) {
		for (BlockPayloadRule r: dangerousPayloads) {
			if (r.check(payload)) {
				blockedIPs.add(new BlockIPRule(ip));
				return false;
			}
		}
		return true;
	}

	public boolean checkResponse(String payload) {
		for (BlockPayloadRule r: sensiblePayloads) {
			if (r.check(payload)) {
				return false;
			}
		}
		return true;
	}

}