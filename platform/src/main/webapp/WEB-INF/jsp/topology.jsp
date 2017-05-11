<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Topology</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
<h2 style="text-align: center; width: 100%; padding: 20px">Topology</h2>

<div id="tree"></div>
<script>
    $(document).ready(function () {
        var treeContainer = $("#tree");
        $.get("/api/topology", function (data, status) {
            $.each(data, function (server, agents) {
                var serverNode = "<div style='padding: 10px'><h4><a href='/server/" + server + "'>" + server + "</a></h4>" +
                        "<ul>";
                $.each(agents, function (index, agent) {
                    serverNode += "<li><a href='/agent/" + agent + "'>" + agent + "</a></li>";
                });
                serverNode += "</ul></div>";
                treeContainer.append(serverNode);
            });
        });
    });
</script>
</body>
</html>
