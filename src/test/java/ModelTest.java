import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import com.gwtt.simulator.netconf.model.hello.Capabilities;
import com.gwtt.simulator.netconf.model.hello.Hello;
import com.gwtt.simulator.netconf.model.rpc.Rpc;
import com.gwtt.simulator.netconf.utils.XstreamException;
import com.gwtt.simulator.netconf.utils.XstreamUtil;

public class ModelTest {

	public static void main(String[] args) throws XstreamException {
		List<String> capabilityList = new ArrayList<>();
		capabilityList.add("urn:ietf:params:netconf:base:1.0");
		capabilityList.add("urn:ietf:params:netconf:base:1.1");

		Capabilities capabilities = new Capabilities();
		capabilities.setCapability(capabilityList);

		Hello hello = new Hello();
		hello.setCapabilities(capabilities);

		String xml = XstreamUtil.toXML(hello);
		System.out.println(xml);

		Hello h = XstreamUtil.fromXML(xml, Hello.class);
		System.out.println(h);

		xml = "<rpc xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\" message-id=\"7\">\r\n" + "    <get>\r\n"
				+ "  <filter type=\"subtree\">\r\n"
				+ "    <components xmlns=\"http://openconfig.net/yang/platform\">\r\n" + "      <component>\r\n"
				+ "        <name>demo</name>\r\n" + "        <chassis/>\r\n" + "      </component>\r\n"
				+ "    </components>\r\n" + "  </filter>\r\n" + "</get>\r\n" + "</rpc>";

		Rpc rpc = XstreamUtil.fromXML(xml, Rpc.class);
		System.out.println(rpc);

		
	}

}
