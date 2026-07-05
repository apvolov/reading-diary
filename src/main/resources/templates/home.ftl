<#import "layout.ftl" as layout>
<@layout.page title="Мой дневник">

<div class="page-header">
    <h1>Мой дневник</h1>
    <div class="header-actions">
        <a href="/books/add" class="btn-primary">+ Добавить книгу</a>
        <form method="POST" action="/logout" style="display:inline;">
            <button type="submit" class="btn-ghost">Выйти</button>
        </form>
    </div>
</div>

<#if entries?has_content>
    <#list entries as entry>
    <div class="book-card">
        <div class="book-info">
            <span class="book-title">${entry.title}</span>
            <span class="book-author">${entry.author}<#if entry.year??> · ${entry.year}</#if></span>
            <#if entry.rating??>
            <span class="book-rating">${entry.rating} / 10</span>
            </#if>
        </div>

        <div class="book-actions">
            <form method="POST" action="/diary/status" class="status-form">
                <input type="hidden" name="id" value="${entry.id}">
                <select name="status" class="status-select">
                    <option value="PLANNED"  <#if entry.status?string == "PLANNED">selected</#if>>Планирую</option>
                    <option value="READING"  <#if entry.status?string == "READING">selected</#if>>Читаю</option>
                    <option value="READ"     <#if entry.status?string == "READ">selected</#if>>Прочитал</option>
                </select>
                <button type="submit" class="btn-small">Сохранить</button>
            </form>
            <a href="/diary/edit?id=${entry.id}" class="btn-link">Редактировать</a>
        </div>
    </div>
    </#list>
<#else>
    <p class="empty-state">Дневник пуст. <a href="/books/add">Добавьте первую книгу</a>.</p>
</#if>

</@layout.page>
