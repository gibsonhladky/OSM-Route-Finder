package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class W3CDocumentIntegrationTest {

	File testFile = new File("document_test.xml");
	Node root;
	
	@Before
	public void setUp() throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    Document document = docBuilder.parse(testFile);
	    root = document.getFirstChild();
	}

	@Test
	public void getChildrenByTag() {
		List<Node> children = getChildrenWithTag("street", root);
		
		assertEquals(3, children.size());
	}
	
	private List<Node> getChildrenWithTag(String tag, Node parent) {
		List<Node> childrenWithTag = new ArrayList<Node>();
		NodeList children = parent.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeName().equals(tag)) {
				childrenWithTag.add(child);
			}
		}
		return childrenWithTag;
	}
	
	@Test
	public void getAttributesOfNode() {
		Node node = getChildrenWithTag("attribute_check", root).get(0);
		assertEquals("result1", getAttributeValue("test1", node));
	}
	
	private String getAttributeValue(String attributeName, Node node) {
		NamedNodeMap attributes = node.getAttributes();
		return attributes.getNamedItem(attributeName).getNodeValue();
	}

}
