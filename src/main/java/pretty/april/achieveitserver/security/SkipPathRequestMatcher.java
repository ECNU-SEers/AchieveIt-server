package pretty.april.achieveitserver.security;

import io.jsonwebtoken.lang.Assert;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher skipMatcher;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        Assert.notNull(pathsToSkip);
        List<RequestMatcher> matcherList = pathsToSkip.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        skipMatcher = new OrRequestMatcher(matcherList);
        this.processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        if (skipMatcher.matches(httpServletRequest)) {
            return false;
        }
        return processingMatcher.matches(httpServletRequest);
    }
}
