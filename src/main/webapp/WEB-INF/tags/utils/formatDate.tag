<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ tag import="java.time.*, java.time.format.*" %>
<%@ attribute name="value" type="java.time.temporal.TemporalAccessor" required="true" %>
<%@ attribute name="pattern" type="java.lang.String" %>
<%
    pattern = pattern == null || pattern.isBlank() ? "yyyy-MM-dd" : pattern;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    out.write(formatter.format(value));
%>

