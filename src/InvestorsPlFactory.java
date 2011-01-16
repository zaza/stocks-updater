public class InvestorsPlFactory {

	public static String[] createKeys(String string) {
		if (string.equalsIgnoreCase("invfiz"))
			return new String[] { "investor-fiz", "invfiz" };
		if (string.equalsIgnoreCase("invgld"))
			return new String[] { "investor-gold-fiz", "invgld" };			
		if (string.equalsIgnoreCase("invpr"))
			return new String[] { "investor-property-fiz", "invpr" };
		if (string.equalsIgnoreCase("invpe"))
			return new String[] { "investor-pe-fiz", "invpe" };				
		if (string.equalsIgnoreCase("invcee"))
			return new String[] { "investor-cee-fiz", "invcee" };			
		return null;
	}

}
