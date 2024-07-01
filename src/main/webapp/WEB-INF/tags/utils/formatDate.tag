<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ tag import="java.time.*" %>
<%@ attribute name="value" type="java.time.TemporalAccessor" required="true" %>
<%@ attribute name="pattern" %>
<%
    pattern = pattern == null || pattern.isBlank() ? "yyyy-MM-dd" : pattern;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    out.write(formatter.format(value));
%>

