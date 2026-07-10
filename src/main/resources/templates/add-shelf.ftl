<#import "layout.ftl" as layout>
<@layout.page title="Добавить полку" fullWidth=true>

<div class="app-shell">
    <@layout.sidebar />

    <main class="main-content main-content-narrow">
        <h1>Добавить полку</h1>

        <#if error??>
        <p class="error">${error}</p>
        </#if>

        <form method="POST" action="/shelves/add">
            <input type="text" name="name" placeholder="Название полки *"
                   value="<#if form??>${form.name!}</#if>" required>
            <button type="submit">Добавить</button>
        </form>
    </main>
</div>

</@layout.page>
