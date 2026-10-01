<%
    // Cau 2 se luu "username" va "role" vao session khi dang nhap thanh cong.
    // Cau 1 chi kiem tra ton tai de dung menu dieu kien - hien tai chua co ai dang nhap.
    String username = (session != null) ? (String) session.getAttribute("username") : null;
    String role = (session != null) ? (String) session.getAttribute("role") : null;
    boolean isAdmin = "Admin".equalsIgnoreCase(role);
    // Gio hang chi danh cho role User; cartCount (so mat hang) duoc cap nhat vao Session sau moi thao tac
    boolean isUser = "User".equalsIgnoreCase(role);
    Object cartCountObj = (session != null) ? session.getAttribute("cartCount") : null;
    int cartCount = (cartCountObj instanceof Integer) ? ((Integer) cartCountObj).intValue() : 0;
%>
<header class="site-header">
    <div class="header-inner">
        <a class="brand" href="<%= request.getContextPath() %>/home">OnlineShop_24162022</a>
        <nav class="main-nav">
            <a href="<%= request.getContextPath() %>/home">Trang Chu</a>
            <a href="<%= request.getContextPath() %>/products">San pham</a>
            <% if (username == null) { %>
                <a href="<%= request.getContextPath() %>/login">Dang nhap</a>
            <% } else { %>
                <% if (isUser) { %>
                    <a href="<%= request.getContextPath() %>/cart">Gio hang (<%= cartCount %>)</a>
                    <a href="<%= request.getContextPath() %>/orders">Don hang cua toi</a>
                <% } %>
                <span class="welcome">Xin chao, <%= username %></span>
                <a href="<%= request.getContextPath() %>/logout">Dang xuat</a>
            <% } %>
            <% if (isAdmin) { %>
                <a href="<%= request.getContextPath() %>/admin/dashboard" class="admin-link">Trang quan tri</a>
            <% } %>
        </nav>
    </div>
</header>
