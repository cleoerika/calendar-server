package com.hcl.experiment.starter;

import java.util.function.Consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  
	  HttpServer server = vertx.createHttpServer();
	  
	  Router router = Router.router(vertx);
	  
	  
	  router.route("/").handler(ctx -> {
		  ctx.response().putHeader("content-type", "text/plain").end("im root");
	  });
	  router.route(HttpMethod.OPTIONS, "/Hello").handler(ctx ->{
		  ctx.response().putHeader("tango", "42");
		  ctx.next();
	  });
	  router.route("/Hello").handler(this::helloHandler);
	  router.route(HttpMethod.OTHER, "/calendar/:calendarname").handler(ctx -> {
		  ctx.response()
		  	.setStatusCode(207)
		  	.setStatusMessage("Multi-status")
		  	.putHeader("Content-Type", "application/xml")
		  	.end(this.propfindResult(ctx));
	  });
	  
	  server.requestHandler(router).listen(8888,http-> {
		  if (http.succeeded()) {
		        startPromise.complete();
		        System.out.println("HTTP server started on port 8888");
		      } else {
		        startPromise.fail(http.cause());
		      }
	  });
	  
	  
	  
   /* vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "application/json")
        .end("{\"message\":\"Hello from Vert.x!\"}");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });*/
  }
  
	private String propfindResult(RoutingContext ctx) {
		String calendarName = ctx.request().getParam("calendarname");

		StringBuilder b = new StringBuilder();

		b.append("<d:multistatus xmlns:d=\"DAV:\" xmlns:cs=\"http://calendarserver.org/ns/\">");
		b.append("  <d:response>\n");
		b.append("    <d:href>/calendars/");
		b.append(calendarName);
		b.append("</d:href>\n");
		b.append("<d:propstat>\n");
		b.append("<d:prop>\n");
		b.append("<d:displayname>Home calendar</d:displayname>\n");
		b.append("               <cs:getctag>3145</cs:getctag>\n");
		b.append("            </d:prop>\n");
		b.append("            <d:status>HTTP/1.1 200 OK</d:status>\n");
		b.append("        </d:propstat>\n");
		b.append("    </d:response>\n");
		b.append("</d:multistatus>\n");

		return b.toString();
	}

private void helloHandler(RoutingContext ctx) {
	  
	  ctx.response()/*.putHeader("content-type", "application/json")*/.end("{\"message\":\"Hello from Vert.x!\"}");
  }
  
  public static void main(final String[] args) {
	  
	  MainVerticle main = new MainVerticle();
	  MainVerticle.runVerticle(main);
	  
  }
  
  private static void runVerticle(MainVerticle main) {
	  
	  final Consumer<Vertx> runner = vertx -> {
		  vertx.deployVerticle(main, res -> {
			  if (res.succeeded()) {
				  System.out.println("Hooray!!");
			  }
		  });
		  
	  };
	  
	  VertxOptions options = new VertxOptions();
	  options.setBlockedThreadCheckInterval(360000);
	  
	  final Vertx vertx = Vertx.vertx(options);
	  runner.accept(vertx);
	  
	  
	  
  }
  
}
