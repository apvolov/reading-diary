<#import "layout.ftl" as layout>
<@layout.page title="Мой дневник" fullWidth=true>

<div class="app-shell">
    <@layout.sidebar />
    <main class="main-content">
        <#if entries?has_content>
        <div class="book-grid">
            <#list entries as entry>
            <div class="book-card">
                <div class="book-cover-wrap">
                    <span class="status-badge
                        <#if entry.status?string == "READ">status-read
                        <#elseif entry.status?string == "READING">status-reading
                        <#else>status-planned
                        </#if>">
                        <#if entry.status?string == "READ">Прочитано
                        <#elseif entry.status?string == "READING">В процессе
                        <#else>Не приступал
                        </#if>
                    </span>
                    <#if entry.coverFilename??>
                    <img src="/covers/${entry.coverFilename}" alt="Обложка" class="book-cover">
                    <#else>
                    <div class="book-cover book-cover-placeholder">cover not found</div>
                    </#if>
                </div>

                <span class="book-title">${entry.title}</span>

                <div class="book-footer">
                    <span class="book-author">${entry.author}</span>
                    <a href="/diary/edit?id=${entry.id}" class="btn-small">Редактировать</a>
                </div>
            </div>
            </#list>
        </div>
        <#else>
            <p class="empty-state">Дневник пуст. <a href="/books/add">Добавьте первую книгу</a>.</p>
        </#if>
    </main>
</div>

</@layout.page>
