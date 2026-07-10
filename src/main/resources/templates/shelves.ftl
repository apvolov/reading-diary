<#import "layout.ftl" as layout>
<@layout.page title="Полки" fullWidth=true>

<div class="app-shell">
    <@layout.sidebar />
    <main class="main-content">
        <#if shelves?has_content>
        <div class="shelf-grid">
            <#list shelves as shelf>
            <div class="shelf-card">
                <span class="shelf-name">${shelf.name}</span>
                <span class="shelf-count">
                    <#if shelf.bookCount == 1>1 книга
                    <#elseif shelf.bookCount gte 2 && shelf.bookCount lte 4>${shelf.bookCount} книги
                    <#else>${shelf.bookCount} книг
                    </#if>
                </span>
            </div>
            </#list>
        </div>
        <#else>
            <p class="empty-state">Полок пока нет. <a href="/shelves/add">Добавьте первую полку</a>.</p>
        </#if>
    </main>
</div>

</@layout.page>
