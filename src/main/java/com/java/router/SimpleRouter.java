package com.java.router;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import com.java.models.User;

public class SimpleRouter extends RouteBuilder{
	@Override
	public void configure() throws Exception {
		restConfiguration("restlet").bindingMode(RestBindingMode.json)
				.dataFormatProperty("prettyPrint", "true")
				.contextPath("/").host("localhost").port("8081")
				.apiContextPath("/api")
					.apiProperty("api.title", "User API")
					.apiProperty("api.version", "1.2.3")
					.apiProperty("cors", "true");
		
		rest("/rest-service")
			.consumes("application/json").produces("application/json")
			.get("/user/{id}").outType(User.class)
				.param().name("id").type(RestParamType.path).endParam()
				.to("direct:getUser")
			.post("/createUser").type(User.class)
				.param().name("body").type(RestParamType.body).endParam()
				.to("bean:userRepo?method=createPerson")
			.delete("/user/{id}").outType(User.class)
				.param().name("id").type(RestParamType.path).endParam()
				.to("direct:deleteUser")
			.put("/user/{id}").type(User.class)
				.param().name("body").type(RestParamType.body).endParam()
				.to("bean:userRepo?method=update")
			.get("/getAllUser").outType(User[].class)
				.to("bean:userRepo?method=retrieveAll");
		
		from("direct:deleteUser")
			.to("direct:getUser")
			.bean("userRepo", "delete(${header.id})");
		
		from("direct:getUser")
			.bean("userRepo", "findById(${header.id})")
			.choice()
				.when(simple("${body} == null"))
					.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
					.transform().constant("User not found!!!");
	}

}
