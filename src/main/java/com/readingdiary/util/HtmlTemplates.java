package com.readingdiary.util;

public class HtmlTemplates {

    public static String page(String title, String bodyContent) {
        return """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>%s</title>
                    <style>
                        body { font-family: sans-serif; max-width: 480px; margin: 40px auto; }
                        input { display: block; width: 100%%; padding: 8px; margin-bottom: 12px; }
                        button { padding: 8px 16px; }
                        .error { color: red; }
                    </style>
                </head>
                <body>
                %s
                </body>
                </html>
                """.formatted(title, bodyContent);
    }

    public static String registerForm(String errorMessage) {
        String error = errorMessage != null ? "<p class=\"error\">" + errorMessage + "</p>" : "";
        return page("Регистрация", """
                <h1>Регистрация</h1>
                %s
                <form method="POST" action="/register">
                    <input type="text" name="username" placeholder="Имя пользователя" required>
                    <input type="email" name="email" placeholder="Email" required>
                    <input type="password" name="password" placeholder="Пароль" required>
                    <button type="submit">Зарегистрироваться</button>
                </form>
                <p><a href="/login">Уже есть аккаунт? Войти</a></p>
                """.formatted(error));
    }

    public static String loginForm(String errorMessage) {
        String error = errorMessage != null ? "<p class=\"error\">" + errorMessage + "</p>" : "";
        return page("Вход", """
                <h1>Вход</h1>
                %s
                <form method="POST" action="/login">
                    <input type="text" name="username" placeholder="Имя пользователя" required>
                    <input type="password" name="password" placeholder="Пароль" required>
                    <button type="submit">Войти</button>
                </form>
                <p><a href="/register">Нет аккаунта? Зарегистрироваться</a></p>
                """.formatted(error));
    }
}
