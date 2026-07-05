<#import "layout.ftl" as layout>
<@layout.page title="Регистрация">
<h1>Регистрация</h1>
<#if error??>
<p class="error">${error}</p>
</#if>
<form method="POST" action="/register">
    <input type="text" name="username" placeholder="Имя пользователя" required>
    <input type="email" name="email" placeholder="Email" required>
    <input type="password" name="password" placeholder="Пароль" required>
    <button type="submit">Зарегистрироваться</button>
</form>
<p class="auth-footer"><a href="/login">Уже есть аккаунт? Войти</a></p>
</@layout.page>
