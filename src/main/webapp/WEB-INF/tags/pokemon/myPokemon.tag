<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ tag import="java.util.List" %>
<%@ attribute name="seq" type="java.lang.Long" required="true" %>

<%
    List<Long> items = (List<Long>)request.getAttribute("myPokemonSeqs");
    if (items.contains(seq)) {
%>
<div class="my-pokemon on" data-seq="${seq}">
<% } else {%>
<div class="my-pokemon" data-seq="${seq}">
<% }%>
    ♥
</div>