package ru.lookzbs;

import com.blade.mvc.RouteContext;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.handler.RouteHandler;

@Path
public class CallbackHandler implements RouteHandler {

    @Override
    public void handle(RouteContext routeContext) {
        new Commander().doCommand(routeContext);
    }
}
