package processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class TextProcessor implements ItemProcessor<String, String> {

    private static final int CEASER_CYPHER_SHIFT_KEY = 5;
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private static final Logger logger = LoggerFactory.getLogger(TextProcessor.class);

    @Override
	public String process(String text) throws Exception {
		text = text.toLowerCase();
		String cipherText = "";
		
		for (int i = 0; i < text.length(); i++) {
			char charVal = text.charAt(i);
			char replaceVal = charVal;
			if(charVal != '\t') {
				int charPosition = ALPHABET.indexOf(charVal);
				int keyVal = (CEASER_CYPHER_SHIFT_KEY + charPosition) % 26;
				 replaceVal = ALPHABET.charAt(keyVal);
			}
			cipherText += replaceVal;
		}
		
		logger.info("Input " + text + " Output " + cipherText);
		return cipherText;
	}

}
