<#import "layout.ftl" as layout>
<@layout.page title="Вход">
<h1>Вход</h1>
<#if error??>
<p class="error">${error}</p>
</#if>
<form method="POST" action="/login">
    <input type="text" name="username" placeholder="Имя пользователя" required>
    <input type="password" name="password" placeholder="Пароль" required>
    <button type="submit">Войти</button>
</form>
<p class="auth-footer"><a href="/register">Нет аккаунта? Зарегистрироваться</a></p>
</@layout.page>
