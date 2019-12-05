package com.hcl.experiment.starter;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClientOptions;
//import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

	
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  
	  HttpServer server = vertx.createHttpServer();

	  Router router = Router.router(vertx);
	  
	  router.route(HttpMethod.GET, "/caldav/v2/:calendarname/events").handler(this::getCalEvents);
	  router.route(HttpMethod.POST, "/caldav/v2/:calendarname/events/:eventid").handler(this::createCalEvent);
	  router.route(HttpMethod.PUT, "/caldav/v2/:calendarname/events/:eventid").handler(this::updateCalEvent);
	  router.route(HttpMethod.DELETE, "/caldav/v2/:calendarname/events/:eventid").handler(this::deleteCalEvent);
	  router.route(HttpMethod.OTHER, "/caldav/v2/:calendarname/events").handler(this::reportCalEvent);
	  router.route(HttpMethod.OTHER, "/caldav/v2/:calendarname/events").handler(this::propCalEvent);
	  

	  server.requestHandler(router).listen(8888,http -> {
		  if (http.succeeded()) {
		        startPromise.complete();
		        System.out.println("HTTP server started on port 8888");
		      } else {
		        startPromise.fail(http.cause());
		      }
	  });
	  
  }
  
  
	private HttpClientOptions HttpClientOptions() {
		// TODO Auto-generated method stub
		return null;
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
	
	//TODO: getAll, getByID
	private void getCalEvents(final RoutingContext ctx) {
		ctx.response().putHeader("Content-Type", "text/calendar");
		ctx.response().end(this.getDummyResult("/calendar.ics"));
	}
	
	
	private void createCalEvent(final RoutingContext ctx) {
		
		ctx.response().putHeader("Content-Type", "text/html");
		ctx.response().setStatusCode(201);
//		ctx.response().end(this.createResult(ctx));
		ctx.response().end("this is a create test");
		
	}
	
	private String createResult(final RoutingContext ctx) {

		StringBuilder b = new StringBuilder();

		b.append("<html><body><h1>");
		b.append("test");
		b.append("    </h1></body></html>");
		
		return b.toString();
	}	
	
	// header: if-match: etag from report method
	private void updateCalEvent(final RoutingContext ctx) {
		
		String eventid = ctx.request().getParam("eventid");
		
		if ((eventid != null) && (!"".equals(eventid))) {
			ctx.response().putHeader("Content-Type", "text/html");
			ctx.response().setStatusCode(201);
			ctx.response().setStatusMessage("Updated");
			ctx.response().end(this.propfindResult(ctx));//TODO
		} /*else {
			ctx.response().putHeader("Content-Type", "text/plain");
			ctx.response().setStatusCode(404);
			ctx.response().end("Event ID doesn't exist or empty");
		}*/
	}

	private void deleteCalEvent(final RoutingContext ctx) {

		String eventID = ctx.request().getParam("eventid");

		if ((eventID != null) && (!"".equals(eventID))) {
			ctx.response().setStatusCode(204);
			ctx.response().setStatusMessage("Deleted");
			ctx.response().end();
		}

	}
	
	private void reportCalEvent(final RoutingContext ctx) {
		ctx.response().putHeader("Content-Type", "application/xml");
		ctx.response().setStatusCode(207);
		ctx.response().end(this.getDummyResult("/reportAll.ics"));
	}
	
	private void propCalEvent(final RoutingContext ctx) {
		ctx.response().putHeader("Content-Type", "application/xml");
		ctx.response().setStatusCode(207);
		ctx.response().end(this.getDummyResult("/propfind.xml"));
	}
	
	
	private String getDummyResult(String resourceName) {
		
//		final CharSource charSource = Resources.asCharSource(
//                Resources.getResource( resourceName ), Charsets.UTF_8
//        );
//
//        final StringBuilder stringBuilder = new StringBuilder();
//        try {
//			charSource.copyTo( stringBuilder );
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        return stringBuilder.toString();
		
		String result = "shit happened: ";
		final InputStream in = this.getClass().getResourceAsStream(resourceName);
		
		if (in == null) {
			return result;
			
		}

		ByteSource byteSource = new ByteSource() {
			
			@Override
			public InputStream openStream() throws IOException {

				return in;
			}
		};
		
		try {
			result = byteSource.asCharSource(Charsets.UTF_8).read();
		} catch (IOException e) {
			result += e.getMessage();
			e.printStackTrace();
		}
		return result;
	}


	private void helloHandler(RoutingContext ctx) {

		ctx.response()/* .putHeader("content-type", "application/json") */.end("{\"message\":\"Hello from Vert.x!\"}");

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
