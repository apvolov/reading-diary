<#import "layout.ftl" as layout>
<@layout.page title="Личный кабинет">
<h1>Привет, ${username}!</h1>
<p>Пустой читательский дневник.</p>
<form method="POST" action="/logout">
    <button type="submit" class="logout">Выйти</button>
</form>
</@layout.page>
