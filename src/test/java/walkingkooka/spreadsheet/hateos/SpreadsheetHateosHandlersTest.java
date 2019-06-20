/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package walkingkooka.spreadsheet.hateos;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.server.FakeHttpRequest;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.net.http.server.HttpResponses;
import walkingkooka.net.http.server.RecordingHttpResponse;
import walkingkooka.net.http.server.hateos.HateosContentType;
import walkingkooka.routing.Router;
import walkingkooka.spreadsheet.engine.SpreadsheetEngine;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContext;
import walkingkooka.spreadsheet.engine.SpreadsheetEngineContexts;
import walkingkooka.spreadsheet.engine.SpreadsheetEngines;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetHateosHandlersTest implements ClassTesting2<SpreadsheetHateosHandlers>,
        PublicStaticHelperTesting<SpreadsheetHateosHandlers> {

    // router...........................................................................................................

    @Test
    public void testRouterBaseNullFails() {
        this.routerFails(null, this.contentType(), this.engine(), this.engineContext());
    }

    @Test
    public void testRouterContentTypeNullFails() {
        this.routerFails(this.base(), null, this.engine(), this.engineContext());
    }

    @Test
    public void testRouterEngineNullFails() {
        this.routerFails(this.base(), this.contentType(), null, this.engineContext());
    }

    @Test
    public void testRouterEngineContextNullFails() {
        this.routerFails(this.base(), this.contentType(), this.engine(), null);
    }

    private void routerFails(final AbsoluteUrl base,
                             final HateosContentType<JsonNode> contentType,
                             final SpreadsheetEngine engine,
                             final Supplier<SpreadsheetEngineContext> context) {
        assertThrows(NullPointerException.class, () -> {
            SpreadsheetHateosHandlers.router(base, contentType, engine, context);
        });
    }

    // cell.............................................................................................................

    @Test
    public void testRouteCellGetLoadCell() {
        this.routeAndCheck(HttpMethod.GET, URL + "/cell/A1");
    }

    @Test
    public void testRouteCellPostSaveCell() {
        this.routeAndCheck(HttpMethod.POST, URL + "/cell/A1");
    }

    @Test
    public void testRouteCellPutFails() {
        this.routeAndFail(HttpMethod.PUT, URL + "/cell/A1");
    }

    @Test
    public void testRouteCellDelete() {
        this.routeAndFail(HttpMethod.DELETE, URL + "/cell/A1");
    }

    // column...........................................................................................................

    @Test
    public void testRouteColumnsGetFails() {
        this.routeAndFail(HttpMethod.GET, URL + "/column/A");
    }

    @Test
    public void testRouteColumnsPost() {
        this.routeAndCheck(HttpMethod.POST, URL + "/column/A");
    }

    @Test
    public void testRouteColumnsPutFails() {
        this.routeAndFail(HttpMethod.PUT, URL + "/column/A");
    }

    @Test
    public void testRouteColumnsDelete() {
        this.routeAndCheck(HttpMethod.DELETE, URL + "/column/A");
    }

    // row..............................................................................................................

    @Test
    public void testRouteRowsGetFails() {
        this.routeAndFail(HttpMethod.GET, URL + "/row/1");
    }

    @Test
    public void testRouteRowsPost() {
        this.routeAndCheck(HttpMethod.POST, URL + "/row/1");
    }

    @Test
    public void testRouteRowsPutFails() {
        this.routeAndFail(HttpMethod.PUT, URL + "/row/1");
    }

    @Test
    public void testRouteRowsDelete() {
        this.routeAndCheck(HttpMethod.DELETE, URL + "/row/1");
    }

    // copycells........................................................................................................

    @Test
    public void testRouteCopyCellsGetFails() {
        this.routeAndFail(HttpMethod.GET, URL + "/cell/A1:B2/copy");
    }

    @Test
    public void testRouteCopyCellsPost() {
        this.routeAndCheck(HttpMethod.POST, URL + "/cell/A1:B2/copy");
    }

    @Test
    public void testRouteCopyCellsPutFails() {
        this.routeAndFail(HttpMethod.PUT, URL + "/cell/A1:B2/copy");
    }

    @Test
    public void testRouteCopyCellsDeleteFails() {
        this.routeAndFail(HttpMethod.DELETE, URL + "/cell/A1:B2/copy");
    }

    private void routeAndCheck(final HttpMethod method,
                               final String url) {
        final HttpRequest request = this.request(method, url);
        final Optional<BiConsumer<HttpRequest, HttpResponse>> possible = this.route(request);
        assertNotEquals(Optional.empty(), possible);
        if(possible.isPresent()) {
            final RecordingHttpResponse response = HttpResponses.recording();
            possible.get().accept(request, response);
            assertEquals(HttpStatusCode.NOT_IMPLEMENTED,
                    response.status().map(s -> s.value()).orElse(null),
                    () -> "status " + request + " " + response + "\n" + possible);
        }
    }

    private Optional<BiConsumer<HttpRequest, HttpResponse>> route(final HttpRequest request) {
        final Router<HttpRequestAttribute<?>, BiConsumer<HttpRequest, HttpResponse>> router = SpreadsheetHateosHandlers.router(this.base(),
                this.contentType(),
                this.engine(),
                this.engineContext());
        return router.route(request.routingParameters());
    }

    private HttpRequest request(final HttpMethod method,
                                final String url) {
        return new FakeHttpRequest() {

            @Override
            public RelativeUrl url() {
                return Url.parseAbsolute(url).relativeUrl();
            }

            @Override
            public HttpMethod method() {
                return method;
            }
        };
    }

    private void routeAndFail(final HttpMethod method,
                              final String url) {
        final HttpRequest request = this.request(method, url);
        final Optional<BiConsumer<HttpRequest, HttpResponse>> possible = this.route(request);
        if(possible.isPresent()) {
            final RecordingHttpResponse response = HttpResponses.recording();
            possible.get().accept(request, response);
            assertEquals(HttpStatusCode.METHOD_NOT_ALLOWED,
                    response.status().map(s -> s.value()).orElse(null),
                    () -> "status " + request + " " + response + "\n" + possible);
        }
    }

    private final static String URL = "http://example.com/api/";

    private AbsoluteUrl base() {
        return Url.parseAbsolute(URL);
    }

    private HateosContentType<JsonNode> contentType() {
        return HateosContentType.json();
    }

    private SpreadsheetEngine engine() {
        return SpreadsheetEngines.fake();
    }

    private Supplier<SpreadsheetEngineContext> engineContext() {
        return () -> SpreadsheetEngineContexts.fake();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetHateosHandlers> type() {
        return SpreadsheetHateosHandlers.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
