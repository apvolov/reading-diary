<#macro page title fullWidth=false>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="/static/style.css">
</head>
<body<#if fullWidth> class="full-bleed"</#if>>
<#if fullWidth>
<#nested>
<#else>
<div class="container">
<#nested>
</div>
</#if>
</body>
</html>
</#macro>

<#macro sidebar>
<aside class="sidebar">
    <div class="sidebar-header">
        <img src="/static/img/icon.png" alt="" class="page-icon">
        <h1 class="diary-title">Мой дневник</h1>
    </div>

    <nav class="sidebar-nav">
        <a href="/" class="sidebar-link"><img src="/static/img/all_books.png" alt="" class="sidebar-link-icon">Все книги</a>
        <span class="sidebar-link"><img src="/static/img/shelves.png" alt="" class="sidebar-link-icon">Полки</span>
    </nav>

    <div class="sidebar-spacer"></div>

    <div class="sidebar-nav">
        <a href="/books/add" class="sidebar-link">Добавить книгу</a>
        <span class="sidebar-link">Добавить полку</span>
    </div>

    <div class="sidebar-spacer"></div>

    <form method="POST" action="/logout" class="sidebar-logout-form">
        <button type="submit" class="sidebar-link sidebar-logout-btn"><img src="/static/img/logout.png" alt="" class="sidebar-link-icon">Выйти</button>
    </form>
</aside>
</#macro>
