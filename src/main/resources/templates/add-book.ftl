<#import "layout.ftl" as layout>
<@layout.page title="Добавить книгу">

<h1>Добавить книгу</h1>

<#if error??>
<p class="error">${error}</p>
</#if>

<form method="POST" action="/books/add">
    <input type="text" name="title" placeholder="Название *"
           value="<#if form??>${form.title!}</#if>" required>
    <input type="text" name="author" placeholder="Автор *"
           value="<#if form??>${form.author!}</#if>" required>
    <input type="number" name="year" placeholder="Год (необязательно)"
           value="<#if form??>${form.year!}</#if>" min="1000" max="2100">
    <textarea name="description" placeholder="Описание (необязательно)" rows="4"><#if form??>${form.description!}</#if></textarea>
    <button type="submit">Добавить</button>
</form>

<p class="auth-footer"><a href="/">← Назад к дневнику</a></p>

</@layout.page>
