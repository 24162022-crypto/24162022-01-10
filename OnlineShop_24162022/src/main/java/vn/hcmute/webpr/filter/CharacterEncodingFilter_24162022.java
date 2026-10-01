package vn.hcmute.webpr.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Dam bao request/response luon dung UTF-8 (bat buoc de doc dung du lieu
 * tieng Viet co dau tu form dang ky/dang nhap, ten san pham, v.v.).
 * Neu khong co filter nay, mot so Tomcat mac dinh doc POST body bang
 * ISO-8859-1 va lam sai lech du lieu tieng Viet.
 */
@WebFilter("/*")
public class CharacterEncodingFilter_24162022 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
