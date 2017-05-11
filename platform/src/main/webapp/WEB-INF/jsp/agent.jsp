<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Agent info</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
<h2 style="text-align: center; width: 100%; padding: 20px">Agent info: ${agentId}</h2>

<div>
    <div id="send-result"></div>
    <textarea id="message" style="resize: none; width: 100%; height: 100px;"></textarea>
    <button id="send" value="send">Send</button>
</div>
<div id="logs" style="width: 100%; height: 50%"></div>
<script>
    var agentId = "${agentId}";
    var latestDate;
    var timeout;
    $(document).ready(function () {
        var logsContainer = $("#logs");
        var latestUpdate = null;
        var updateLogs = function () {
            $.get(
                    "/api/agent/logs",
                    {
                        agentId: agentId,
                        date: latestUpdate
                    },
                    function (data, status) {
                        if (data.length > 0) {
                            latestUpdate = data[data.length - 1].createdDate;
                        }
                        $.each(data, function (index, logItem) {
                            logsContainer.append("<p>" + JSON.stringify(logItem) + "</p>");
                        });
                        timeout = setTimeout(updateLogs, 2000);
                    }
            );
        };
        updateLogs();

        var messageContainer = $("#message");
        var statusContainer = $("#send-result");
        $("#send").on("click", function () {
            $.post(
                    "/api/agent/send",
                    {
                        agentId: agentId,
                        message: messageContainer.val()
                    },
                    function (data, status) {
                    }
            ).done(function () {
                        statusContainer.html("<span style='color: green'>Success!</span>");
                    }
            ).fail(function () {
                        statusContainer.html("<span style='color: red'>Failed!</span>");
                    }
            );
        })
    });
</script>
</body>
</html>
