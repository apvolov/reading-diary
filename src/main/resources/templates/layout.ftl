<#macro page title>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="/static/style.css">
</head>
<body>
<div class="container">
<#nested>
</div>
</body>
</html>
</#macro>
