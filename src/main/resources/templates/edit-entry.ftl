<#import "layout.ftl" as layout>
<@layout.page title="Редактировать запись" fullWidth=true>

<div class="app-shell">
    <@layout.sidebar />

    <main class="main-content main-content-wide">
        <div class="edit-layout edit-layout-with-cover">
            <#if entry.coverFilename??>
            <img src="/covers/${entry.coverFilename}" alt="Обложка" class="cover-large">
            <#else>
            <div class="cover-large cover-large-placeholder">обложка не загружена</div>
            </#if>

            <div class="edit-content">
                <div class="edit-title-block">
                    <h1>${entry.title}</h1>
                    <p class="book-author-sub">${entry.author}<#if entry.year??> ${entry.year?c}</#if></p>
                </div>

                <#if error??>
                <p class="error">${error}</p>
                </#if>

                <form method="POST" action="/diary/edit" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${entry.id}">

                    <label>Статус</label>
                    <select name="status" class="status-select">
                        <option value="PLANNED" <#if entry.status?string == "PLANNED">selected</#if>>Планирую</option>
                        <option value="READING" <#if entry.status?string == "READING">selected</#if>>Читаю</option>
                        <option value="READ"    <#if entry.status?string == "READ">selected</#if>>Прочитал</option>
                    </select>

                    <label>Обложка</label>
                    <input type="file" name="cover" accept="image/jpeg,image/png,image/gif,image/webp">

                    <label>Оценка (1–10)</label>
                    <input type="number" name="rating" min="1" max="10"
                           value="${entry.rating!}" placeholder="Оставьте пустым, если не хотите">

                    <label>Дата окончания</label>
                    <input type="date" name="date_finished"
                           value="<#if entry.dateFinished??>${entry.dateFinished}</#if>">

                    <label>Отзыв</label>
                    <textarea name="review" rows="6" placeholder="Ваши впечатления о книге...">${entry.review!}</textarea>

                    <button type="submit" class="btn-save-centered">Сохранить</button>
                </form>
            </div>
        </div>
    </main>
</div>

</@layout.page>
