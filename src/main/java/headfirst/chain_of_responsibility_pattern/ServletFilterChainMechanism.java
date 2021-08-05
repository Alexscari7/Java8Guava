package headfirst.chain_of_responsibility_pattern;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * N
 *
 * @author wusd
 * @date : 2021/08/05 14:30
 */
public class ServletFilterChainMechanism {
    public static void main(String[] args) {
        Filter filterA = new FilterA();
        Filter filterB = new FilterB();
        FilterChain chain = new FilterChain(new Request(), new Response(), filterA, filterB);
        chain.start();
    }
}

class Request {}

class Response {}

interface Filter {
    void doFilter(Request request, Response response, FilterChain filterChain);
}

class FilterA implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain filterChain) {
        System.out.println("过滤器A正在处理请求");
        filterChain.doFilter(request, response);
    }
}

class FilterB implements Filter {
    @Override
    public void doFilter(Request request, Response response, FilterChain filterChain) {
        System.out.println("过滤器B正在处理请求");
        filterChain.doFilter(request, response);
    }
}

class FilterChain {
    private List<Filter> filters;
    private Iterator<Filter> iterator;
    private Request request;
    private Response response;

    public FilterChain(Request request, Response response, Filter... filters) {
        this.request = request;
        this.response = response;
        this.filters = Arrays.asList(filters);
    }

    public void doFilter(Request request, Response response) {
        if (request == null || response == null) {
            throw new IllegalArgumentException("请求不能为空");
        }
        if (iterator == null) {
            iterator = filters.iterator();
        }
        if (iterator.hasNext()) {
            Filter next = iterator.next();
            next.doFilter(request, response, this);
        }
        this.request = request;
        this.response = response;
    }

    public void start() {
        doFilter(this.request, this.response);
    }
}
