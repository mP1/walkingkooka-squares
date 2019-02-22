package walkingkooka.spreadsheet.hateos;

import walkingkooka.net.http.server.hateos.HateosGetHandler;
import walkingkooka.net.http.server.hateos.HateosHandler;
import walkingkooka.test.ClassTesting;
import walkingkooka.type.MemberVisibility;

public final class SpreadsheetEngineCellHateosHandlerTest extends SpreadsheetHateosHandlerTestCase<SpreadsheetEngineCellHateosHandler> {

    @Override
    public Class<SpreadsheetEngineCellHateosHandler> type() {
        return SpreadsheetEngineCellHateosHandler.class;
    }

    @Override
    public String typeNameSuffix() {
        return HateosHandler.class.getSimpleName();
    }
}
