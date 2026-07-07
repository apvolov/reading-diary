<#import "layout.ftl" as layout>
<@layout.page title="Редактировать запись">

<div class="edit-header">
    <#if entry.coverFilename??>
    <img src="/covers/${entry.coverFilename}" alt="Обложка" class="cover-preview">
    </#if>
    <div>
        <h1>${entry.title}</h1>
        <p class="book-author-sub">${entry.author}<#if entry.year??> · ${entry.year}</#if></p>
    </div>
</div>

<#if error??>
<p class="error">${error}</p>
</#if>

<form method="POST" action="/covers/upload" enctype="multipart/form-data" class="cover-upload-form">
    <input type="hidden" name="id" value="${entry.id}">
    <label>Обложка</label>
    <div class="file-row">
        <input type="file" name="cover" accept="image/jpeg,image/png,image/gif,image/webp">
        <button type="submit" class="btn-small">Загрузить</button>
    </div>
</form>

<form method="POST" action="/diary/edit">
    <input type="hidden" name="id" value="${entry.id}">

    <label>Оценка (1–10)</label>
    <input type="number" name="rating" min="1" max="10"
           value="${entry.rating!}" placeholder="Оставьте пустым, если не хотите">

    <label>Дата окончания</label>
    <input type="date" name="date_finished"
           value="<#if entry.dateFinished??>${entry.dateFinished}</#if>">

    <label>Отзыв</label>
    <textarea name="review" rows="6" placeholder="Ваши впечатления о книге...">${entry.review!}</textarea>

    <button type="submit">Сохранить</button>
</form>

<p class="auth-footer"><a href="/">← Назад к дневнику</a></p>

</@layout.page>
