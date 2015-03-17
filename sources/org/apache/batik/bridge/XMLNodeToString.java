package org.apache.batik.bridge;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

/**
 * CRP: Class added for debugging purposes only.
 */
public class XMLNodeToString {

	public static String convertToString(Element node){
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(node),
			      new StreamResult(buffer));
			String str = buffer.toString();
			return str;
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
