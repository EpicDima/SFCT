# SFCT

Неофициальное мобильное приложение сайта https://kudapostupat.by/

Для парсинга HTML страниц используется библиотека jsoup (https://jsoup.org/). Логика по парсингу
HTML находится в модуле core, который не имеет зависимостей от Android. Это даёт возможность
добавить новый модуль к примеру для отображения в виде десктопного приложения, которое будет
переиспользовать модуль core (не планируется).

В качестве языка программирования выбран Kotlin, потому что сейчас это стандарт для приложений на
Android, особенно для новых проектов.
