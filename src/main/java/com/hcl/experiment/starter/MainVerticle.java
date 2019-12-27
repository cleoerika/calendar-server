package com.hcl.experiment.starter;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.providers.GithubAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

	private Map<Buffer, CalendarInfo> readingList = new LinkedHashMap<Buffer, CalendarInfo>();
	
	// you should never store these in code,
	// these are your github application credentials
	private static final String CLIENT_ID = "590226577088-9lpbenkeg63tn6evmfcfsm1hp58bahp8.apps.googleusercontent.com";
	private static final String CLIENT_SECRET = "ZggQCCi4beB_RohNg4RBq5Pe";

	@Override
	public void start(Promise<Void> startPromise) throws Exception {

		HttpServer server = vertx.createHttpServer(this.getServerOptions());

		Router router = Router.router(vertx);

//		// Simple auth service which uses a GitHub to authenticate the user
//		OAuth2Auth authProvider = GithubAuth.create(vertx, CLIENT_ID, CLIENT_SECRET);
//		// We need a user session handler too to make sure the user is stored in the
//		// session between requests
//		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).setAuthProvider(authProvider));
//
//		OAuth2Auth oauth2 = OAuth2Auth.create(vertx, new OAuth2ClientOptions().setClientID(CLIENT_ID)
//				.setClientSecret(CLIENT_SECRET).setSite("https://www.googleapis.com/auth/calendar/"));
//		System.out.println("AUTH" + oauth2.toString());
		
		router.route(HttpMethod.GET, "/caldav/v2/:calendarname/events").handler(this::getCalEvents);
		router.route(HttpMethod.POST, "/caldav/v2/:calendarname/events/:eventid").handler(this::createCalEvent);
		router.route(HttpMethod.PUT, "/caldav/v2/:calendarname/events/:eventid").handler(this::updateCalEvent);
		router.route(HttpMethod.DELETE, "/caldav/v2/:calendarname/events/:eventid").handler(this::deleteCalEvent);
		router.route(HttpMethod.OTHER, "/caldav/v2/:calendarname/events").handler(this::reportCalEvent);
		router.route(HttpMethod.OTHER, "/caldav/v2/:calendarname/events").handler(this::propCalEvent);
		router.route(HttpMethod.CONNECT, "/").handler(this::connectHandler);

		server.requestHandler(router).listen(8888, http -> {
			if (http.succeeded()) {
				startPromise.complete();
//				this.logger.debug("Starting " + this.getClass().getName());
				System.out.println("HTTPS server started on port 8888");
				LOGGER.info("HTTPS server started on port 8888");
			} else {
				System.out.println("Shit happened");
				startPromise.fail(http.cause());
			}
		});

	}

	private HttpServerOptions getServerOptions() {
		final HttpServerOptions result = new HttpServerOptions();
		final String tlsFile = "/Users/cleoerikasoriano/Documents/calendar-server/frascati.projectkeep.io.pfx";
		final String tlsPasswords = System.getenv("TLSPassword");
		result.setSsl(true);
		final PfxOptions pfx = new PfxOptions().setPath(tlsFile).setPassword(tlsPasswords);
		result.setPfxKeyCertOptions(pfx).setUseAlpn(false).addEnabledSecureTransportProtocol("TLSv1.3");
		// .removeEnabledSecureTransportProtocol("TLSv1").removeEnabledSecureTransportProtocol("SSLv2Hello");
		// .setOpenSslEngineOptions(new OpenSSLEngineOptions());

		return result;
	}

	private void connectHandler(RoutingContext ctx) {
		ctx.response().setStatusCode(200).end("Logged IN");
	}

	// TODO: transfer to other class
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

	// TODO: getAll, getByID
	private void getCalEvents(final RoutingContext ctx) {
		System.out.println("enter get method");
//		CalendarInfo article = ctx.getBody().getBodyAsJson().mapTo(CalendarInfo.class);
//	    readingList.put(article.getId(), article);
		
		ctx.request().handler(new Handler<Buffer>() {

			@Override
			public void handle(Buffer event) {
				// TODO Auto-generated method stub
				ctx.response().putHeader("Content-Type", "text/calendar");
				ctx.response().setStatusCode(200);
				ctx.response().setChunked(true);
				
				System.out.println("test : \n" + event);
				ctx.response().end(event);
			}
			
		});
		
		
		

	}

	private void createCalEvent(final RoutingContext ctx) {

		if (ctx.request().method() == HttpMethod.POST) {
			System.out.println("HttpMethod.POST");

			ctx.request().handler(new Handler<Buffer>() {

				@Override
				public void handle(Buffer event) {

					ctx.response().setChunked(true);
					ctx.response().putHeader("Content-Type", "text/calendar");
					ctx.response().setStatusCode(201);

					System.out.println("result : \n" + event);

					ctx.response().end(event);
				}
			});
		}
	}

	private String createResult(RoutingContext ctx) {
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

	// header: if-match: etag from report method
	// TODO: fix update
	private void updateCalEvent(final RoutingContext ctx) {

		String eventid = ctx.request().getParam("eventid");

		if ((eventid != null) && (!"".equals(eventid))) {
			ctx.response().putHeader("Content-Type", "text/xml");
			ctx.response().setStatusCode(201);
			ctx.response().setStatusMessage("Updated");
			// ctx.response().end(this.propfindResult(ctx));//TODO
		} /*
			 * else { ctx.response().putHeader("Content-Type", "text/plain");
			 * ctx.response().setStatusCode(404);
			 * ctx.response().end("Event ID doesn't exist or empty"); }
			 */
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

		ctx.response().putHeader("Content-Type", "text/xml");
//		ctx.response().setChunked(true);
		ctx.response().setStatusCode(207);
		ctx.response().end("reportCalEvent");
//		ctx.response().end(this.getDummyResult("/reportAll.ics"));
	}

	private void propCalEvent(final RoutingContext ctx) {
		ctx.response().setStatusCode(207);
		ctx.response().headers().add("Content-Type", "text/xml").add("Depth", "0");
//		ctx.response().setChunked(true);
		ctx.response().end("propCalEvent");
//		ctx.response().end(this.getDummyResult("/propfind.xml"));
//		ctx.response().end(createResult(ctx));

		System.out.println("URI : " + ctx.request().uri());
		System.out.println("PATH : " + ctx.request().path());

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
